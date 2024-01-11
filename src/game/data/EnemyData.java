package game.data;

import game.Player;

import java.awt.Point;

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
        return (int) this.enemyPosition.getX();
    }

    public int getEnemyY() {
        return (int) this.enemyPosition.getY();
    }

    public int getEnemyPower() {
        return this.enemyPower;
    }

    public int getEnemyHealth() {
        return this.enemyHealth;
    }
}
