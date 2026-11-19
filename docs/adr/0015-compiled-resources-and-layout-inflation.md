# ADR-0015: Compiled resources and XML layout inflation

- **Status**: Accepted
- **Date**: 2026-11-12
- **Updated**: 2026-11-18 (widget subset, drawables and rendering landed —
  see the 2026-11-18 note in Decision); 2026-11-19 (launcher boundary note
  refreshed: only the network call remains)

## Context

The icon picker was the only consumer of `resources.arsc`: it read simple
values by id and never needed styles. Apps that call
`setContentView(R.layout.activity_x)` need far more — layout XML decoded as a
document, theme/style lookup with `TypedArray`, and an inflater that maps
element names to view classes and builds the tree with real
`ViewGroup.LayoutParams`.

The pieces to build on already existed: `tartine.apk.BinaryXml` decoded AXML
(the manifest path), and `tartine.apk.ResourceTable` decoded chunks. But
`BinaryXml` dropped each attribute's `android`-assigned resource id (the
document's resource-map chunk), `ResourceTable` did not read `FLAG_COMPLEX`
map entries (styles/themes), and there was no seam between the root module's
parsers and the framework's `android.content.res` code. The framework module
cannot depend on the root module (`tartine.apk` lives there), and emulating
AOSP's native `XmlBlock` in Java would duplicate the decoder for no gain.

Two design questions had to be settled: where AXML is parsed, and what the
framework does about themes and inflation without a widget toolkit.

## Decision

**The host parses; the framework consumes a decoded model.** The
`tartine.apk` readers stay the only AXML/`resources.arsc` decoders:

- `BinaryXml` emits each attribute's `resourceId` from the resource-map
  chunk, so name-based and id-based lookups both work.
- `ResourceTable` parses bags (parent + key/value map entries), registers
  name/id lookups for every entry, and selects a value by configuration:
  exact density first, then nearest higher, then nearest lower, rejecting
  configurations that declare a newer platform than the modelled API 36.
- `tartine.fw.AppResourceHost` (framework module) is the seam:
  `value`/`attribute`/`xml`/`arrayValues`/`asset`/`assetList`/`resourceName`/
  `identifier`, carrying `tartine.fw.ResValue`/`xml` `tartine.fw.XmlNode`
  records. `AppResources` holds the installed instance, and
  `tartine.apk.ResourceHostAdapter` is the host implementation.
- `Main` installs the adapter before guest launch and applies the manifest's
  activity theme id. The framework never sees AXML bytes.

**The framework owns resource semantics.** `Resources` reads values,
colors, dimensions and strings through the host, mints `Theme`s and returns
`XmlResourceParser`s; an unknown XML id throws AOSP's `NotFoundException`
(callers such as ConstraintLayout's optional `constraintSet` lookup catch
it), while other unknown ids stay named gaps.
`android.content.res.XmlResourceParserImpl` replays an `XmlNode` as the
pull-parser + `AttributeSet` contract the inflater and
`obtainStyledAttributes` rely on. `Resources.Theme`/`TypedArray` implement
the resolution order: an element's own attributes beat the style chain;
the chain is `defStyleRes` → `defStyleAttr` (resolved as a theme reference)
→ the applied theme styles (last wins) → the element's `style` attribute;
a `TYPE_ATTRIBUTE` value follows one hop. Themes attach through
`android.app.ContextThemeWrapper`, which `Activity` extends; the public
`android.view.ContextThemeWrapper` delegates to it.

**Inflation mirrors AOSP's structure.** `android.view.LayoutInflater` folds
in the `PhoneLayoutInflater` name prefixes, gives `Factory`/`Factory2`/the
app's `onCreateView` first refusal, resolves classes through the thread
context class loader (the guest loader at runtime), handles
`merge`/`requestFocus`/`tag`, generates each child's params from the parent
container, and runs `onFinishInflate`. `ViewGroup` gains the
`generateLayoutParams(AttributeSet)` fallback (AOSP's is abstract) and
`LayoutParams(Context, AttributeSet)` reads `layout_width`/`layout_height`
through the styled-attribute chain.

**`Context.getClassLoader` is translator-rewritten.** AppCompat resolves its
view-inflater class through `context.getClassLoader().loadClass(name)`. The
framework `Context` is host code, so returning a host loader would leak
host classes (and a raw host loader is not a `GuestClassLoader`). Instead
`SandboxPass` pops the receiver and calls `GuestSandbox.classLoader()`,
which wraps the calling thread's `TranslatedLoader`; lookups then go through
the sandbox allowlist like every other guest class lookup.

**The widget subset follows the probe walls (2026-11-18 update).** The
follow-up ported the `android.widget` slice the layout probe exercises —
`LinearLayout`/`FrameLayout`/`ScrollView`/`HorizontalScrollView`/`TextView`
(+`Button`/`EditText`/`CheckBox`/`RadioButton` via `CompoundButton`)/
`ImageView` (+`ImageButton`)/`ProgressBar`/`Space`, with `OverScroller` and
the view side they need (`Gravity`, the animation interpolators,
`Editable`/`SpannableStringBuilder`/`TextUtils` for text measurement).
Platform base classes are ported faithfully and stay host framework code;
androidx/material classes remain guest DEX running over the framework.
Measure/layout reuse the Compose-path `View` pipeline, and draw maps onto
the existing `Canvas`/`Paint` seam. `Resources.getDrawable` now parses
drawable XML into the no-bitmap family (color/shape/gradient/inset/layer/
state-list/ripple/vector shells, plus `Shape`/`RectShape`/`OvalShape`);
bitmap-backed framework drawables degrade to a logged
`framework drawable unavailable (bitmap boundary)` warning instead of a
hard wall — `BitmapFactory`/`Canvas.drawBitmap` remain future work — and
framework state-list *colors* stay unresolved because androidx inflates its
own XML selectors. The probe (`apks/app-debug-layout-xml.apk` via
`mise run run`/`gui ... --activity`) now completes `onCreate`, measure,
layout, draw and one presented frame with no uncaught exception.

**Non-launcher activities can be probed.** `Main` accepts
`--activity/-a <name>` (and `run`/`gui` take an optional `[activity]`
argument) to launch an activity other than the manifest launcher. The
fixture's real launcher now completes field initialization
(`new OkHttpClient()` passes after the JUL/`javax.net`/proxy-metadata
linkage widening, ADR-0009); its worker thread then hits the network sandbox
on the first name lookup (`HostnamesKt.toCanonicalHost` ends in a denied
`java.net.InetAddress`), and the app catches only `IOException`, so that
boundary still surfaces as an uncaught background exception.
`DebugConsoleActivity` exists precisely to be inflatable without the network
path.

## Consequences

- Apps that call `setContentView(R.layout...)` run the real framework path —
  resource lookup, styling, parser replay, tree building, measure/layout,
  draw — into one presented frame for the layout probe. Unported widget
  families still surface as inflation failures naming the class.
- The resource/inflation framework tests run against a hand-built
  `tartine.fw.FakeAppResourceHost` (no APK needed), while the host-side
  `ResourceHostAdapter` tests keep driving the real `resources.arsc`/AXML
  readers, so both halves stay pinned.
- The host/framework split keeps `BinaryXml`/`ResourceTable` reusable for
  the future general resource loader (qualifier buckets, `<include>`,
  selector/drawable XML), none of which is implemented here.
- Known limits carried by this decision: no `<include>` resolution, no
  namespace prefixes (compiled documents carry URIs), no resource text
  events, no configuration selection beyond density/platform, no bitmap
  rendering (framework drawables degrade with a warning), and framework
  state-list colors unresolved. A guest probe of a non-launcher activity may
  still hit the network denial first, which is why `--activity` is a flag
  rather than a manifest behavior.
