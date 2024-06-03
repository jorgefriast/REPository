package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class CookiePowerup implements Powerup {
    private MyRowing myRowing;
    private final Texture texture = new Texture("powerups/cookie.png");
    private final String description = "Increase the boat health by\n25 points.";
    private final String name = "Fortune Cookie";
    private final int price = 150;

    public CookiePowerup(MyRowing myRowing) {
        this.myRowing = myRowing;
    }

    @Override
    public void use() {
        Boat playerBoat = this.myRowing.getPlayerBoat();
        playerBoat.setBoatHealth(playerBoat.getBoatHealth() + 25);
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
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }
}
