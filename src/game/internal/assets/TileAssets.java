package game.internal.assets;

import java.awt.Image;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Contains tile-related assets
 * @author Tommy Shan
 * @version 1.0 - January 10th 2024
 */
public class TileAssets {
    private final double MAIN_SPRITE_WEIGHT = 0.8;

    private final Image[] mainSprites;
    private final Image[] accentSprites;

    /**
     * Constructs a {@link TileAssets} instance with a sprite size
     * @param size the size to resize the sprites to
     * @throws IOException if an I/O error occur while reading the files
     */
    TileAssets(int size) throws IOException {
        Map<String, Image[]> sprites = Assets.loadAndGroupImages(Paths.get("resources/tiles"), size);

        this.mainSprites = sprites.get("main");
        this.accentSprites = sprites.get("accent");
    }

    /**
     * getRandomTile
     * Generates a random tile which is either a main or accent tile
     * with a weighting.
     * @return the generated tile
     */
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
