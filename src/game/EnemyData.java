package game;

public class EnemyData {
    private final Point enemyPosition;
    private final int enemyHealth;
    private final int enemyPower;

    public EnemyData(Player enemy, Point playerLocation) {
        this.enemyPosition = playerLocation;
        this.enemyHealth = enemy.getHealth();
        this.enemyPower = enemy.getPower();
    }

    public int getEnemyX() {
        return this.enemyPosition.getX();
    }

    public int getEnemyY() {
        return this.enemyPosition.getY();
    }

    public int getEnemyPower() {
        return this.enemyPower;
    }

    public int getEnemyHealth() {
        return this.enemyHealth;
    }
}
