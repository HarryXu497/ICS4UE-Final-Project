package game.internal.entities;

import java.awt.*;

/**
 * Represents a game object in the game which can be draw and animated
 * @author Tommy Shan
 * @version 1.0 - January 9th 2024
 */
public abstract class GameObject {
    /**
     * draw
     * Draws the {@link GameObject} at the specified location.
     * @param g the {@link Graphics} object
     * @param x the x coordinate of the top left corner of the drawing location
     * @param y the y coordinate of the top left corner of the drawing location
     */
    public abstract void draw(Graphics g, int x, int y);

    /**
     * tick
     * Advances the sprite image
     */
    public abstract void tick();
}
