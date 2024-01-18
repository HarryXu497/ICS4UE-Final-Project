package game;

/**
 * Simple class representing an integer cartesian coordinate.
 * Created to reduce imports for clients and to simplify the client API
 * @author Tommy Shan
 * @version 1.0 - January 10th 2024
 */
public class Point {
    private final int x;
    private final int y;

    /**
     * Constructs a {@link Point}.
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * getX
     * Returns the x-coordinate of the point.
     * @return the x-coordinate of the point.
     */
    public int getX() {
        return this.x;
    }

    /**
     * getY
     * Returns the y-coordinate of the point.
     * @return the y-coordinate of the point.
     */
    public int getY() {
        return this.y;
    }
}
