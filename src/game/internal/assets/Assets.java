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

public class Assets {
    private static Assets instance;

    private final PlayerAssets player;
    private final TileAssets tile;
    private final GuiAssets gui;
    private final CoinAssets coins;

    private Assets(PlayerAssets player, TileAssets tile, GuiAssets gui, CoinAssets coins) {
        this.player = player;
        this.tile = tile;
        this.gui = gui;
        this.coins = coins;
    }

    public PlayerAssets getPlayer() {
        return this.player;
    }

    public TileAssets getTile() {
        return this.tile;
    }

    public GuiAssets getGui() {
        return this.gui;
    }

    public CoinAssets getCoins() {
        return this.coins;
    }

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
