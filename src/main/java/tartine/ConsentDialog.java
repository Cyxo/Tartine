package tartine;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import tartine.fw.PermissionPrompt;
import tartine.graphics.WindowIcon;

/**
 * Desktop consent dialog behind the {@link PermissionPrompt} seam. Unlike the
 * ownerless {@code JOptionPane} it replaces, the prompt is its own top-level
 * {@link JDialog}: it carries the guest app's icon and raises itself to the
 * front, so the request reads as coming from the app rather than a bare frame.
 *
 * <p>The platform attention request is a single flash ({@code FlashWindow} on
 * Windows), not a repeating one, so a timer repeats it: a short burst on open
 * draws the eye even when the desktop already focused the dialog, and the
 * flash keeps going while the dialog is not the focused window, so the
 * request cannot be missed when the app spawned it in the background, behind
 * a fullscreen window, or on another virtual desktop. Platforms without
 * {@link Taskbar.Feature#USER_ATTENTION_WINDOW}, and headless hosts, skip the
 * flash but still show the dialog.
 */
final class ConsentDialog {

    private static final Logger log =
            Logger.getLogger(ConsentDialog.class.getName());

    /** Interval between attention flashes while the dialog is unfocused. */
    private static final int FLASH_PERIOD_MILLIS = 750;

    /** Flashes shown on open even when the dialog is already focused. */
    private static final int MIN_FLASHES = 3;

    private ConsentDialog() {
    }

    /**
     * Converts the resolved window icons into the AWT images the dialog
     * hands to {@link java.awt.Window#setIconImages}. Kept as a pure helper
     * so the RGBA-to-ARGB bridge is unit-testable without a display.
     */
    static List<Image> toAwtImages(List<WindowIcon> icons) {
        List<Image> images = new ArrayList<>(icons.size());
        for (WindowIcon icon : icons) {
            images.add(toBufferedImage(icon));
        }
        return images;
    }

    /**
     * Copies one straight-alpha, top-down RGBA {@link WindowIcon} into a
     * {@code TYPE_INT_ARGB} image, rearranging the channels to AWT's ARGB
     * order.
     */
    static BufferedImage toBufferedImage(WindowIcon icon) {
        byte[] rgba = icon.rgba();
        int[] argb = new int[icon.width() * icon.height()];
        for (int index = 0; index < argb.length; index++) {
            int base = index * 4;
            int alpha = rgba[base + 3] & 0xFF;
            int red = rgba[base] & 0xFF;
            int green = rgba[base + 1] & 0xFF;
            int blue = rgba[base + 2] & 0xFF;
            argb[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
        BufferedImage image = new BufferedImage(icon.width(), icon.height(),
                BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, icon.width(), icon.height(), argb, 0, icon.width());
        return image;
    }

    /**
     * Shows the modal prompt on the calling thread (the AWT event dispatch
     * thread) and returns the user's choice; closing the window denies.
     */
    static PermissionPrompt.Decision ask(String packageName, String label,
            List<Image> icons) {
        JDialog dialog = new JDialog((Frame) null, "Tartine permission", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setIconImages(icons);
        AtomicReference<PermissionPrompt.Decision> answer =
                new AtomicReference<>(PermissionPrompt.Decision.DENIED);

        JLabel message = new JLabel(
                packageName + " wants to access " + label + ".");
        message.setBorder(new EmptyBorder(16, 16, 8, 16));

        JButton allow = new JButton("Allow");
        allow.addActionListener(event -> {
            answer.set(PermissionPrompt.Decision.GRANTED);
            dialog.dispose();
        });
        JButton deny = new JButton("Deny");
        deny.addActionListener(event -> {
            answer.set(PermissionPrompt.Decision.DENIED);
            dialog.dispose();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttons.add(deny);
        buttons.add(allow);

        JPanel content = new JPanel(new BorderLayout());
        content.add(message, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        dialog.setContentPane(content);
        dialog.getRootPane().setDefaultButton(deny);
        dialog.setResizable(false);

        startAttentionFlash(dialog);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        return answer.get();
    }

    /**
     * Flashes the dialog's taskbar button from the moment it opens: a short
     * burst first, then repeated while the dialog is not the focused window,
     * so the request is noticeable without flashing forever on a dialog the
     * user is already reading.
     */
    private static void startAttentionFlash(JDialog dialog) {
        Taskbar taskbar = attentionTaskbar();
        if (taskbar == null) {
            return;
        }
        AtomicInteger emitted = new AtomicInteger();
        Timer flash = new Timer(FLASH_PERIOD_MILLIS, null);
        flash.setInitialDelay(0);
        flash.addActionListener(event -> {
            taskbar.requestWindowUserAttention(dialog);
            if (dialog.isFocused() && emitted.incrementAndGet() >= MIN_FLASHES) {
                flash.stop();
            }
        });
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent event) {
                dialog.toFront();
                flash.start();
            }

            @Override
            public void windowClosed(WindowEvent event) {
                flash.stop();
            }
        });
    }

    /** The taskbar when it can flash a window, otherwise null. */
    private static Taskbar attentionTaskbar() {
        if (!Taskbar.isTaskbarSupported()) {
            return null;
        }
        try {
            Taskbar taskbar = Taskbar.getTaskbar();
            return taskbar.isSupported(Taskbar.Feature.USER_ATTENTION_WINDOW)
                    ? taskbar : null;
        } catch (UnsupportedOperationException | SecurityException error) {
            log.log(Level.FINE, "taskbar attention request unavailable", error);
            return null;
        }
    }
}
