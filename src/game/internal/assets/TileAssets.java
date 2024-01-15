package game.internal.assets;

import java.awt.Image;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class TileAssets {
    private final double MAIN_SPRITE_WEIGHT = 0.8;

    private final Image[] mainSprites;
    private final Image[] accentSprites;

    public TileAssets(int size) throws IOException {
        Map<String, Image[]> sprites = Assets.loadAndGroupImages(Paths.get("resources/tiles"), size);

        this.mainSprites = sprites.get("main");
        this.accentSprites = sprites.get("accent");
    }

    public Image[] getAccentSprites() {
        return this.accentSprites;
    }

    public Image[] getMainSprites() {
        return this.mainSprites;
    }

    public Image getRandomTile() {
        if (Math.random() <= MAIN_SPRITE_WEIGHT) {
            int spriteIndex = (int) (Math.random() * this.mainSprites.length);
            return this.mainSprites[spriteIndex];
        } else {
            int spriteIndex = (int) (Math.random() * this.accentSprites.length);
            return this.accentSprites[spriteIndex];
        }
    }
}
