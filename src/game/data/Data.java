package game.data;

import game.GameObject;
import game.Player;
import util.Pair;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
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

    public EnemyData getEnemyLocation(int nClosest) {
        PriorityQueue<Pair<Player, Integer>> priorityQueue = new PriorityQueue<>(Comparator.comparing(Pair::getSecond));
        Map<Player, Point> mp = new HashMap<>();
        for(int i=0; i<map.length; i++) {
            for(int j=0; j<map[i].length; j++) {
                if(map[i][j] instanceof Player && (i != x || j != y)) {
                    int manhattanDistance = Math.abs(i - x) + Math.abs(j - y);
                    priorityQueue.add(new Pair<>((Player) map[i][j], manhattanDistance));
                    mp.put((Player) map[i][j], new Point(i, j));
                }
            }
        }
        for(int i=1; i<nClosest; i++) priorityQueue.poll();
        Pair<Player, Integer> enemy = priorityQueue.peek();
        EnemyData enemyInfo = new EnemyData(enemy.getFirst(), mp.get(enemy.getFirst()));
        return enemyInfo;
    }
}
