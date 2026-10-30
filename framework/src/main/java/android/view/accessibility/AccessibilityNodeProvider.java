package android.view.accessibility;

import android.os.Bundle;
import java.util.List;

    public class AccessibilityNodeProvider {

    public static final int HOST_VIEW_ID = -1;

    public AccessibilityNodeProvider() {
        // AOSP frameworks-base core/java/android/view/accessibility/
        // AccessibilityNodeProvider.java: the base class declares no
        // explicit constructor and carries no fields — the implicit
        // <init> body is empty (subclasses override
        // createAccessibilityNodeInfo).
    }

    public void addExtraDataToAccessibilityNodeInfo(int arg0, AccessibilityNodeInfo arg1, String arg2, Bundle arg3) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeProvider;->addExtraDataToAccessibilityNodeInfo(ILandroid/view/accessibility/AccessibilityNodeInfo;Ljava/lang/String;Landroid/os/Bundle;)V");
    }

    public AccessibilityNodeInfo createAccessibilityNodeInfo(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeProvider;->createAccessibilityNodeInfo(I)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String arg0, int arg1) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeProvider;->findAccessibilityNodeInfosByText(Ljava/lang/String;I)Ljava/util/List;");
    }

    public AccessibilityNodeInfo findFocus(int arg0) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeProvider;->findFocus(I)Landroid/view/accessibility/AccessibilityNodeInfo;");
    }

    public boolean performAction(int arg0, int arg1, Bundle arg2) {
        throw new UnsupportedOperationException("Tartine: not implemented: Landroid/view/accessibility/AccessibilityNodeProvider;->performAction(IILandroid/os/Bundle;)Z");
    }
    }
