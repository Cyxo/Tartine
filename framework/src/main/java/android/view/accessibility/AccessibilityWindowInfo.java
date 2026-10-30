package android.view.accessibility;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;

    public class AccessibilityWindowInfo implements Parcelable {

    public static Parcelable.Creator CREATOR;
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_MAGNIFICATION_OVERLAY = 6;
    public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
    public static final int TYPE_SYSTEM = 3;
    public static final int TYPE_WINDOW_CONTROL = 7;

    public AccessibilityWindowInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;-><init>()V");
    }

    public AccessibilityWindowInfo(AccessibilityWindowInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;-><init>(Landroid/view/accessibility/AccessibilityWindowInfo;)V");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->equals(Ljava/lang/Object;)Z");
    }

    public AccessibilityNodeInfo getAnchor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getAnchor()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public void getBoundsInScreen(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getBoundsInScreen(Landroid/graphics/Rect;)V");
    }

    public AccessibilityWindowInfo getChild(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getChild(I)Landroid/view/accessibility/AccessibilityWindowInfo;");
    }

    public int getChildCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getChildCount()I");
    }

    public int getDisplayId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getDisplayId()I");
    }

    public int getId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getId()I");
    }

    public int getLayer() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getLayer()I");
    }

    public LocaleList getLocales() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getLocales()Landroid/os/LocaleList;");
    }

    public AccessibilityWindowInfo getParent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getParent()Landroid/view/accessibility/AccessibilityWindowInfo;");
    }

    public void getRegionInScreen(Region arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getRegionInScreen(Landroid/graphics/Region;)V");
    }

    public AccessibilityNodeInfo getRoot() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getRoot()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public AccessibilityNodeInfo getRoot(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getRoot(I)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public CharSequence getTitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getTitle()Ljava/lang/CharSequence;");
    }

    public long getTransitionTimeMillis() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getTransitionTimeMillis()J");
    }

    public int getType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->getType()I");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->hashCode()I");
    }

    public boolean isAccessibilityFocused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->isAccessibilityFocused()Z");
    }

    public boolean isActive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->isActive()Z");
    }

    public boolean isFocused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->isFocused()Z");
    }

    public boolean isInPictureInPictureMode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->isInPictureInPictureMode()Z");
    }

    public static AccessibilityWindowInfo obtain() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->obtain()Landroid/view/accessibility/AccessibilityWindowInfo;");
    }

    public static AccessibilityWindowInfo obtain(AccessibilityWindowInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->obtain(Landroid/view/accessibility/AccessibilityWindowInfo;)Landroid/view/accessibility/AccessibilityWindowInfo;");
    }

    public void recycle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->recycle()V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityWindowInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
    }
