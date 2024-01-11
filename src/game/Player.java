package game;

import java.util.Deque;

public abstract class Player {
    private int battery;
    private int power;
    private int fightingPoint;
    private Deque<Action> actions;
    private Direction direction;

    public abstract void fight(int enemyId);

    public void move() {
        actions.add(Move.fromDirection(direction));
    }

    public void turnLeft() {
        actions.add(Turn.LEFT);
    }

    public void turnRight() {
        actions.add(Turn.RIGHT);
    }

    public void turnBack() {
        actions.add(Turn.BACK);
    }

    public int getBattery() {
        return this.battery;
    }
    public int getPower() { return this.power; }
    public int getFightingPoint() { return this.fightingPoint; }
}
