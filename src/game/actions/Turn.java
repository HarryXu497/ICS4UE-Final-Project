package game.actions;

public enum Turn implements MovementAction {
    LEFT,
    RIGHT,
    BACK;

    public Direction turn(Direction currentDirection) {
        Direction[] directions = Direction.values();
        int currentIndex = 0;

        for (int i = 0; i < directions.length; i++) {
            if (directions[i] == currentDirection) {
                currentIndex = i;
                break;
            }
        }

        if (this == LEFT) {
            return directions[Math.floorMod(currentIndex + 1, directions.length)];
        } else if (this == RIGHT) {
            return directions[Math.floorMod(currentIndex - 1, directions.length)];
        } else {
            return directions[Math.floorMod(currentIndex + 2, directions.length)];
        }
    }
}
