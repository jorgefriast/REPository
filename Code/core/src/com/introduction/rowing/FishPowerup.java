package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class FishPowerup implements Powerup {
    private MyRowing myRowing;
    private final Texture texture = new Texture("powerups/koi.png");
    private final String description = "All obstacles in your lane \n disappear";
    private final String name = "Koi";
    private final int price = 200;
    public FishPowerup(MyRowing myRowing) {
        this.myRowing = myRowing;
    }

    @Override
    public void use() {
        this.myRowing.deleteAllPlayerObstacles();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getPrice() {
        return this.price;
    }
}
