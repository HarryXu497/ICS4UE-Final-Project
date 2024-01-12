package game;

import game.internal.actions.Action;
import game.internal.actions.Direction;
import game.internal.actions.Move;
import game.internal.actions.Turn;
import game.internal.entities.GameObject;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;

public abstract class Player extends GameObject {
    private int currency;
    private int health;
    private int power;
    private final Deque<Action> actions;
    private Direction direction;

    public Player() {
        this.actions = new ArrayDeque<>();
    }

    // Methods accessible to game
    void fight(Player enemy) {
        this.setHealth(this.getHealth() - enemy.getPower());
        enemy.setHealth(enemy.getHealth() - this.getPower());
    }

    Deque<Action> getActions() {
        return this.actions;
    }

    @Override
    public void draw(Graphics g, int x, int y, int gridSize) {
        int offset = (gridSize / 2) - 10;
        g.setColor(Color.RED);
        g.drawRect(x - offset, y - offset, 20, 20);
    }

    void setHealth(int newHealth) {
        this.health = Math.max(0, newHealth);
    }

    void setPower(int newPower) {
        this.power = Math.max(0, newPower);
    }

    void setCurrency(int newCurrency) {
        this.currency = Math.max(0, newCurrency);
    }

    // Methods accessible to user

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

    public int getCurrency() {
        return this.currency;
    }

    public int getHealth() {
        return this.health;
    }

    public int getPower() {
        return this.power;
    }

    // Abstract methods
    public abstract void cycle(Data data, Shop shop);
}
