package game.internal.actions;

public enum Move implements Action {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int deltaX;
    private final int deltaY;

    Move(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return this.deltaX;
    }

    public int getDeltaY() {
        return this.deltaY;
    }

    public static Move fromDirection(Direction direction) {
        if (direction == Direction.UP) {
            return Move.UP;
        } else if (direction == Direction.DOWN) {
            return Move.DOWN;
        } else if (direction == Direction.LEFT) {
            return Move.LEFT;
        } else if (direction == Direction.RIGHT) {
            return Move.RIGHT;
        }

        throw new IllegalArgumentException("Unknown Direction!");
    }
}
