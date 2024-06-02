package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class CatPowerup implements Powerup {
    private MyRowing myRowing;
    private final Texture texture = new Texture("powerups/cat.png");
    private final String description = "Gives invulnerability to obstacles\ncollisions for 5 seconds.";
    private final String name = "Maneki Neko (Fortune Cat)";
    private final int price = 201;


    public CatPowerup(MyRowing myRowing) {
        this.myRowing = myRowing;
    }

    @Override
    public void use() {
        myRowing.getPlayerBoat().setInvulnerabilityTime(5);
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
    public int getPrice() {
        return price;
    }

    @Override
    public String getName() {
        return name;
    }
}
