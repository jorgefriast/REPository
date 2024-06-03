package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class FlowerPowerup implements Powerup {

    private MyRowing myRowing;
    private final String description = "Set the momemtum to 3 until\nyour boat collides with an\nobstacle.";
    private final String name = "Sakura Flower";
    private final int price = 150;
    private final Texture texture = new Texture("powerups/sakura_flower.png");

    public FlowerPowerup(MyRowing myRowing) {
        this.myRowing = myRowing;
    }

    @Override
    public void use() {
        myRowing.getPlayerBoat().activateMomemtumPowerup();
    }

    @Override
    public Texture getTexture() {
        return this.texture;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return  this.name;
    }

    @Override
    public int getPrice() {
        return this.price;
    }
}
