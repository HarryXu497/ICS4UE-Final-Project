package game;

import java.util.List;

public class QueryData {
    private final List<EnemyData> enemies;
    private final List<Point> currency;

    public QueryData(List<EnemyData> enemies, List<Point> currency) {
        this.enemies = enemies;
        this.currency = currency;
    }

    public List<EnemyData> getEnemies() {
        return this.enemies;
    }

    public List<Point> getCurrency() {
        return this.currency;
    }
}
