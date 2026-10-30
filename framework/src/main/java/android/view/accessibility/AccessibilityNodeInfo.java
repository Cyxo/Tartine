package android.view.accessibility;

import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Size;
import android.view.View;
import java.time.Duration;
import java.util.List;
import java.util.Map;

    public class AccessibilityNodeInfo implements Parcelable {

    public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
    public static final String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";
    public static final String ACTION_ARGUMENT_DIRECTION_INT = "android.view.accessibility.action.ARGUMENT_DIRECTION_INT";
    public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
    public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
    public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_X = "ACTION_ARGUMENT_MOVE_WINDOW_X";
    public static final String ACTION_ARGUMENT_MOVE_WINDOW_Y = "ACTION_ARGUMENT_MOVE_WINDOW_Y";
    public static final String ACTION_ARGUMENT_PRESS_AND_HOLD_DURATION_MILLIS_INT = "android.view.accessibility.action.ARGUMENT_PRESS_AND_HOLD_DURATION_MILLIS_INT";
    public static final String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";
    public static final String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";
    public static final String ACTION_ARGUMENT_SCROLL_AMOUNT_FLOAT = "android.view.accessibility.action.ARGUMENT_SCROLL_AMOUNT_FLOAT";
    public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
    public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
    public static final String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
    public static final int ACTION_CLEAR_FOCUS = 2;
    public static final int ACTION_CLEAR_SELECTION = 8;
    public static final int ACTION_CLICK = 16;
    public static final int ACTION_COLLAPSE = 524288;
    public static final int ACTION_COPY = 16384;
    public static final int ACTION_CUT = 65536;
    public static final int ACTION_DISMISS = 1048576;
    public static final int ACTION_EXPAND = 262144;
    public static final int ACTION_FOCUS = 1;
    public static final int ACTION_LONG_CLICK = 32;
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
    public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
    public static final int ACTION_PASTE = 32768;
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    public static final int ACTION_SELECT = 4;
    public static final int ACTION_SET_SELECTION = 131072;
    public static final int ACTION_SET_TEXT = 2097152;
    public static final int CHECKED_STATE_FALSE = 0;
    public static final int CHECKED_STATE_PARTIAL = 2;
    public static final int CHECKED_STATE_TRUE = 1;
    public static Parcelable.Creator CREATOR;
    public static final int EXPANDED_STATE_COLLAPSED = 1;
    public static final int EXPANDED_STATE_FULL = 3;
    public static final int EXPANDED_STATE_PARTIAL = 2;
    public static final int EXPANDED_STATE_UNDEFINED = 0;
    public static final String EXTRA_DATA_RENDERING_INFO_KEY = "android.view.accessibility.extra.DATA_RENDERING_INFO_KEY";
    public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH";
    public static final int EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH = 20000;
    public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX";
    public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_IN_WINDOW_KEY = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_IN_WINDOW_KEY";
    public static final String EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY = "android.view.accessibility.extra.DATA_TEXT_CHARACTER_LOCATION_KEY";
    public static final int FLAG_PREFETCH_ANCESTORS = 1;
    public static final int FLAG_PREFETCH_DESCENDANTS_BREADTH_FIRST = 16;
    public static final int FLAG_PREFETCH_DESCENDANTS_DEPTH_FIRST = 8;
    public static final int FLAG_PREFETCH_DESCENDANTS_HYBRID = 4;
    public static final int FLAG_PREFETCH_SIBLINGS = 2;
    public static final int FLAG_PREFETCH_UNINTERRUPTIBLE = 32;
    public static final int FOCUS_ACCESSIBILITY = 2;
    public static final int FOCUS_INPUT = 1;
    public static final int MAX_NUMBER_OF_PREFETCHED_NODES = 50;
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;

    public AccessibilityNodeInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;-><init>()V");
    }

    public AccessibilityNodeInfo(AccessibilityNodeInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;-><init>(Landroid/view/accessibility/AccessibilityNodeInfo;)V");
    }

    public AccessibilityNodeInfo(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;-><init>(Landroid/view/View;)V");
    }

    public AccessibilityNodeInfo(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;-><init>(Landroid/view/View;I)V");
    }

    public void addAction(AccessibilityNodeInfo.AccessibilityAction arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->addAction(Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;)V");
    }

    public void addAction(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->addAction(I)V");
    }

    public void addChild(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->addChild(Landroid/view/View;)V");
    }

    public void addChild(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->addChild(Landroid/view/View;I)V");
    }

    public void addLabeledBy(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->addLabeledBy(Landroid/view/View;)V");
    }

    public void addLabeledBy(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->addLabeledBy(Landroid/view/View;I)V");
    }

    public boolean canOpenPopup() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->canOpenPopup()Z");
    }

    public int describeContents() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->describeContents()I");
    }

    public boolean equals(Object arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->equals(Ljava/lang/Object;)Z");
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByText(Ljava/lang/String;)Ljava/util/List;");
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->findAccessibilityNodeInfosByViewId(Ljava/lang/String;)Ljava/util/List;");
    }

    public AccessibilityNodeInfo findFocus(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->findFocus(I)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public AccessibilityNodeInfo focusSearch(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->focusSearch(I)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public List<AccessibilityNodeInfo.AccessibilityAction> getActionList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getActionList()Ljava/util/List;");
    }

    public int getActions() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getActions()I");
    }

    public List<String> getAvailableExtraData() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getAvailableExtraData()Ljava/util/List;");
    }

    public void getBoundsInParent(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getBoundsInParent(Landroid/graphics/Rect;)V");
    }

    public void getBoundsInScreen(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getBoundsInScreen(Landroid/graphics/Rect;)V");
    }

    public void getBoundsInWindow(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getBoundsInWindow(Landroid/graphics/Rect;)V");
    }

    public int getChecked() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getChecked()I");
    }

    public AccessibilityNodeInfo getChild(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(I)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public AccessibilityNodeInfo getChild(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getChild(II)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public int getChildCount() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getChildCount()I");
    }

    public CharSequence getClassName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getClassName()Ljava/lang/CharSequence;");
    }

    public AccessibilityNodeInfo.CollectionInfo getCollectionInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getCollectionInfo()Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;");
    }

    public AccessibilityNodeInfo.CollectionItemInfo getCollectionItemInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getCollectionItemInfo()Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;");
    }

    public CharSequence getContainerTitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getContainerTitle()Ljava/lang/CharSequence;");
    }

    public CharSequence getContentDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getContentDescription()Ljava/lang/CharSequence;");
    }

    public int getDrawingOrder() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getDrawingOrder()I");
    }

    public CharSequence getError() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getError()Ljava/lang/CharSequence;");
    }

    public int getExpandedState() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getExpandedState()I");
    }

    public AccessibilityNodeInfo.ExtraRenderingInfo getExtraRenderingInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getExtraRenderingInfo()Landroid/view/accessibility/AccessibilityNodeInfo$ExtraRenderingInfo;");
    }

    public Bundle getExtras() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getExtras()Landroid/os/Bundle;");
    }

    public CharSequence getHintText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getHintText()Ljava/lang/CharSequence;");
    }

    public int getInputType() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getInputType()I");
    }

    public AccessibilityNodeInfo getLabelFor() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getLabelFor()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public AccessibilityNodeInfo getLabeledBy() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getLabeledBy()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public List<AccessibilityNodeInfo> getLabeledByList() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getLabeledByList()Ljava/util/List;");
    }

    public int getLiveRegion() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getLiveRegion()I");
    }

    public int getMaxTextLength() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getMaxTextLength()I");
    }

    public Duration getMinDurationBetweenContentChanges() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getMinDurationBetweenContentChanges()Ljava/time/Duration;");
    }

    public int getMovementGranularities() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getMovementGranularities()I");
    }

    public CharSequence getPackageName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getPackageName()Ljava/lang/CharSequence;");
    }

    public CharSequence getPaneTitle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getPaneTitle()Ljava/lang/CharSequence;");
    }

    public AccessibilityNodeInfo getParent() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getParent()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public AccessibilityNodeInfo getParent(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getParent(I)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public AccessibilityNodeInfo.RangeInfo getRangeInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getRangeInfo()Landroid/view/accessibility/AccessibilityNodeInfo$RangeInfo;");
    }

    public CharSequence getStateDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getStateDescription()Ljava/lang/CharSequence;");
    }

    public CharSequence getSupplementalDescription() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getSupplementalDescription()Ljava/lang/CharSequence;");
    }

    public CharSequence getText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getText()Ljava/lang/CharSequence;");
    }

    public int getTextSelectionEnd() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getTextSelectionEnd()I");
    }

    public int getTextSelectionStart() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getTextSelectionStart()I");
    }

    public CharSequence getTooltipText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getTooltipText()Ljava/lang/CharSequence;");
    }

    public AccessibilityNodeInfo.TouchDelegateInfo getTouchDelegateInfo() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getTouchDelegateInfo()Landroid/view/accessibility/AccessibilityNodeInfo$TouchDelegateInfo;");
    }

    public AccessibilityNodeInfo getTraversalAfter() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getTraversalAfter()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public AccessibilityNodeInfo getTraversalBefore() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getTraversalBefore()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public String getUniqueId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getUniqueId()Ljava/lang/String;");
    }

    public String getViewIdResourceName() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getViewIdResourceName()Ljava/lang/String;");
    }

    public AccessibilityWindowInfo getWindow() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getWindow()Landroid/view/accessibility/AccessibilityWindowInfo;");
    }

    public int getWindowId() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->getWindowId()I");
    }

    public boolean hasRequestInitialAccessibilityFocus() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->hasRequestInitialAccessibilityFocus()Z");
    }

    public int hashCode() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->hashCode()I");
    }

    public boolean isAccessibilityDataSensitive() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isAccessibilityDataSensitive()Z");
    }

    public boolean isAccessibilityFocused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isAccessibilityFocused()Z");
    }

    public boolean isCheckable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isCheckable()Z");
    }

    public boolean isChecked() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isChecked()Z");
    }

    public boolean isClickable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isClickable()Z");
    }

    public boolean isContentInvalid() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isContentInvalid()Z");
    }

    public boolean isContextClickable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isContextClickable()Z");
    }

    public boolean isDismissable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isDismissable()Z");
    }

    public boolean isEditable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isEditable()Z");
    }

    public boolean isEnabled() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isEnabled()Z");
    }

    public boolean isFieldRequired() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isFieldRequired()Z");
    }

    public boolean isFocusable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isFocusable()Z");
    }

    public boolean isFocused() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isFocused()Z");
    }

    public boolean isGranularScrollingSupported() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isGranularScrollingSupported()Z");
    }

    public boolean isHeading() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isHeading()Z");
    }

    public boolean isImportantForAccessibility() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isImportantForAccessibility()Z");
    }

    public boolean isLongClickable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isLongClickable()Z");
    }

    public boolean isMultiLine() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isMultiLine()Z");
    }

    public boolean isPassword() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isPassword()Z");
    }

    public boolean isScreenReaderFocusable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isScreenReaderFocusable()Z");
    }

    public boolean isScrollable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isScrollable()Z");
    }

    public boolean isSelected() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isSelected()Z");
    }

    public boolean isShowingHintText() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isShowingHintText()Z");
    }

    public boolean isTextEntryKey() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isTextEntryKey()Z");
    }

    public boolean isTextSelectable() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isTextSelectable()Z");
    }

    public boolean isVisibleToUser() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->isVisibleToUser()Z");
    }

    public static AccessibilityNodeInfo obtain() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->obtain()Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public static AccessibilityNodeInfo obtain(AccessibilityNodeInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->obtain(Landroid/view/accessibility/AccessibilityNodeInfo;)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public static AccessibilityNodeInfo obtain(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->obtain(Landroid/view/View;)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public static AccessibilityNodeInfo obtain(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->obtain(Landroid/view/View;I)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public boolean performAction(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(I)Z");
    }

    public boolean performAction(int arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->performAction(ILandroid/os/Bundle;)Z");
    }

    public void recycle() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->recycle()V");
    }

    public boolean refresh() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->refresh()Z");
    }

    public boolean refreshWithExtraData(String arg0, Bundle arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->refreshWithExtraData(Ljava/lang/String;Landroid/os/Bundle;)Z");
    }

    public boolean removeAction(AccessibilityNodeInfo.AccessibilityAction arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->removeAction(Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;)Z");
    }

    public void removeAction(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->removeAction(I)V");
    }

    public boolean removeChild(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->removeChild(Landroid/view/View;)Z");
    }

    public boolean removeChild(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->removeChild(Landroid/view/View;I)Z");
    }

    public boolean removeLabeledBy(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->removeLabeledBy(Landroid/view/View;)Z");
    }

    public boolean removeLabeledBy(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->removeLabeledBy(Landroid/view/View;I)Z");
    }

    public void setAccessibilityDataSensitive(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setAccessibilityDataSensitive(Z)V");
    }

    public void setAccessibilityFocused(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setAccessibilityFocused(Z)V");
    }

    public void setAvailableExtraData(List<String> arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setAvailableExtraData(Ljava/util/List;)V");
    }

    public void setBoundsInParent(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setBoundsInParent(Landroid/graphics/Rect;)V");
    }

    public void setBoundsInScreen(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setBoundsInScreen(Landroid/graphics/Rect;)V");
    }

    public void setBoundsInWindow(Rect arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setBoundsInWindow(Landroid/graphics/Rect;)V");
    }

    public void setCanOpenPopup(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setCanOpenPopup(Z)V");
    }

    public void setCheckable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setCheckable(Z)V");
    }

    public void setChecked(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setChecked(Z)V");
    }

    public void setChecked(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setChecked(I)V");
    }

    public void setClassName(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setClassName(Ljava/lang/CharSequence;)V");
    }

    public void setClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setClickable(Z)V");
    }

    public void setCollectionInfo(AccessibilityNodeInfo.CollectionInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setCollectionInfo(Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;)V");
    }

    public void setCollectionItemInfo(AccessibilityNodeInfo.CollectionItemInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setCollectionItemInfo(Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;)V");
    }

    public void setContainerTitle(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setContainerTitle(Ljava/lang/CharSequence;)V");
    }

    public void setContentDescription(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setContentDescription(Ljava/lang/CharSequence;)V");
    }

    public void setContentInvalid(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setContentInvalid(Z)V");
    }

    public void setContextClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setContextClickable(Z)V");
    }

    public void setDismissable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setDismissable(Z)V");
    }

    public void setDrawingOrder(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setDrawingOrder(I)V");
    }

    public void setEditable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setEditable(Z)V");
    }

    public void setEnabled(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setEnabled(Z)V");
    }

    public void setError(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setError(Ljava/lang/CharSequence;)V");
    }

    public void setExpandedState(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setExpandedState(I)V");
    }

    public void setFieldRequired(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setFieldRequired(Z)V");
    }

    public void setFocusable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setFocusable(Z)V");
    }

    public void setFocused(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setFocused(Z)V");
    }

    public void setGranularScrollingSupported(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setGranularScrollingSupported(Z)V");
    }

    public void setHeading(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setHeading(Z)V");
    }

    public void setHintText(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setHintText(Ljava/lang/CharSequence;)V");
    }

    public void setImportantForAccessibility(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setImportantForAccessibility(Z)V");
    }

    public void setInputType(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setInputType(I)V");
    }

    public void setLabelFor(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setLabelFor(Landroid/view/View;)V");
    }

    public void setLabelFor(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setLabelFor(Landroid/view/View;I)V");
    }

    public void setLabeledBy(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setLabeledBy(Landroid/view/View;)V");
    }

    public void setLabeledBy(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setLabeledBy(Landroid/view/View;I)V");
    }

    public void setLiveRegion(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setLiveRegion(I)V");
    }

    public void setLongClickable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setLongClickable(Z)V");
    }

    public void setMaxTextLength(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setMaxTextLength(I)V");
    }

    public void setMinDurationBetweenContentChanges(Duration arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setMinDurationBetweenContentChanges(Ljava/time/Duration;)V");
    }

    public void setMovementGranularities(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setMovementGranularities(I)V");
    }

    public void setMultiLine(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setMultiLine(Z)V");
    }

    public void setPackageName(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setPackageName(Ljava/lang/CharSequence;)V");
    }

    public void setPaneTitle(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setPaneTitle(Ljava/lang/CharSequence;)V");
    }

    public void setParent(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setParent(Landroid/view/View;)V");
    }

    public void setParent(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setParent(Landroid/view/View;I)V");
    }

    public void setPassword(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setPassword(Z)V");
    }

    public void setQueryFromAppProcessEnabled(View arg0, boolean arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setQueryFromAppProcessEnabled(Landroid/view/View;Z)V");
    }

    public void setRangeInfo(AccessibilityNodeInfo.RangeInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setRangeInfo(Landroid/view/accessibility/AccessibilityNodeInfo$RangeInfo;)V");
    }

    public void setRequestInitialAccessibilityFocus(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setRequestInitialAccessibilityFocus(Z)V");
    }

    public void setScreenReaderFocusable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setScreenReaderFocusable(Z)V");
    }

    public void setScrollable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setScrollable(Z)V");
    }

    public void setSelected(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setSelected(Z)V");
    }

    public void setShowingHintText(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setShowingHintText(Z)V");
    }

    public void setSource(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setSource(Landroid/view/View;)V");
    }

    public void setSource(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setSource(Landroid/view/View;I)V");
    }

    public void setStateDescription(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setStateDescription(Ljava/lang/CharSequence;)V");
    }

    public void setSupplementalDescription(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setSupplementalDescription(Ljava/lang/CharSequence;)V");
    }

    public void setText(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setText(Ljava/lang/CharSequence;)V");
    }

    public void setTextEntryKey(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setTextEntryKey(Z)V");
    }

    public void setTextSelectable(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setTextSelectable(Z)V");
    }

    public void setTextSelection(int arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setTextSelection(II)V");
    }

    public void setTooltipText(CharSequence arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setTooltipText(Ljava/lang/CharSequence;)V");
    }

    public void setTouchDelegateInfo(AccessibilityNodeInfo.TouchDelegateInfo arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setTouchDelegateInfo(Landroid/view/accessibility/AccessibilityNodeInfo$TouchDelegateInfo;)V");
    }

    public void setTraversalAfter(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setTraversalAfter(Landroid/view/View;)V");
    }

    public void setTraversalAfter(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setTraversalAfter(Landroid/view/View;I)V");
    }

    public void setTraversalBefore(View arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setTraversalBefore(Landroid/view/View;)V");
    }

    public void setTraversalBefore(View arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setTraversalBefore(Landroid/view/View;I)V");
    }

    public void setUniqueId(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setUniqueId(Ljava/lang/String;)V");
    }

    public void setViewIdResourceName(String arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setViewIdResourceName(Ljava/lang/String;)V");
    }

    public void setVisibleToUser(boolean arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->setVisibleToUser(Z)V");
    }

    public String toString() {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->toString()Ljava/lang/String;");
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo;->writeToParcel(Landroid/os/Parcel;I)V");
    }
        public static class AccessibilityAction implements Parcelable {

        public static AccessibilityNodeInfo.AccessibilityAction ACTION_ACCESSIBILITY_FOCUS;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_CLEAR_FOCUS;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_CLEAR_SELECTION;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_CLICK;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_COLLAPSE;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_CONTEXT_CLICK;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_COPY;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_CUT;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_DISMISS;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_DRAG_CANCEL;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_DRAG_DROP;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_DRAG_START;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_EXPAND;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_FOCUS;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_HIDE_TOOLTIP;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_IME_ENTER;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_LONG_CLICK;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_MOVE_WINDOW;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_NEXT_HTML_ELEMENT;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_PAGE_DOWN;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_PAGE_LEFT;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_PAGE_RIGHT;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_PAGE_UP;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_PASTE;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_PRESS_AND_HOLD;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_PREVIOUS_HTML_ELEMENT;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_BACKWARD;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_DOWN;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_FORWARD;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_IN_DIRECTION;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_LEFT;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_RIGHT;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_TO_POSITION;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SCROLL_UP;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SELECT;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SET_PROGRESS;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SET_SELECTION;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SET_TEXT;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SHOW_ON_SCREEN;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SHOW_TEXT_SUGGESTIONS;
        public static AccessibilityNodeInfo.AccessibilityAction ACTION_SHOW_TOOLTIP;
        public static Parcelable.Creator CREATOR;

        public AccessibilityAction(int arg0, CharSequence arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;-><init>(ILjava/lang/CharSequence;)V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;->describeContents()I");
        }

        public boolean equals(Object arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;->equals(Ljava/lang/Object;)Z");
        }

        public int getId() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;->getId()I");
        }

        public CharSequence getLabel() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;->getLabel()Ljava/lang/CharSequence;");
        }

        public int hashCode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;->hashCode()I");
        }

        public String toString() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;->toString()Ljava/lang/String;");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$AccessibilityAction;->writeToParcel(Landroid/os/Parcel;I)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public AccessibilityAction() {
            }
        }
        public static class CollectionInfo {

        public static final int SELECTION_MODE_MULTIPLE = 2;
        public static final int SELECTION_MODE_NONE = 0;
        public static final int SELECTION_MODE_SINGLE = 1;
        public static final int UNDEFINED = -1;

        public CollectionInfo(int arg0, int arg1, boolean arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;-><init>(IIZ)V");
        }

        public CollectionInfo(int arg0, int arg1, boolean arg2, int arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;-><init>(IIZI)V");
        }

        public int getColumnCount() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;->getColumnCount()I");
        }

        public int getImportantForAccessibilityItemCount() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;->getImportantForAccessibilityItemCount()I");
        }

        public int getItemCount() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;->getItemCount()I");
        }

        public int getRowCount() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;->getRowCount()I");
        }

        public int getSelectionMode() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;->getSelectionMode()I");
        }

        public boolean isHierarchical() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;->isHierarchical()Z");
        }

        public static AccessibilityNodeInfo.CollectionInfo obtain(int arg0, int arg1, boolean arg2) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;->obtain(IIZ)Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;");
        }

        public static AccessibilityNodeInfo.CollectionInfo obtain(int arg0, int arg1, boolean arg2, int arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;->obtain(IIZI)Landroid/view/accessibility/AccessibilityNodeInfo$CollectionInfo;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public CollectionInfo() {
            }
        }
        public static class CollectionItemInfo {

        public CollectionItemInfo(int arg0, int arg1, int arg2, int arg3, boolean arg4) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;-><init>(IIIIZ)V");
        }

        public CollectionItemInfo(int arg0, int arg1, int arg2, int arg3, boolean arg4, boolean arg5) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;-><init>(IIIIZZ)V");
        }

        public int getColumnIndex() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->getColumnIndex()I");
        }

        public int getColumnSpan() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->getColumnSpan()I");
        }

        public String getColumnTitle() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->getColumnTitle()Ljava/lang/String;");
        }

        public int getRowIndex() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->getRowIndex()I");
        }

        public int getRowSpan() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->getRowSpan()I");
        }

        public String getRowTitle() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->getRowTitle()Ljava/lang/String;");
        }

        public boolean isHeading() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->isHeading()Z");
        }

        public boolean isSelected() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->isSelected()Z");
        }

        public static AccessibilityNodeInfo.CollectionItemInfo obtain(int arg0, int arg1, int arg2, int arg3, boolean arg4) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->obtain(IIIIZ)Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;");
        }

        public static AccessibilityNodeInfo.CollectionItemInfo obtain(int arg0, int arg1, int arg2, int arg3, boolean arg4, boolean arg5) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;->obtain(IIIIZZ)Landroid/view/accessibility/AccessibilityNodeInfo$CollectionItemInfo;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public CollectionItemInfo() {
            }
        }
        public static class ExtraRenderingInfo {

        public Size getLayoutSize() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$ExtraRenderingInfo;->getLayoutSize()Landroid/util/Size;");
        }

        public float getTextSizeInPx() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$ExtraRenderingInfo;->getTextSizeInPx()F");
        }

        public int getTextSizeUnit() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$ExtraRenderingInfo;->getTextSizeUnit()I");
        }
        }
        public static class RangeInfo {

        public static AccessibilityNodeInfo.RangeInfo INDETERMINATE;
        public static final int RANGE_TYPE_FLOAT = 1;
        public static final int RANGE_TYPE_INDETERMINATE = 3;
        public static final int RANGE_TYPE_INT = 0;
        public static final int RANGE_TYPE_PERCENT = 2;

        public RangeInfo(int arg0, float arg1, float arg2, float arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$RangeInfo;-><init>(IFFF)V");
        }

        public float getCurrent() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$RangeInfo;->getCurrent()F");
        }

        public float getMax() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$RangeInfo;->getMax()F");
        }

        public float getMin() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$RangeInfo;->getMin()F");
        }

        public int getType() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$RangeInfo;->getType()I");
        }

        public static AccessibilityNodeInfo.RangeInfo obtain(int arg0, float arg1, float arg2, float arg3) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$RangeInfo;->obtain(IFFF)Landroid/view/accessibility/AccessibilityNodeInfo$RangeInfo;");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public RangeInfo() {
            }
        }
        public static class TouchDelegateInfo implements Parcelable {

        public static Parcelable.Creator CREATOR;

        public TouchDelegateInfo(Map arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$TouchDelegateInfo;-><init>(Ljava/util/Map;)V");
        }

        public int describeContents() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$TouchDelegateInfo;->describeContents()I");
        }

        public Region getRegionAt(int arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$TouchDelegateInfo;->getRegionAt(I)Landroid/graphics/Region;");
        }

        public int getRegionCount() {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$TouchDelegateInfo;->getRegionCount()I");
        }

        public AccessibilityNodeInfo getTargetForRegion(Region arg0) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$TouchDelegateInfo;->getTargetForRegion(Landroid/graphics/Region;)Landroid/view/accessibility/AccessibilityNodeInfo;");
        }

        public void writeToParcel(Parcel arg0, int arg1) {
            throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeInfo$TouchDelegateInfo;->writeToParcel(Landroid/os/Parcel;I)V");
        }

            /** Scratch mirror constructor (SkeletonGen). */
            public TouchDelegateInfo() {
            }
        }
    }
