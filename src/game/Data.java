package game;

import game.internal.entities.Currency;
import game.internal.entities.GameObject;
import util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Data {
    private final GameObject[][] map;
    private final Player player;
    private final int numPlayers;
    private final int numCurrency;
    private final int playerX;
    private final int playerY;

    private boolean enemyQueried;
    private boolean currencyQueried;

    public Data(GameObject[][] map, Player player, int numPlayers, int numCurrency, int playerX, int playerY) {
        this.map = map;
        this.player = player;
        this.numPlayers = numPlayers;
        this.numCurrency = numCurrency;
        this.playerX = playerX;
        this.playerY = playerY;
    }

    public Point getPosition() {
        return new Point(this.playerX, this.playerY);
    }

    public QueryData query(Point target) {
        return this.query(target.getX(), target.getY());
    }

    public QueryData query(int targetX, int targetY) {
        if ((targetY < 0) || (targetY >= this.map.length) || (targetX < 0) || (targetX >= this.map[targetY].length)) {
            return null;
        }

        List<EnemyData> enemies = new ArrayList<>();
        List<Point> currency = new ArrayList<>();

        int playerRange = this.player.getRange();

        int minX = Math.max(0, playerX - playerRange);
        int minY = Math.max(0, playerY - playerRange);
        int maxX = Math.min(this.map[playerY].length - 1, playerX + playerRange);
        int maxY = Math.min(this.map.length - 1, playerY + playerRange);

        for (int y = minY; y <= maxY; y++){
            for (int x = minX; x <= maxX; x++) {
                GameObject gameObject = this.map[y][x];

                if (gameObject == null) {
                    continue;
                }

                Point currentPoint = new Point(x, y);

                if (gameObject instanceof Player) {
                    Player enemy = (Player) gameObject;

                    if (this.player != enemy) {
                        enemies.add(new EnemyData(enemy, currentPoint));
                    }
                }

                if (gameObject instanceof Currency) {
                    currency.add(currentPoint);
                }
            }
        }

        return new QueryData(enemies, currency);
    }

    public Point getEnemyLocation(int nClosest) {
        if (nClosest >= this.numPlayers) {
            return null;
        }

        // Only allow player to call this method once
        if (this.enemyQueried) {
            return null;
        }

        this.enemyQueried = true;

        // Priority queue of enemies sorted on distance
        PriorityQueue<Pair<Player, Integer>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Pair::getSecond));
        Map<Player, Point> playerPositions = new HashMap<>();

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                if (this.map[y][x] instanceof Player && (y != this.playerY || x != this.playerX)) {
                    int manhattanDistance = Math.abs(y - this.playerY) + Math.abs(x - this.playerX);

                    priorityQueue.add(new Pair<>((Player) this.map[y][x], manhattanDistance));
                    playerPositions.put((Player) this.map[y][x], new Point(x, y));
                }
            }
        }

        for (int i = 1; i < nClosest; i++) {
            priorityQueue.poll();
        }

        Pair<Player, Integer> topEntry = priorityQueue.peek();

        if (topEntry == null) {
            return null;
        }

        return playerPositions.get(topEntry.getFirst());
    }

    public Point getCurrencyLocation(int nClosest) {
        if (nClosest >= this.numCurrency) {
            return null;
        }

        // Only allow player to call this method once
        if (this.currencyQueried) {
            return null;
        }

        this.currencyQueried = true;

        PriorityQueue<Pair<Currency, Integer>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Pair::getSecond));
        Map<Currency, Point> currencyPositions = new HashMap<>();

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length; x++) {
                if (this.map[y][x] instanceof Currency && (y != this.playerX || x != this.playerY)) {
                    int manhattanDistance = Math.abs(y - this.playerY) + Math.abs(x - this.playerX);

                    priorityQueue.add(new Pair<>((Currency) this.map[y][x], manhattanDistance));
                    currencyPositions.put((Currency) this.map[y][x], new Point(x, y));
                }
            }
        }

        for (int i = 1; i < nClosest; i++) {
            priorityQueue.poll();
        }

        Pair<Currency, Integer> currency = priorityQueue.peek();

        if (currency == null) {
            return null;
        }

        return currencyPositions.get(currency.getFirst());
    }
}
