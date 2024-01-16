package game;

/**
 * Class containing information about a queried enemy.
 * @author Tommy Shan
 * @version 1.0 - January 11th 2024
 */
public class EnemyData {
    private final Point enemyPosition;
    private final int enemyHealth;
    private final int enemyPower;

    /**
     * Constructs an {@link EnemyData} instance with
     * an enemy player instance and its position.
     * @param enemy the enemy player
     * @param enemyPosition the position of the enemy player
     */
    EnemyData(Player enemy, Point enemyPosition) {
        this.enemyPosition = enemyPosition;
        this.enemyHealth = enemy.getHealth();
        this.enemyPower = enemy.getPower();
    }

    /**
     * getEnemyPosition
     * Gets the position of the enemy.
     * @return the enemy's position as a {@link Point}
     */
    public Point getEnemyPosition() {
        return this.enemyPosition;
    }

    /**
     * getEnemyPower
     * Gets the power of the enemy.
     * @return the enemy's power
     */
    public int getEnemyPower() {
        return this.enemyPower;
    }

    /**
     * getEnemyHealth
     * Gets the health of the enemy.
     * @return the enemy's health
     */
    public int getEnemyHealth() {
        return this.enemyHealth;
    }
}
