package game.internal.assets;

import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class PlayerAssets {
    private static final int MAX_FONT_SIZE = 16;

    private final Image[] idleSprites;
    private final Image[] movingSprites;
    private final Image[] hurtSprites;

    private final Font font;

    public PlayerAssets(int size) throws IOException {
        Map<String, Image[]> sprites = Assets.loadAndGroupImages(Paths.get("resources/sprites/computer2"), size);

        this.idleSprites = sprites.get("idle");
        this.movingSprites = sprites.get("moving");
        this.hurtSprites = sprites.get("hurt");

        this.font = new Font("Jetbrains Mono", Font.PLAIN, Math.min(MAX_FONT_SIZE, size / 3));
    }

    public Image[] getIdleSprites() {
        return this.idleSprites;
    }

    public Image[] getMovingSprites() {
        return this.movingSprites;
    }

    public Image[] getHurtSprites() {
        return this.hurtSprites;
    }

    public Font getFont() {
        return this.font;
    }
}
