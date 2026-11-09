# ADR-0013: Permission/consent layer — Android permission state with desktop prompts

- **Status**: Accepted
- **Date**: 2026-11-09

## Context

Android apps expect the runtime-permission model: a manifest
`<uses-permission>` list, `Context.checkSelfPermission`, an
`Activity.requestPermissions` /
`onRequestPermissionsResult` round trip, `PackageManager` permission
queries, and shared-storage access gated behind `READ_EXTERNAL_STORAGE` /
`MEDIA_*` / `MANAGE_EXTERNAL_STORAGE`. None of that existed: manifest
permissions were parsed only for the `apk` summary, the `Context`/`Activity`
permission methods were named gaps, and the guest file bridge mapped
`/sdcard` with no permission concept at all.

ADR-0012 fixes the framing: prompts are **consent, not enforcement**.
Permission dialogs exist for fidelity and user expectations; they do not
constrain a hostile APK that bypasses the framework APIs. The layer below
still has to be faithful for cooperative apps, and it has to fail closed
when no prompt can be shown so headless runs do not silently grant access.

## Decision

**One per-app table, two chokepoints.** `tartine.fw.PermissionManager`
(alongside the `DisplayConfig`/`ThemeMode` host-seam singletons) owns the
app's package name, manifest-declared permissions, target SDK, and the
user's grant/deny decisions. `Main.txLaunch` configures it before any guest
code runs, and the guest reaches it through two doors:

1. **Framework APIs** — `Context.checkSelfPermission`/`checkPermission`/
   `checkCalling*`/`enforce*`, `Activity.requestPermissions`/
   `onRequestPermissionsResult`/`shouldShowRequestPermissionRationale`, and
   `PackageManager` (`checkPermission`, `getPackageInfo`,
   `getPermissionInfo`, group queries, `getPackagesHoldingPermissions`,
   `getTargetSdkVersion`).
2. **The Vfs file bridge** — `GuestFiles`/`GuestFileStreams` gate shared
   storage, since file access bypasses the framework into the host JDK.

**Decision rules** (`PermissionCatalog` carries the protection table):

- Declared normal permissions and declared app-defined permissions the
  catalog does not know are granted at install time (an app-defined
  permission defaults to normal).
- Declared dangerous permissions are denied until the user answers a
  prompt. `check` never prompts; `ensure`/`requestPermissions`/first file
  use does.
- Signature permissions, undeclared permissions, and unknown
  undeclared names stay denied.
- Apps targeting API 22 or older get their declared dangerous permissions
  at install time, matching the pre-runtime-permission model.
- `INTERNET` and the other network permissions are normal, never prompted.

**Prompts go through `PermissionPrompt`** (`GRANTED`/`DENIED`/
`UNAVAILABLE`). The `gui` command installs a Swing dialog; headless `run`
leaves the seam unset, which answers deny **without recording a decision**,
so a later GUI run can still ask. Grants and denials persist as
`granted`/`denied` properties under
`<Tartine root>/permissions/<package>.properties`; a persisted decision is
returned without prompting again.

**Shared storage maps onto the existing tiers.** `/sdcard` and
`/storage/emulated/0` reads need a granted read/media/all-files permission;
writes need a write or all-files grant. The app's own
`/sdcard/Android/{data,media,obb}/<pkg>` directories stay free, as on a
device, and app-private `/data/...` storage never consults the permission
table. A denied read looks nonexistent (`null` metadata,
`FileNotFoundException` from streams); a denied write is a
`SecurityException`, the Java answer for a permission failure. The gate is
active only when an app has been configured, so framework-only runs and
existing Vfs tests keep their behavior.

## Consequences

- Cooperative apps see Android's real permission flow: the dialog appears
  on first use or explicit request, the answer sticks, and
  `checkSelfPermission` reports it. This is the mechanism the audit's P3
  asked for and it uses the Vfs chokepoint that already existed.
- A headless run denies dangerous permissions by design. Storage-using
  probes therefore stop at permission-denied answers until run through
  `gui`; that is the intended consent behavior, not a gap.
- The store file is per package under the Tartine root and is host-owned;
  it is not guest-mapped (the guest sees the `/data` tree, never
  `<root>/permissions`).
- Because consent is not enforcement (ADR-0012), no security claim is made
  about the store or the prompts.
- Not covered here: MediaStore and SAF document pickers (ADR-0005 tier 3),
  per-folder share grants, location/camera/sensor behavior behind the
  framework family ports, and IPC/exported-component checks. They plug
  into the same table when their families land.
