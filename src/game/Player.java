package game;

import game.data.Data;

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
        // TODO: implement this
    }

    @Override
    void draw(Graphics g, int x, int y) {
        g.drawRect(x + 10, y + 10, 20, 20);
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
    public abstract void cycle(Data data);
}
