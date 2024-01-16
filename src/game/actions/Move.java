package game.actions;

/**
 * Represents a move action by the player.
 * @author Tommy Shan
 * @version 1.0 - January 8th 2024
 */
public enum Move {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int deltaX;
    private final int deltaY;

    /**
     * Constructs a {@link Move} with a
     * delta x and delta y associated.
     */
    Move(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    /**
     * getDeltaX
     * Gets the change in x coordinate of this move.
     * @return the change in x
     */
    public int getDeltaX() {
        return this.deltaX;
    }

    /**
     * getDeltaY
     * Gets the change in y coordinate of this move.
     * @return the change in y
     */
    public int getDeltaY() {
        return this.deltaY;
    }

    /**
     * fromDirection
     * Returns the corresponding move based on the inputted direction.
     * @param direction the direction to convert
     * @return the corresponding move action
     */
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
