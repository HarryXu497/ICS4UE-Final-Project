package game.internal.assets;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class GuiAssets {
    private final Image heart;

    public GuiAssets(int size) throws IOException {
        this.heart = ImageIO.read(new File("resources/gui/heart.png"))
                .getScaledInstance(size / 5, size / 5, Image.SCALE_DEFAULT);
    }

    public Image getHeart() {
        return this.heart;
    }
}
