package game.internal.assets;

import java.awt.Image;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class CoinAssets {
    private final Image[] coins;

    public CoinAssets(int size) throws IOException {
        Map<String, Image[]> sprites = Assets.loadAndGroupImages(Paths.get("resources/coin"), size);
        this.coins = sprites.get("coin");
    }

    public Image[] getCoins() {
        return this.coins;
    }
}
