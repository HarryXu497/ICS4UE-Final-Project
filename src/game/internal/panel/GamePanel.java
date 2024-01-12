package game.internal.panel;

import game.Data;
import game.Player;
import game.Shop;
import game.internal.entities.GameObject;
import ui.Const;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {
    private static final int GRID_DIMENSIONS = 30; // TODO: make this dynamic
    private static final int GRID_SIZE = Const.FRAME_HEIGHT / GRID_DIMENSIONS;

    private final GameObject[][] map;

    public GamePanel(Set<Player> players) {
        this.map = new GameObject[GRID_DIMENSIONS][GRID_DIMENSIONS];

        // Generate player locations
        for (Player player : players) {
            boolean validPosition = false;

            while (!validPosition) {
                int x = (int) (Math.random() * 30);
                int y = (int) (Math.random() * 30);

                if (this.map[y][x] == null) {
                    this.map[y][x] = player;
                    validPosition = true;
                }
            }
        }

        // Swing Setup
        this.setIgnoreRepaint(true);
    }

    public void start() {
        Timer gameLoop = new Timer();
        gameLoop.schedule(new GameLoopTask(), 0, 500);
    }

    public void runCycle() {
        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObject = this.map[y][x];

                if (currentObject instanceof Player) {
                    Player player = (Player) currentObject;

                    Data data = new Data(this.map, player, 10, 10, x, y);
                    Shop shop = new Shop(player);

                    player.cycle(data, shop);
                }
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                GameObject currentObject = this.map[y][x];

                g.setColor(Color.BLACK);
                g.drawRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);

                if (currentObject != null) {
                    currentObject.draw(g, x, y, GRID_SIZE);
                }
            }
        }
    }

    public class GameLoopTask extends TimerTask {
        @Override
        public void run() {
            runCycle();
        }
    }
}
