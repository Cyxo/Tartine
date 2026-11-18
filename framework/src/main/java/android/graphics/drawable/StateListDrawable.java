package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.StateSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

/**
 * AOSP {@code StateListDrawable}: an ordered selector of
 * {@code <item android:state_...="true" android:drawable="@drawable/...">}
 * entries. The first matching state set wins; a bare item is the fallback.
 */
public class StateListDrawable extends Drawable implements Drawable.Callback {

    private static final int ATTR_DRAWABLE = 0x01010199;

    private static final class StateDrawable {
        private final int[] stateSet;
        private final Drawable drawable;

        private StateDrawable(int[] stateSet, Drawable drawable) {
            this.stateSet = stateSet;
            this.drawable = drawable;
        }
    }

    private final List<StateDrawable> mEntries = new ArrayList<>();
    private Drawable mCurrent;
    private int mCurrentIndex = -1;
    private ColorFilter mColorFilter;
    private int mAlpha = 255;

    public StateListDrawable() {
    }

    public void addState(int[] stateSet, Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setCallback(this);
        drawable.setLayoutDirection(getLayoutDirection());
        drawable.setVisible(isVisible(), false);
        mEntries.add(new StateDrawable(stateSet == null ? StateSet.WILD_CARD
                : stateSet, drawable));
        if (mCurrent == null) {
            selectDrawable(0);
        }
    }

    public int getStateCount() {
        return mEntries.size();
    }

    public Drawable getStateDrawable(int index) {
        return mEntries.get(index).drawable;
    }

    public int getStateDrawableIndex(int[] stateSet) {
        for (int i = 0; i < mEntries.size(); i++) {
            if (Arrays.equals(mEntries.get(i).stateSet, stateSet)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mCurrent != null) {
            mCurrent.draw(canvas);
        }
    }

    @Override
    public int getAlpha() {
        return mCurrent == null ? mAlpha : mCurrent.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        if (mCurrent != null) {
            mCurrent.setAlpha(alpha);
        }
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mColorFilter = colorFilter;
        if (mCurrent != null) {
            mCurrent.setColorFilter(colorFilter);
        }
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return mCurrent == null ? PixelFormat.TRANSLUCENT
                : mCurrent.getOpacity();
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public void setTintList(ColorStateList tint) {
        super.setTintList(tint);
        for (StateDrawable entry : mEntries) {
            entry.drawable.setTintList(tint);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        if (mCurrent != null) {
            mCurrent.setBounds(bounds);
        }
    }

    @Override
    public boolean onStateChange(int[] stateSet) {
        int index = indexOfState(stateSet);
        if (index >= 0 && selectDrawable(index)) {
            return true;
        }
        return mCurrent != null && mCurrent.setState(stateSet);
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (mCurrent != null) {
            mCurrent.setVisible(visible, restart);
        }
        return changed;
    }

    @Override
    public void jumpToCurrentState() {
        if (mCurrent != null) {
            mCurrent.jumpToCurrentState();
        }
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        invalidateSelf();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }

    @Override
    public void inflate(Resources resources, XmlPullParser parser,
            AttributeSet attrs, Resources.Theme theme) {
        super.inflate(resources, parser, attrs, theme);
        final int rootDepth = parser.getDepth();
        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.END_TAG
                    && parser.getDepth() == rootDepth) {
                break;
            }
            if (type != XmlPullParser.START_TAG) {
                continue;
            }
            if (!"item".equals(parser.getName())) {
                continue;
            }
            AttributeSet itemAttrs = parser instanceof AttributeSet
                    ? (AttributeSet) parser : null;
            int drawableId = 0;
            int[] stateSet = new int[itemAttrs == null ? 0
                    : itemAttrs.getAttributeCount()];
            int stateCount = 0;
            if (itemAttrs != null) {
                for (int i = 0; i < itemAttrs.getAttributeCount(); i++) {
                    int attr = itemAttrs.getAttributeNameResource(i);
                    if (attr == ATTR_DRAWABLE) {
                        drawableId = itemAttrs.getAttributeResourceValue(i, 0);
                    } else if (isStateAttribute(attr)) {
                        stateSet[stateCount++] = itemAttrs
                                .getAttributeBooleanValue(i, false)
                                ? attr : -attr;
                    }
                }
            }
            if (drawableId != 0) {
                Drawable drawable = resources.getDrawable(drawableId, theme);
                addState(StateSet.trimStateSet(stateSet, stateCount),
                        drawable);
            }
        }
    }

    private static boolean isStateAttribute(int attr) {
        return attr == StateSet.STATE_FOCUSED
                || attr == StateSet.STATE_WINDOW_FOCUSED
                || attr == StateSet.STATE_ENABLED
                || attr == StateSet.STATE_CHECKABLE
                || attr == StateSet.STATE_CHECKED
                || attr == StateSet.STATE_SELECTED
                || attr == StateSet.STATE_ACTIVATED
                || attr == StateSet.STATE_ACCELERATED
                || attr == StateSet.STATE_HOVERED
                || attr == StateSet.STATE_DRAG_CAN_ACCEPT
                || attr == StateSet.STATE_DRAG_HOVERED
                || attr == StateSet.STATE_PRESSED;
    }

    private int indexOfState(int[] stateSet) {
        for (int i = 0; i < mEntries.size(); i++) {
            if (StateSet.stateSetMatches(mEntries.get(i).stateSet, stateSet)) {
                return i;
            }
        }
        return -1;
    }

    private boolean selectDrawable(int index) {
        if (index == mCurrentIndex) {
            return false;
        }
        Drawable old = mCurrent;
        mCurrentIndex = index;
        mCurrent = index < 0 ? null : mEntries.get(index).drawable;
        if (old != null) {
            old.setVisible(false, false);
        }
        if (mCurrent != null) {
            mCurrent.setVisible(isVisible(), true);
            mCurrent.setBounds(getBounds());
            mCurrent.setAlpha(mAlpha);
            if (mColorFilter != null) {
                mCurrent.setColorFilter(mColorFilter);
            }
        }
        invalidateSelf();
        return true;
    }
}
