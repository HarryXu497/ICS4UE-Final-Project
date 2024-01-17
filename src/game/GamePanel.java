package game;

import client.ClientConnection;
import game.actions.Move;
import game.internal.assets.Assets;
import game.internal.entities.Currency;
import game.internal.entities.GameObject;
import loader.ObjectLoader;
import loader.ObjectLoaderException;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A Swing component responsible for managing and displaying the game.
 * @author Tommy Shan
 * @author Harry Xu
 * @version 1.0 - January 8th 2024
 */
public class GamePanel extends JPanel {
    /** Game constants */
    private static final int TICKS_PER_UPDATE = 8;
    private static final int MS_PER_TICK = 100;
    private static final int CYCLES_DAMAGE = 10;
    private static final int CURRENCY_VARIANCE = 3;
    private static final int MIN_CURRENCY = 4;

    /** Map generation constants */
    private static final int TILES_PER_PLAYER = 100;
    private static final int MAX_HEIGHT = 40;
    private static final int MIN_HEIGHT = 25;

    private final Timer gameLoop;
    private final Consumer<List<String>> onWin;

    private final Set<Player> players;
    private final List<Player> playerStandings;
    private final Set<Currency> currencies;

    private final int gridSize;
    private int currentCycle;

    private final GameObject[][] map;
    private final Image[][] mapTiles;

    /**
     * Constructs a {@link GamePanel}.
     * @param clients the clients with submitted codes
     * @param panelSize the size that the panel will eventually occupy.
     *                  Used to generate the map
     * @param onWin the callback function which is called when the game is won
     */
    public GamePanel(Set<ClientConnection> clients, Dimension panelSize, Consumer<List<String>> onWin) {
        this.onWin = onWin;

        // Game map dimensions
        int gridHeight = (int) Math.floor(
                Math.sqrt(
                        clients.size() * TILES_PER_PLAYER * panelSize.height / (double) panelSize.width
                )
        );

        if (gridHeight < MIN_HEIGHT) {
            gridHeight = MIN_HEIGHT;
        }

        if (gridHeight > MAX_HEIGHT) {
            gridHeight = MAX_HEIGHT;
        }

        this.gridSize = panelSize.height / gridHeight;
        int gridWidth = panelSize.width / this.gridSize;
        this.map = new GameObject[gridHeight - 2][gridWidth];
        this.mapTiles = new Image[gridHeight - 2][gridWidth];
        this.currentCycle = 1;

        this.gameLoop = new Timer();

        // Initialize sprites
        try {
            Assets.initialize(this.gridSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Load players
        ObjectLoader objectLoader;

        try {
            objectLoader = new ObjectLoader();
        } catch (IOException e) {
            System.out.println("Failed to open temporary directory for player submissions");
            throw new RuntimeException(e);
        }

        this.players = new HashSet<>();

        for (ClientConnection client : clients) {
            Player player;

            try {
                player = objectLoader.load(client);
            } catch (IOException | ObjectLoaderException e) {
                e.printStackTrace();
                continue;
            }

            this.players.add(player);
        }

        this.currencies = new HashSet<>();
        this.playerStandings = new ArrayList<>();

        // Generate map tiles
        for (int y = 0; y < this.mapTiles.length; y++) {
            for (int x = 0; x < this.mapTiles[y].length; x++) {
                this.mapTiles[y][x] = Assets.getInstance().getTile().getRandomTile();
            }
        }

        // Generate player locations
        for (Player player : this.players) {
            boolean validPosition = false;

            while (!validPosition) {
                int y = (int) (Math.random() * this.map.length);
                int x = (int) (Math.random() * this.map[y].length);

                if (this.map[y][x] == null) {
                    this.map[y][x] = player;
                    validPosition = true;
                }
            }
        }

        // Swing Setup
        this.setIgnoreRepaint(true);
    }

    /**
     * start
     * Starts the game.
     */
    public void start() {
        this.gameLoop.schedule(new GameLoopTask(), 0, MS_PER_TICK);
    }

    /**
     * runCycle
     * Runs an update cycle on the map
     */
    public void runUpdate() {
        this.generateCurrency();

        // If players should be damaged this cycle
        boolean damagePlayers = this.currentCycle % CYCLES_DAMAGE == 0;

        Set<Player> movedPlayers = new HashSet<>();

        // Iterate through map
        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObject = this.map[y][x];

                if (currentObject instanceof Player) {
                    Player player = (Player) currentObject;

                    // If player has already been moved
                    if (movedPlayers.contains(player)) {
                        continue;
                    }

                    movedPlayers.add(player);

                    if (damagePlayers) {
                        player.setHealth(player.getHealth() - 1);
                    }

                    // Player dies
                    if (player.getHealth() == 0) {
                        this.map[y][x] = null;
                        this.players.remove(player);
                        this.playerStandings.add(player);
                        continue;
                    }

                    // Call player cycle method
                    Data data = new Data(this.map, player, this.players.size(), this.currencies.size(), x, y);
                    Shop shop = new Shop(player);

                    try {
                        player.update(data, shop);
                    } catch (RuntimeException e) {
                        System.out.println("An error occurred in " + player.getName() + "'s player");
                    }

                    // Move
                    this.movePlayer(player, x, y);
                }
            }
        }

        // Player standings
        if (this.players.size() <= 1) {
            this.playerStandings.addAll(this.players);

            List<String> playerNames = this.playerStandings
                    .stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());

            this.gameLoop.cancel();

            this.onWin.accept(playerNames);
            return;
        }

        this.currentCycle++;
    }

    /**
     * runTick
     * Runs an animation frame (a tick).
     * An animation frame is NOT the same as an update cycle.
     * Animation frames occur more often to create a smoother animation.
     */
    public void runTick() {
        for (GameObject[] gameObjects : this.map) {
            for (GameObject currentObject : gameObjects) {
                if (currentObject != null) {
                    currentObject.tick();
                }
            }
        }

        repaint();
    }

    /**
     * generateCurrency
     * Randomly generates currency throughout the map.
     */
    public void generateCurrency() {
        int variance = ((int) (Math.random() * 2 * CURRENCY_VARIANCE)) - CURRENCY_VARIANCE;
        int numCurrency = Math.max(this.players.size() + variance, MIN_CURRENCY);

        for (int i = 0; i < numCurrency; i++) {
            int y = (int) (Math.random() * this.map.length);
            int x = (int) (Math.random() * this.map[y].length);

            // Adds currency only if space is empty
            if (this.map[y][x] == null) {
                this.map[y][x] = new Currency();
                this.currencies.add((Currency) this.map[y][x]);
            }
        }
    }

    /**
     * movePlayer
     * Moves the player on the map.
     * @param player the player to move
     * @param x the x coordinate of the location of the player
     * @param y the y coordinate of the location of the player
     */
    public void movePlayer(Player player, int x, int y) {
        Move move = player.getMove();

        if (move == null) {
            return;
        }

        // Move player if possible
        int newY = y + move.getDeltaY();
        int newX = x + move.getDeltaX();

        if ((newY < 0) || (newY >= this.map.length)) {
            newY = y;
        }

        if ((newX < 0) || (newX >= this.map[newY].length)) {
            newX = x;
        }

        GameObject newObject = this.map[newY][newX];

        boolean movePlayer = true;

        // Collisions
        if (player != newObject) {
            if (newObject instanceof Currency) {
                player.setCurrency(player.getCurrency() + 1);
                this.currencies.remove(newObject);
            } else if (newObject instanceof Player) {
                Player enemy = (Player) newObject;
                player.fight(enemy);

                movePlayer = false;
            }
        }

        // Move player
        if (movePlayer) {
            this.map[y][x] = null;
            this.map[newY][newX] = player;
        }
    }

    /**
     * paintComponent
     * Draws the game.
     * @param g the {@link Graphics} object used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background tiles
        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                int xCoord = x * this.gridSize;
                int yCoord = y * this.gridSize;

                g.drawImage(this.mapTiles[y][x], xCoord, yCoord, null);
            }
        }

        // Draw game objects
        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObject = this.map[y][x];

                int xCoord = x * this.gridSize;
                int yCoord = y * this.gridSize;

                if (currentObject != null) {
                    currentObject.draw(g, xCoord, yCoord);
                }
            }
        }
    }

    /**
     * Task responsible for animating (ticks) and running update cycles .
     * @author Tommy Shan
     * @author Harry Xu
     * @version 1.0 - January 10th 2024
     */
    public class GameLoopTask extends TimerTask {
        private int ticks;

        public GameLoopTask() {
            this.ticks = 0;
        }

        @Override
        public void run() {
            this.ticks++;


            if (ticks % TICKS_PER_UPDATE == 0) {
                runUpdate();
            }

            runTick();
        }
    }
}
