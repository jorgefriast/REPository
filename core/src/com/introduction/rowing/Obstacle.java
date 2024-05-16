package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class Obstacle extends Entity {

    protected int damage, pushBack;

    public Obstacle(Position position, int width, int height, Texture image, int damage, int pushBack) {
        super(position, width, height, image);
        this.damage = damage;
        this.pushBack = pushBack;
    }

    /**
     * Get the damage of the entity
     * @return the damage of the entity
     */
    public int getDamage() {
        return damage;
    }
}
