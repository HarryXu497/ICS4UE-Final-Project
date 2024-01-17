package game;

import java.util.List;

/**
 * Object returned by player queries.
 * Composed of a list of {@link EnemyData} containing enemy information
 * and a list of {@link Point} representing currency location.
 * @author Tommy Shan
 * @version 1.0 - January 11th 2024
 */
public class QueryData {
    private final List<EnemyData> enemies;
    private final List<Point> currency;

    /**
     * Creates a {@link QueryData} instance containing enemy and currency data.
     * @param enemies the list of enemy data
     * @param currency the list of currency data
     */
    QueryData(List<EnemyData> enemies, List<Point> currency) {
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
