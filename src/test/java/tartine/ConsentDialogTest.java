package tartine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import org.junit.jupiter.api.Test;
import tartine.graphics.WindowIcon;

/**
 * Pins the bridge from the guest APK's straight-alpha, top-down RGBA window
 * icons into the channel-reordered AWT images the consent dialog shows.
 */
class ConsentDialogTest {

    @Test
    void convertsStraightAlphaRgbaToArgbInOrder() {
        WindowIcon icon = new WindowIcon(2, 1, new byte[] {
                (byte) 0xFF, 0x00, 0x00, (byte) 0x80,
                0x00, (byte) 0xFF, 0x00, (byte) 0xFF,
        });

        BufferedImage image = ConsentDialog.toBufferedImage(icon);

        assertEquals(BufferedImage.TYPE_INT_ARGB, image.getType());
        assertEquals(2, image.getWidth());
        assertEquals(1, image.getHeight());
        assertEquals(0x80FF0000, image.getRGB(0, 0));
        assertEquals(0xFF00FF00, image.getRGB(1, 0));
    }

    @Test
    void keepsEveryIconInOrder() {
        WindowIcon small = new WindowIcon(1, 1, new byte[] {0, 0, 0, 0});
        WindowIcon large = new WindowIcon(2, 2, new byte[16]);

        List<Image> images = ConsentDialog.toAwtImages(List.of(small, large));

        assertEquals(2, images.size());
        assertEquals(1, ((BufferedImage) images.get(0)).getWidth());
        assertEquals(2, ((BufferedImage) images.get(1)).getWidth());
        assertTrue(ConsentDialog.toAwtImages(List.of()).isEmpty());
    }
}
