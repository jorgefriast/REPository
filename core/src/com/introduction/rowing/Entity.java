package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;
import org.w3c.dom.Text;

import java.awt.*;

public class Entity {
    protected Position position;
    protected int width, height;

    protected Texture image;

    public Entity(Position position, int width, int height, Texture image) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    /**
     * Get the position of the entity
     * @return the position of the entity
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the width of the entity
     * @return the width of the entity
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the entity
     * @return the height of the entity
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the image of the entity
     * @return the image of the entity
     */
    public Texture getImage() {
        return image;
    }

    public void setPosition(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    public Rectangle getBounds() {
        return new Rectangle(position.getX(), position.getY(), width, height);
    }


}
