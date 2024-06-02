package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class FishPowerup implements Powerup {
    private MyRowing myRowing;
    private final Texture texture = new Texture("powerups/koi.png");
    private final String description = "Fills the acceleration bar.";
    public FishPowerup(MyRowing myRowing) {
        this.myRowing = myRowing;
    }

    @Override
    public void use() {
        this.myRowing.maxAccelerationLevel();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
