package game;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public enum Move implements Action {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static Move fromDirection(Direction direction) {
        if(direction == Direction.UP) return Move.UP;
        else if(direction == Direction.DOWN) return Move.DOWN;
        else if(direction == Direction.LEFT) return Move.LEFT;
        else if(direction == Direction.RIGHT) return Move.RIGHT;
        throw new IllegalArgumentException("Unknown Direction!");
    }
}
