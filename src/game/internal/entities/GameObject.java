package game.internal.entities;

import java.awt.*;

public abstract class GameObject {
    public abstract void draw(Graphics g, int x, int y);
    public abstract void tick();
}
