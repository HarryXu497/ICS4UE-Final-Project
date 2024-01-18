package game.internal.assets;

import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Contains player-related assets
 * @author Tommy Shan
 * @version 1.0 - January 10th 2024
 */
public class PlayerAssets {
    private static final int MAX_FONT_SIZE = 16;

    private final Image[] idleSprites;
    private final Image[] movingSprites;
    private final Image[] hurtSprites;
    private final Font font;

    /**
     * Constructs a {@link PlayerAssets} instance with a sprite size
     * @param size the size to resize the sprites to
     * @throws IOException if an I/O error occur while reading the files
     */
    PlayerAssets(int size) throws IOException {
        Map<String, Image[]> sprites = Assets.loadAndGroupImages(Paths.get("resources/sprites"), size);

        this.idleSprites = sprites.get("idle");
        this.movingSprites = sprites.get("moving");
        this.hurtSprites = sprites.get("hurt");

        this.font = new Font("Jetbrains Mono", Font.PLAIN, Math.min(MAX_FONT_SIZE, size / 3));
    }

    /**
     * getIdleSprites
     * Gets the idle player sprites.
     * @return the idle player sprites
     */
    public Image[] getIdleSprites() {
        return this.idleSprites;
    }

    /**
     * getMovingSprites
     * Gets the moving player sprites.
     * @return the moving player sprites
     */
    public Image[] getMovingSprites() {
        return this.movingSprites;
    }

    /**
     * getHurtSprites
     * Gets the hurt player sprites.
     * @return the hurt player sprites
     */
    public Image[] getHurtSprites() {
        return this.hurtSprites;
    }

    /**
     * getFont
     * Gets the player's name font.
     * @return the font used for player-related text
     */
    public Font getFont() {
        return this.font;
    }
}
