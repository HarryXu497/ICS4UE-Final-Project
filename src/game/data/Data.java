package game.data;

import game.GameObject;
import game.Player;
import util.Pair;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Data {
    private final GameObject[][] map;
    private final Player player;
    private final int x;
    private final int y;

    public Data(GameObject[][] map, Player player, int x, int y) {
        this.map = map;
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public EnemyData getEnemyLocation(int n) {
        PriorityQueue<Pair<Player, Integer>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Pair::getSecond));
        for(int i=0; i<map.length; i++) {
            for(int j=0; j<map[i].length; j++) {
                if(map[i][j] instanceof Player) {
                    int manhattanDistance = Math.abs(i - x) + Math.abs(j - y);
                    priorityQueue.add(new Pair<>((Player) map[i][j], manhattanDistance));
                }
            }
        }

    }
}
