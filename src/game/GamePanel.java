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
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class GamePanel extends JPanel {
    private static final int TICKS_PER_UPDATE = 8;
    private static final int CYCLES_DAMAGE = 10;
    private static final int CURRENCY_VARIANCE = 3;
    private static final int MIN_CURRENCY = 4;

    private static final int TILES_PER_PLAYER = 100;
    private static final int MAX_HEIGHT = 40;
    private static final int MIN_HEIGHT = 25;

    private final Consumer<String> onWin;

    private final Set<Player> players;
    private final Set<Currency> currencies;

    private final int gridSize;
    private int currentCycle;

    private final GameObject[][] map;
    private final Image[][] mapTiles;

    public GamePanel(Set<ClientConnection> clients, Dimension panelSize, Consumer<String> onWin) {
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

    public int getGridSize() {
        return this.gridSize;
    }

    public void start() {
        Timer gameLoop = new Timer();
        gameLoop.schedule(new GameLoopTask(), 0, 100);
    }

    public void runCycle() {
        boolean damagePlayers = this.currentCycle % CYCLES_DAMAGE == 0;
        int numPlayers = 0;

        this.generateCurrency();

        Set<Player> movedPlayers = new HashSet<>();

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObject = this.map[y][x];

                if (currentObject instanceof Player) {
                    Player player = (Player) currentObject;

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
                        continue;
                    }

                    Data data = new Data(this.map, player, this.players.size(), this.currencies.size(), x, y);
                    Shop shop = new Shop(player);

                    numPlayers++;

                    try {
                        player.update(data, shop);
                    } catch (RuntimeException e) {
                        System.out.println("An error occurred in " + player.getName() + "'s player");
                        e.printStackTrace();
                    }

                    // Move
                    this.movePlayer(player, x, y);
                }
            }
        }

        if (numPlayers == 1) {
            Player winner = null;

            for (int y = 0; y < this.map.length; y++) {
                for (int x = 0; x < this.map[y].length; x++) {
                    GameObject currentObject = this.map[y][x];

                    if (currentObject instanceof Player) {
                        winner = (Player) currentObject;
                    }
                }
            }

            if (winner == null) {
                throw new RuntimeException("This should never happen");
            }

            onWin.accept(winner.getName());
            return;
        }

        this.currentCycle++;
    }

    public void runTick() {
        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObject = this.map[y][x];

                if (currentObject != null) {
                    currentObject.tick();
                }
            }
        }

        repaint();
    }

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

    public void movePlayer(Player player, int x, int y) {
        Move move = player.getMove();

        if (move != null) {
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

            if (movePlayer) {
                this.map[y][x] = null;
                this.map[newY][newX] = player;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                int xCoord = x * this.gridSize;
                int yCoord = y * this.gridSize;

                g.drawImage(this.mapTiles[y][x], xCoord, yCoord, null);
            }
        }

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

    public class GameLoopTask extends TimerTask {

        private int ticks;

        public GameLoopTask() {
            this.ticks = 0;
        }

        @Override
        public void run() {
            this.ticks++;


            if (ticks % TICKS_PER_UPDATE == 0) {
                runCycle();
            }

            runTick();
        }
    }
}
