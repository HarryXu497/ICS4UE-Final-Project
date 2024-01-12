package game;

import game.internal.entities.Currency;
import game.internal.entities.GameObject;
import util.Pair;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Data {
    private final GameObject[][] map;
    private final Player player;
    private final int numPlayers;
    private final int numCurrency;
    private final int x;
    private final int y;

    public Data(GameObject[][] map, Player player, int numPlayers, int numCurrency, int x, int y) {
        this.map = map;
        this.player = player;
        this.numPlayers = numPlayers;
        this.numCurrency = numCurrency;
        this.x = x;
        this.y = y;
    }

    public EnemyData getEnemyLocation(int nClosest) {
        if (nClosest >= this.numPlayers) {
            return null;
        }

        // Priority queue of enemies sorted on distance
        PriorityQueue<Pair<Player, Integer>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Pair::getSecond));
        Map<Player, Point> playerPositions = new HashMap<>();

        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                if (this.map[i][j] instanceof Player && (i != this.x || j != this.y)) {
                    int manhattanDistance = Math.abs(i - this.x) + Math.abs(j - this.y);

                    priorityQueue.add(new Pair<>((Player) this.map[i][j], manhattanDistance));
                    playerPositions.put((Player) this.map[i][j], new Point(i, j));
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

        Player enemy = topEntry.getFirst();

        return new EnemyData(enemy, playerPositions.get(enemy));
    }

    public Point getCurrencyLocation(int nClosest) {
        if (nClosest >= this.numCurrency) {
            return null;
        }

        PriorityQueue<Pair<Currency, Integer>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Pair::getSecond));
        Map<Currency, Point> currencyPositions = new HashMap<>();

        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                if (this.map[i][j] instanceof Currency && (i != this.x || j != this.y)) {
                    int manhattanDistance = Math.abs(i - this.x) + Math.abs(j - this.y);

                    priorityQueue.add(new Pair<>((Currency) this.map[i][j], manhattanDistance));
                    currencyPositions.put((Currency) this.map[i][j], new Point(i, j));
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
