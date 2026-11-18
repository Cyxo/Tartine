package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.Spanned;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * AOSP {@code android.widget.TextView}, ported for the label/button/banner
 * surfaces layout XML uses: styled text measured through
 * {@link StaticLayout}, gravity alignment, max-lines/ellipsis truncation,
 * hint text, compound drawable slots and the state-aware text color.
 *
 * <p>Staging reductions: editable text is kept as a plain {@link CharSequence}
 * (the shell has no {@code Editable}/{@code SpannableStringBuilder} yet), so
 * {@link BufferType#EDITABLE} behaves like {@link BufferType#NORMAL};
 * transformation methods, movement methods and editor actions are not
 * ported; compound drawables reserve padding but only bitmap/vector drawables
 * decide their own rasterisation.
 */
public class TextView extends View {

    /** AOSP TextView.BufferType: how the text content is stored. */
    public enum BufferType {
        NORMAL, SPANNABLE, EDITABLE
    }

    private static final int ATTR_TEXT = 0x0101014F;
    private static final int ATTR_TEXT_COLOR = 0x01010098;
    private static final int ATTR_TEXT_COLOR_PRIMARY = 0x01010036;
    private static final int ATTR_TEXT_COLOR_HINT = 0x0101009A;
    private static final int ATTR_TEXT_SIZE = 0x01010095;
    private static final int ATTR_TEXT_STYLE = 0x01010097;
    private static final int ATTR_TEXT_APPEARANCE = 0x01010034;
    private static final int ATTR_TEXT_ALL_CAPS = 0x0101038C;
    private static final int ATTR_HINT = 0x01010150;
    private static final int ATTR_GRAVITY = 0x010100AF;
    private static final int ATTR_MAX_LINES = 0x01010153;
    private static final int ATTR_LINES = 0x01010154;
    private static final int ATTR_SINGLE_LINE = 0x0101015D;
    private static final int ATTR_INCLUDE_FONT_PADDING = 0x0101015F;
    private static final int ATTR_ELLIPSIZE = 0x010100AB;
    private static final int ATTR_LINE_SPACING_EXTRA = 0x01010217;
    private static final int ATTR_LINE_SPACING_MULTIPLIER = 0x01010218;
    private static final int ATTR_LETTER_SPACING = 0x010104B6;
    private static final int ATTR_DRAWABLE_PADDING = 0x01010171;

    private static final int TEXT_VIEW_STYLE = 0x01010084;

    /** AOSP TextView.NO_FILTERS: the shared empty filter chain. */
    private static final InputFilter[] NO_FILTERS = new InputFilter[0];

    private CharSequence mText = "";
    private InputFilter[] mFilters = NO_FILTERS;

    /** AOSP TextView.mEditableFactory: installed by editors/emoji helpers. */
    private Editable.Factory mEditableFactory = Editable.Factory.getInstance();

    /**
     * AOSP TextView.mInput's key-listener surface. The shell has no editable
     * input pipeline; the listener is staged so AndroidX's emoji wrapper can
     * read and reinstall it.
     */
    private KeyListener mKeyListener;
    private CharSequence mHint;
    private ColorStateList mTextColor;
    private ColorStateList mHintTextColor;
    private int mCurTextColor;
    private int mCurHintTextColor;
    private float mTextSize;
    private int mTextStyle;
    private Typeface mTypeface;
    private int mGravity = Gravity.TOP | Gravity.START;
    private int mMaxLines = Integer.MAX_VALUE;
    private TextUtils.TruncateAt mEllipsize;
    private boolean mIncludeFontPadding = true;
    private boolean mAllCaps;
    private TransformationMethod mTransformationMethod;
    private boolean mSingleLine;
    private boolean mHorizontallyScrolling;
    private float mLineSpacingExtra;
    private float mLineSpacingMultiplier = 1.0f;
    private float mLetterSpacing;
    private int mCompoundDrawablePadding;
    private Drawable[] mCompoundDrawables = new Drawable[4];
    private TextPaint mTextPaint;
    private Layout mLayout;
    private int mLayoutWidth = -1;
    private CharSequence mError;
    private boolean mFreezesText;
    private int mSelectionStart;
    private int mSelectionEnd;
    private boolean mTextIsSelectable;
    private int mAutoLinkMask;
    private boolean mLinksClickable = true;
    private boolean mExplicitTextColor;
    private final List<TextWatcher> mTextWatchers = new ArrayList<>();

    public TextView(Context context) {
        this(context, null, 0);
    }

    /** AOSP {@code TextView(Context, AttributeSet)} (textViewStyle def). */
    public TextView(Context context, AttributeSet attrs) {
        this(context, attrs, TEXT_VIEW_STYLE);
    }

    /**
     * AOSP {@code TextView(Context, AttributeSet, int)}: reads the text
     * appearance and the layout-facing text attributes, with the caller's
     * style attribute (for example {@code buttonStyle}) in the def-style
     * slot.
     */
    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * AOSP {@code TextView(Context, AttributeSet, int, int)}: the theme style
     * chain resolves through {@link Context#obtainStyledAttributes}, then
     * explicit XML attributes win.
     */
    public TextView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr);
        mTypeface = Typeface.DEFAULT;
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        mTextSize = 15.0f * (density > 0.0f ? density : 1.0f);
        mTextColor = ColorStateList.valueOf(0xFF000000);
        mCurTextColor = mTextColor.getDefaultColor();
        mCurHintTextColor = 0xFF808080;
        setWillNotDraw(false);
        readAttributes(context, attrs, defStyleAttr, defStyleRes);
        applyTextAppearance(context, attrs);
        updateTextColors();
    }

    /** Reads the attributes TextView itself owns. */
    private void readAttributes(Context context, AttributeSet attrs,
            int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, new int[] {
                ATTR_TEXT, ATTR_TEXT_COLOR, ATTR_TEXT_SIZE, ATTR_TEXT_STYLE,
                ATTR_HINT, ATTR_GRAVITY, ATTR_MAX_LINES, ATTR_LINES,
                ATTR_SINGLE_LINE, ATTR_INCLUDE_FONT_PADDING, ATTR_ELLIPSIZE,
                ATTR_LINE_SPACING_EXTRA, ATTR_LINE_SPACING_MULTIPLIER,
                ATTR_TEXT_ALL_CAPS, ATTR_LETTER_SPACING,
                ATTR_DRAWABLE_PADDING}, defStyleAttr, defStyleRes);
        try {
            if (a.hasValue(0)) {
                mText = a.getText(0);
            }
            if (a.hasValue(1)) {
                mTextColor = a.getColorStateList(1);
                mExplicitTextColor = true;
            }
            if (a.hasValue(2)) {
                mTextSize = a.getDimensionPixelSize(2, (int) mTextSize);
            }
            if (a.hasValue(3)) {
                mTextStyle = a.getInt(3, Typeface.NORMAL);
            }
            if (a.hasValue(4)) {
                mHint = a.getText(4);
            }
            if (a.hasValue(5)) {
                mGravity = a.getInt(5, mGravity);
            }
            if (a.hasValue(6)) {
                mMaxLines = a.getInt(6, mMaxLines);
            } else if (a.hasValue(7)) {
                mMaxLines = a.getInt(7, mMaxLines);
            }
            if (a.getBoolean(8, false)) {
                applySingleLine(true);
            }
            mIncludeFontPadding = a.getBoolean(9, true);
            if (a.hasValue(10)) {
                switch (a.getInt(10, 0)) {
                    case 1:
                        mEllipsize = TextUtils.TruncateAt.START;
                        break;
                    case 2:
                        mEllipsize = TextUtils.TruncateAt.MIDDLE;
                        break;
                    case 3:
                        mEllipsize = TextUtils.TruncateAt.END;
                        break;
                    case 4:
                        mEllipsize = TextUtils.TruncateAt.MARQUEE;
                        break;
                    default:
                        mEllipsize = null;
                        break;
                }
            }
            mLineSpacingExtra = a.getDimension(11, 0.0f);
            mLineSpacingMultiplier = a.getFloat(12, 1.0f);
            mAllCaps = a.getBoolean(13, false);
            mLetterSpacing = a.getFloat(14, 0.0f);
            mCompoundDrawablePadding = a.getDimensionPixelSize(15, 0);
        } finally {
            a.recycle();
        }
        if (!aThemeHasTextColor(context)) {
            TypedArray theme = context.obtainStyledAttributes(
                    new int[] {ATTR_TEXT_COLOR_PRIMARY});
            try {
                if (theme.hasValue(0)) {
                    mTextColor = theme.getColorStateList(0);
                }
            } finally {
                theme.recycle();
            }
        }
        TypedArray hint = context.obtainStyledAttributes(
                new int[] {ATTR_TEXT_COLOR_HINT});
        try {
            if (hint.hasValue(0)) {
                mHintTextColor = hint.getColorStateList(0);
            }
        } finally {
            hint.recycle();
        }
    }

    /** True while the explicit textColor attribute is present. */
    private boolean aThemeHasTextColor(Context context) {
        // The primary text color is applied only when the view carries no
        // explicit android:textColor; a second lookup of the same attribute
        // would return the theme value anyway, so track it from the state.
        return mExplicitTextColor;
    }

    /** Applies an {@code android:textAppearance} style when present. */
    private void applyTextAppearance(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                new int[] {ATTR_TEXT_APPEARANCE});
        int appearance = 0;
        try {
            appearance = a.getResourceId(0, 0);
        } finally {
            a.recycle();
        }
        if (appearance != 0) {
            setTextAppearance(context, appearance);
        }
    }

    /**
     * AOSP {@code TextView.setTextAppearance(int)} (API 23+): the same style
     * read, resolved against this view's context.
     */
    public void setTextAppearance(int resId) {
        setTextAppearance(getContext(), resId);
    }

    /** AOSP {@code TextView.setTextAppearance(Context, int)}. */
    @Deprecated
    public void setTextAppearance(Context context, int resId) {
        if (resId == 0) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(resId, new int[] {
                ATTR_TEXT_COLOR, ATTR_TEXT_SIZE, ATTR_TEXT_STYLE,
                ATTR_TEXT_ALL_CAPS, ATTR_LINE_SPACING_EXTRA,
                ATTR_LINE_SPACING_MULTIPLIER, ATTR_LETTER_SPACING});
        try {
            if (a.hasValue(0)) {
                mTextColor = a.getColorStateList(0);
            }
            if (a.hasValue(1)) {
                mTextSize = a.getDimensionPixelSize(1, (int) mTextSize);
            }
            if (a.hasValue(2)) {
                mTextStyle = a.getInt(2, mTextStyle);
            }
            mAllCaps = a.getBoolean(3, mAllCaps);
            mLineSpacingExtra = a.getDimension(4, mLineSpacingExtra);
            mLineSpacingMultiplier = a.getFloat(5, mLineSpacingMultiplier);
            mLetterSpacing = a.getFloat(6, mLetterSpacing);
        } finally {
            a.recycle();
        }
        applyTypeface();
        updateTextColors();
        requestLayout();
        invalidate();
    }

    /** Applies the current color list against the live drawable state. */
    private void updateTextColors() {
        if (mTextColor != null) {
            mCurTextColor = mTextColor.getColorForState(getDrawableState(),
                    mTextColor.getDefaultColor());
        }
        if (mHintTextColor != null) {
            mCurHintTextColor = mHintTextColor.getColorForState(
                    getDrawableState(), mHintTextColor.getDefaultColor());
        }
        if (mTextPaint != null) {
            mTextPaint.setColor(mCurTextColor);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateTextColors();
        invalidate();
    }

    /** The shared text paint, created on first use. */
    public TextPaint getPaint() {
        if (mTextPaint == null) {
            mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
            mTextPaint.density = getResources().getDisplayMetrics().density;
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setColor(mCurTextColor);
            applyTypeface();
        }
        return mTextPaint;
    }

    private void applyTypeface() {
        Typeface base = mTypeface == null ? Typeface.DEFAULT : mTypeface;
        if (mTextStyle != Typeface.NORMAL) {
            base = Typeface.create(base, mTextStyle);
        }
        if (mTextPaint != null) {
            mTextPaint.setTypeface(base);
        }
    }

    public CharSequence getText() {
        return mText == null ? "" : mText;
    }

    public void setText(CharSequence text) {
        setText(text, BufferType.NORMAL);
    }

    public void setText(int resid) {
        setText(getContext().getResources().getText(resid));
    }

    /**
     * AOSP {@code TextView.setText(CharSequence, BufferType)}: the staging
     * character store is one {@link CharSequence}; EDITABLE and SPANNABLE keep
     * the same content until the editable model is ported.
     */
    public void setText(CharSequence text, BufferType type) {
        CharSequence old = getText();
        CharSequence next = text == null ? "" : text;
        if (mFilters.length > 0) {
            // AOSP applies the filter chain while committing a text change
            // (TextView.setText -> applyFilters). The destination is the old
            // text; the filter contract wants it as a Spanned, so a plain
            // sequence is wrapped.
            Spanned dest = old instanceof Spanned
                    ? (Spanned) old : new SpannableStringBuilder(old);
            for (InputFilter filter : mFilters) {
                CharSequence out = filter.filter(next, 0, next.length(),
                        dest, 0, old.length());
                if (out != null) {
                    next = out;
                }
            }
        }
        mText = next;
        mSelectionStart = 0;
        mSelectionEnd = mText.length();
        if (mLayoutWidth > 0) {
            rebuildLayout(mLayoutWidth);
        }
        if (!mTextWatchers.isEmpty()) {
            for (TextWatcher watcher : mTextWatchers) {
                watcher.beforeTextChanged(old, 0, old.length(),
                        mText.length());
            }
            for (TextWatcher watcher : mTextWatchers) {
                watcher.onTextChanged(mText, 0, old.length(), mText.length());
            }
            Editable editable = mText instanceof Editable
                    ? (Editable) mText : new SpannableStringBuilder(mText);
            for (TextWatcher watcher : mTextWatchers) {
                watcher.afterTextChanged(editable);
            }
        }
        requestLayout();
        invalidate();
    }

    /**
     * AOSP {@code TextView.getEditableText}: the editable store, or null when
     * the view was not built as an editable field.
     */
    public Editable getEditableText() {
        return mText instanceof Editable ? (Editable) mText : null;
    }

    /** Registers a text-change watcher. */
    public void addTextChangedListener(TextWatcher watcher) {
        if (watcher != null && !mTextWatchers.contains(watcher)) {
            mTextWatchers.add(watcher);
        }
    }

    /** Unregisters a text-change watcher. */
    public void removeTextChangedListener(TextWatcher watcher) {
        mTextWatchers.remove(watcher);
    }

    /**
     * AOSP {@code TextView.getFilters}: the chain applied on every text
     * change, or the empty shared array.
     */
    public InputFilter[] getFilters() {
        return mFilters;
    }

    /**
     * AOSP {@code TextView.setFilters}: null is rejected; the array is held
     * by reference (the editable-model filter state lands with the editable
     * buffer port).
     */
    public void setFilters(InputFilter[] filters) {
        if (filters == null) {
            throw new IllegalArgumentException();
        }
        mFilters = filters;
    }

    /**
     * AOSP {@code TextView.setEditableFactory}: installs the factory and
     * re-applies the current text so the new buffer type takes effect.
     */
    public void setEditableFactory(Editable.Factory factory) {
        mEditableFactory = factory;
        setText(mText);
    }

    /** AOSP {@code TextView.getEditableFactory}. */
    public Editable.Factory getEditableFactory() {
        return mEditableFactory;
    }

    /**
     * AOSP {@code TextView.getKeyListener} (final on the platform): the
     * staged listener, or null when no listener is installed.
     */
    public final KeyListener getKeyListener() {
        return mKeyListener;
    }

    /**
     * AOSP {@code TextView.setKeyListener}: installs the listener. The
     * platform also derives the input type and recreates the editable
     * buffer; with no input pipeline the shell stages the listener only.
     */
    public void setKeyListener(KeyListener input) {
        mKeyListener = input;
    }

    public int length() {
        return getText().length();
    }

    public void append(CharSequence text) {
        setText(getText().toString() + (text == null ? "" : text));
    }

    public CharSequence getHint() {
        return mHint;
    }

    public void setHint(CharSequence hint) {
        mHint = hint;
        invalidate();
    }

    public void setHint(int resid) {
        setHint(getContext().getResources().getText(resid));
    }

    public int getCurrentTextColor() {
        return mCurTextColor;
    }

    public ColorStateList getTextColors() {
        return mTextColor;
    }

    public void setTextColor(int color) {
        setTextColor(ColorStateList.valueOf(color));
    }

    public void setTextColor(ColorStateList colors) {
        mTextColor = colors;
        mExplicitTextColor = true;
        updateTextColors();
        invalidate();
    }

    public void setHintTextColor(int color) {
        mHintTextColor = ColorStateList.valueOf(color);
        updateTextColors();
        invalidate();
    }

    public void setHintTextColor(ColorStateList colors) {
        mHintTextColor = colors;
        updateTextColors();
        invalidate();
    }

    public final ColorStateList getHintTextColors() {
        return mHintTextColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTextSize(int unit, float size) {
        mTextSize = TypedValue.applyDimension(unit, size,
                getResources().getDisplayMetrics());
        getPaint().setTextSize(mTextSize);
        rebuildLayoutIfMeasured();
    }

    public void setTypeface(Typeface tf) {
        if (mTypeface != tf) {
            mTypeface = tf;
            applyTypeface();
            rebuildLayoutIfMeasured();
        }
    }

    public void setTypeface(Typeface tf, int style) {
        mTextStyle = style;
        setTypeface(tf);
    }

    public Typeface getTypeface() {
        return mTypeface;
    }

    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            rebuildLayoutIfMeasured();
        }
    }

    public void setSingleLine() {
        setSingleLine(true);
    }

    /**
     * AOSP {@code TextView.setSingleLine(boolean)}: one line, horizontally
     * scrolling, and an end ellipsis by default.
     */
    public void setSingleLine(boolean singleLine) {
        mSingleLine = singleLine;
        applySingleLine(singleLine);
    }

    private void applySingleLine(boolean singleLine) {
        if (singleLine) {
            mMaxLines = 1;
            mHorizontallyScrolling = true;
            if (mEllipsize == null) {
                mEllipsize = TextUtils.TruncateAt.END;
            }
        } else {
            mMaxLines = Integer.MAX_VALUE;
            mHorizontallyScrolling = false;
        }
        rebuildLayoutIfMeasured();
    }

    public int getMaxLines() {
        return mMaxLines;
    }

    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines <= 0 ? Integer.MAX_VALUE : maxLines;
        rebuildLayoutIfMeasured();
    }

    public void setLines(int lines) {
        setMaxLines(lines);
    }

    public int getLineCount() {
        Layout layout = getLayout();
        return layout == null ? 0 : layout.getLineCount();
    }

    public TextUtils.TruncateAt getEllipsize() {
        return mEllipsize;
    }

    public void setEllipsize(TextUtils.TruncateAt where) {
        mEllipsize = where;
        rebuildLayoutIfMeasured();
    }

    public boolean getIncludeFontPadding() {
        return mIncludeFontPadding;
    }

    public void setIncludeFontPadding(boolean includePad) {
        mIncludeFontPadding = includePad;
        rebuildLayoutIfMeasured();
    }

    public void setAllCaps(boolean allCaps) {
        mAllCaps = allCaps;
        rebuildLayoutIfMeasured();
    }

    /** AOSP getTransformationMethod: the display filter, null by default. */
    public final TransformationMethod getTransformationMethod() {
        return mTransformationMethod;
    }

    /** AOSP setTransformationMethod: records the filter and redraws. */
    public void setTransformationMethod(TransformationMethod method) {
        if (mTransformationMethod == method) {
            return;
        }
        mTransformationMethod = method;
        rebuildLayoutIfMeasured();
    }

    public void setLineSpacing(float add, float mult) {
        mLineSpacingExtra = add;
        mLineSpacingMultiplier = mult;
        rebuildLayoutIfMeasured();
    }

    public float getLineSpacingExtra() {
        return mLineSpacingExtra;
    }

    public float getLineSpacingMultiplier() {
        return mLineSpacingMultiplier;
    }

    public float getLetterSpacing() {
        return mLetterSpacing;
    }

    public void setLetterSpacing(float letterSpacing) {
        mLetterSpacing = letterSpacing;
        rebuildLayoutIfMeasured();
    }

    public void setHorizontallyScrolling(boolean whether) {
        mHorizontallyScrolling = whether;
        rebuildLayoutIfMeasured();
    }

    public boolean getHorizontallyScrolling() {
        return mHorizontallyScrolling;
    }

    public void setTextIsSelectable(boolean selectable) {
        mTextIsSelectable = selectable;
    }

    public boolean isTextSelectable() {
        return mTextIsSelectable;
    }

    public int getAutoLinkMask() {
        return mAutoLinkMask;
    }

    public void setAutoLinkMask(int mask) {
        mAutoLinkMask = mask;
    }

    public boolean getLinksClickable() {
        return mLinksClickable;
    }

    public void setLinksClickable(boolean whether) {
        mLinksClickable = whether;
    }

    public boolean getFreezesText() {
        return mFreezesText;
    }

    public void setFreezesText(boolean freezesText) {
        mFreezesText = freezesText;
    }

    public CharSequence getError() {
        return mError;
    }

    public void setError(CharSequence error) {
        mError = error;
        invalidate();
    }

    public int getSelectionStart() {
        return mSelectionStart;
    }

    public int getSelectionEnd() {
        return mSelectionEnd;
    }

    public void setSelection(int index) {
        setSelection(index, index);
    }

    public void setSelection(int start, int stop) {
        mSelectionStart = Math.max(0, Math.min(start, length()));
        mSelectionEnd = Math.max(0, Math.min(stop, length()));
        invalidate();
    }

    public void setCompoundDrawables(Drawable left, Drawable top,
            Drawable right, Drawable bottom) {
        mCompoundDrawables = new Drawable[] {left, top, right, bottom};
        requestLayout();
        invalidate();
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
            Drawable top, Drawable right, Drawable bottom) {
        setCompoundDrawables(left, top, right, bottom);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top,
            int right, int bottom) {
        setCompoundDrawablesWithIntrinsicBounds(
                left == 0 ? null : getResources().getDrawable(left),
                top == 0 ? null : getResources().getDrawable(top),
                right == 0 ? null : getResources().getDrawable(right),
                bottom == 0 ? null : getResources().getDrawable(bottom));
    }

    public void setCompoundDrawablesRelative(Drawable start, Drawable top,
            Drawable end, Drawable bottom) {
        setCompoundDrawables(start, top, end, bottom);
    }

    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable start,
            Drawable top, Drawable end, Drawable bottom) {
        setCompoundDrawablesRelative(start, top, end, bottom);
    }

    public Drawable[] getCompoundDrawables() {
        return mCompoundDrawables.clone();
    }

    public Drawable[] getCompoundDrawablesRelative() {
        return getCompoundDrawables();
    }

    public int getCompoundDrawablePadding() {
        return mCompoundDrawablePadding;
    }

    public void setCompoundDrawablePadding(int pad) {
        mCompoundDrawablePadding = pad;
        requestLayout();
        invalidate();
    }

    public int getCompoundPaddingLeft() {
        return getPaddingLeft() + compoundWidth(mCompoundDrawables[0]);
    }

    public int getCompoundPaddingRight() {
        return getPaddingRight() + compoundWidth(mCompoundDrawables[2]);
    }

    public int getCompoundPaddingTop() {
        return getPaddingTop() + compoundHeight(mCompoundDrawables[1]);
    }

    public int getCompoundPaddingBottom() {
        return getPaddingBottom() + compoundHeight(mCompoundDrawables[3]);
    }

    public int getCompoundPaddingStart() {
        return getCompoundPaddingLeft();
    }

    public int getCompoundPaddingEnd() {
        return getCompoundPaddingRight();
    }

    private int compoundWidth(Drawable drawable) {
        if (drawable == null) {
            return 0;
        }
        int width = drawable.getIntrinsicWidth();
        return width > 0 ? width + mCompoundDrawablePadding : 0;
    }

    private int compoundHeight(Drawable drawable) {
        if (drawable == null) {
            return 0;
        }
        int height = drawable.getIntrinsicHeight();
        return height > 0 ? height + mCompoundDrawablePadding : 0;
    }

    public int getTotalPaddingLeft() {
        return getCompoundPaddingLeft();
    }

    public int getTotalPaddingRight() {
        return getCompoundPaddingRight();
    }

    public int getTotalPaddingTop() {
        return getExtendedPaddingTop();
    }

    public int getTotalPaddingBottom() {
        return getCompoundPaddingBottom();
    }

    /** AOSP {@code TextView.getExtendedPaddingTop}: the text area's top. */
    public int getExtendedPaddingTop() {
        return getCompoundPaddingTop() + getVerticalOffset(true);
    }

    private int getVerticalOffset(boolean forceNormal) {
        if (mLayout == null) {
            return 0;
        }
        int innerHeight = getHeight() - getCompoundPaddingTop()
                - getCompoundPaddingBottom();
        int textHeight = mLayout.getHeight();
        if (innerHeight <= textHeight) {
            return 0;
        }
        int verticalGravity = Gravity.getAbsoluteGravity(mGravity,
                getLayoutDirection()) & Gravity.VERTICAL_GRAVITY_MASK;
        if (verticalGravity == Gravity.CENTER_VERTICAL) {
            return (innerHeight - textHeight) / 2;
        }
        if (verticalGravity == Gravity.BOTTOM) {
            return innerHeight - textHeight;
        }
        return 0;
    }

    /** The layout built for the last measure pass, or null before one. */
    public Layout getLayout() {
        return mLayout;
    }

    private void rebuildLayoutIfMeasured() {
        if (mLayoutWidth > 0) {
            rebuildLayout(mLayoutWidth);
        }
        requestLayout();
        invalidate();
    }

    private CharSequence displayText() {
        CharSequence text = getText();
        if (mTransformationMethod != null) {
            CharSequence transformed =
                    mTransformationMethod.getTransformation(text, this);
            if (transformed != null) {
                return transformed;
            }
        }
        if (!mAllCaps) {
            return text;
        }
        return text.toString().toUpperCase();
    }

    private void rebuildLayout(int width) {
        TextPaint paint = getPaint();
        paint.setTextSize(mTextSize);
        paint.setColor(mCurTextColor);
        applyTypeface();
        int alignment = Gravity.getAbsoluteGravity(mGravity,
                getLayoutDirection()) & Gravity.HORIZONTAL_GRAVITY_MASK;
        Layout.Alignment align = alignment == Gravity.CENTER_HORIZONTAL
                ? Layout.Alignment.ALIGN_CENTER
                : alignment == Gravity.RIGHT ? Layout.Alignment.ALIGN_OPPOSITE
                        : Layout.Alignment.ALIGN_NORMAL;
        CharSequence content = displayText();
        mLayoutWidth = width;
        mLayout = StaticLayout.Builder
                .obtain(content, 0, content.length(), paint, width)
                .setAlignment(align)
                .setIncludePad(mIncludeFontPadding)
                .setLineSpacing(mLineSpacingExtra, mLineSpacingMultiplier)
                .setMaxLines(mMaxLines)
                .setEllipsize(mEllipsize)
                .setEllipsizedWidth(width)
                .build();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int horizontalPadding = getCompoundPaddingLeft()
                + getCompoundPaddingRight();
        final int verticalPadding = getCompoundPaddingTop()
                + getCompoundPaddingBottom();

        int available = widthMode == MeasureSpec.MODE_UNSPECIFIED
                ? Integer.MAX_VALUE - horizontalPadding : widthSize;
        int layoutWidth = Math.max(1, available - horizontalPadding);
        rebuildLayout(layoutWidth);
        int desiredWidth = horizontalPadding;
        for (int line = 0; line < mLayout.getLineCount(); line++) {
            desiredWidth = Math.max(desiredWidth,
                    (int) Math.ceil(mLayout.getLineMax(line))
                            + horizontalPadding);
        }
        if (widthMode == MeasureSpec.MODE_UNSPECIFIED) {
            layoutWidth = Math.max(1, desiredWidth - horizontalPadding);
            rebuildLayout(layoutWidth);
            desiredWidth = horizontalPadding;
            for (int line = 0; line < mLayout.getLineCount(); line++) {
                desiredWidth = Math.max(desiredWidth,
                        (int) Math.ceil(mLayout.getLineMax(line))
                                + horizontalPadding);
            }
        }
        desiredWidth = Math.max(desiredWidth, getSuggestedMinimumWidth());
        final int measuredWidth = resolveSizeAndState(desiredWidth,
                widthMeasureSpec, 0);
        int desiredHeight = mLayout.getHeight() + verticalPadding;
        if (mLayout.getLineCount() == 0) {
            // An empty TextView still occupies one line box on the device;
            // the shared Layout treats empty text as zero lines.
            desiredHeight += (int) Math.ceil(getPaint().getFontSpacing());
        }
        desiredHeight = Math.max(desiredHeight, getSuggestedMinimumHeight());
        final int measuredHeight = resolveSizeAndState(desiredHeight,
                heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLayout == null) {
            return;
        }
        if (TextUtils.isEmpty(getText()) && mHint != null) {
            CharSequence saved = mText;
            int savedColor = mCurTextColor;
            mText = mHint;
            mCurTextColor = mCurHintTextColor;
            applyHintLayout(canvas);
            mText = saved;
            mCurTextColor = savedColor;
            return;
        }
        int save = canvas.save();
        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        mLayout.draw(canvas);
        canvas.restoreToCount(save);
    }

    /** Draws hint text with the hint color without touching the stored text. */
    private void applyHintLayout(Canvas canvas) {
        TextPaint paint = getPaint();
        int color = paint.getColor();
        paint.setColor(mCurHintTextColor);
        String hint = mHint.toString();
        Layout hintLayout = hint.isEmpty() ? null
                : StaticLayout.Builder.obtain(hint, 0, hint.length(), paint,
                        Math.max(1, mLayoutWidth)).setIncludePad(false).build();
        if (hintLayout != null) {
            int save = canvas.save();
            canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
            hintLayout.draw(canvas);
            canvas.restoreToCount(save);
        }
        paint.setColor(color);
    }

    @Override
    public int getBaseline() {
        if (mLayout == null || mLayout.getLineCount() == 0) {
            return -1;
        }
        return getExtendedPaddingTop() + mLayout.getLineBaseline(0);
    }
}
