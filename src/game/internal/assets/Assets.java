package game.internal.assets;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton class to allow for global access to assets (e.g. sprites and fonts) .
 * @author Tommy Shan
 * @version 1.0 - January 10th 2023
 */
public class Assets {
    /** Singleton instance */
    private static Assets instance;

    private final PlayerAssets player;
    private final TileAssets tile;
    private final GuiAssets gui;
    private final CoinAssets coins;

    /**
     * Constructs assets with sub-asset classes.
     * @param player the player sub-assets
     * @param tile the tile sub-assets
     * @param gui the gui sub-assets
     * @param coins the coin sub-assets
     */
    private Assets(PlayerAssets player, TileAssets tile, GuiAssets gui, CoinAssets coins) {
        this.player = player;
        this.tile = tile;
        this.gui = gui;
        this.coins = coins;
    }

    /**
     * getPlayer
     * Gets the player sub-assets.
     * @return the player sub-assets
     */
    public PlayerAssets getPlayer() {
        return this.player;
    }

    /**
     * getTile
     * Gets the tile sub-assets.
     * @return the tile sub-assets
     */
    public TileAssets getTile() {
        return this.tile;
    }

    /**
     * getGui
     * Gets the gui sub-assets.
     * @return the gui sub-assets
     */
    public GuiAssets getGui() {
        return this.gui;
    }

    /**
     * getCoins
     * Gets the coin sub-assets.
     * @return the coin sub-assets
     */
    public CoinAssets getCoins() {
        return this.coins;
    }

    /**
     * initialize
     * Loads, initializes, and processes the assets,
     * and sets the instance to the {@link #instance} variable.
     * @param size the size to load the sprites to
     * @throws IOException if an I/O error occur while reading the files
     */
    public static void initialize(int size) throws IOException {
        if (instance != null) {
            return;
        }

        instance = new Assets(
                new PlayerAssets(size),
                new TileAssets(size),
                new GuiAssets(size),
                new CoinAssets(size)
        );
    }

    /**
     * getInstance
     * Gets the singleton instance of the class
     * @return the singleton {@link Assets} instance
     */
    public static Assets getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Sprites not initialized");
        }

        return instance;
    }

    /**
     * processImage
     * Converts an entry of directories to image paths into an entry of directory names to resized images.
     * @param entry the map entry
     * @param size the resized size of the images
     * @return the transformed entry
     */
    private static Map.Entry<String, Image[]> processImage(Map.Entry<Path, List<Path>> entry, int size) {
        String color = entry.getKey().toString();
        List<Path> files = entry.getValue();

        Image[] images = new Image[files.size()];

        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i).toFile();

            try {
                images[i] = ImageIO.read(file).getScaledInstance(size, size, Image.SCALE_DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new AbstractMap.SimpleEntry<>(color, images);
    }

    /**
     * processImage
     * Helper function which partially applies the
     * {@link Assets#processImage(Map.Entry, int)} method with a size parameter.
     * @param size the resized size of the images
     * @return a partially applied function which maps an
     * entry of directories to image paths into an
     * entry of directory names to resized images
     */
    static Function<Map.Entry<Path, List<Path>>, Map.Entry<String, Image[]>> processImage(int size) {
        return (Map.Entry<Path, List<Path>> entry) -> processImage(entry, size);
    }

    /**
     * loadAndGroupImages
     * Loads all images within a root directory and
     * categorizes them by the name of their parent directory.
     * @param root the root directory
     * @param size the resized size of the images
     * @return a map of parent directory names to images
     */
    static Map<String, Image[]> loadAndGroupImages(Path root, int size) throws IOException {
        try (Stream<Path> paths = Files.walk(root)) {
            // Read and process sprites with stream API
            return paths
                    .filter(Files::isRegularFile)
                    .collect(Collectors.groupingBy(path -> path.getName(path.getNameCount() - 2)))
                    .entrySet()
                    .stream()
                    .map(processImage(size))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
    }
}
