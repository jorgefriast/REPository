package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class FlowerPowerup implements Powerup {

    private MyRowing myRowing;
    private final Texture texture = new Texture("powerups/sakura-flower.png");

    public FlowerPowerup(MyRowing myRowing) {
        this.myRowing = myRowing;
    }

    @Override
    public void use() {

    }

    @Override
    public Texture getTexture() {
        return this.texture;
    }
}
