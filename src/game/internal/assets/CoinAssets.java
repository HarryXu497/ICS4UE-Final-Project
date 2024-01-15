package game.internal.assets;

import java.awt.Image;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Contains coin-related assets.
 * @author Tommy Shan
 * @version 1.0 - January 11th 2023
 */
public class CoinAssets {
    private final Image[] coins;

    /**
     * Constructs a {@link CoinAssets} instance with a sprite size.
     * @param size the size to resize the sprites to
     * @throws IOException if an I/O error occur while reading the files
     */
    CoinAssets(int size) throws IOException {
        Map<String, Image[]> sprites = Assets.loadAndGroupImages(Paths.get("resources/coin"), size);
        this.coins = sprites.get("coin");
    }

    /**
     * getCoins
     * Gets the coin sprites.
     * @return the coin sprites
     */
    public Image[] getCoins() {
        return this.coins;
    }
}
