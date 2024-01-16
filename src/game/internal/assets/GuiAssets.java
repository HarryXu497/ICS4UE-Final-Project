package game.internal.assets;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 * Contains GUI-related assets.
 * @author Tommy Shan
 * @version 1.0 - January 10th 2024
 */
public class GuiAssets {
    private final Image heart;

    /**
     * Constructs a {@link GuiAssets} instance with a sprite size.
     * @param size the size to resize the sprites to
     * @throws IOException if an I/O error occur while reading the files
     */
    GuiAssets(int size) throws IOException {
        this.heart = ImageIO.read(new File("resources/gui/heart.png"))
                .getScaledInstance(size / 5, size / 5, Image.SCALE_DEFAULT);
    }

    /**
     * getCoins
     * Gets the heart sprites.
     * @return the heart sprites
     */
    public Image getHeart() {
        return this.heart;
    }
}
