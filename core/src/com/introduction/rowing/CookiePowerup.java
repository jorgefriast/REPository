package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class CookiePowerup implements Powerup {
    private MyRowing myRowing;
    private final Texture texture = new Texture("powerups/cookie.png");
    private final String description = "Increase the boat health by 25 points.";

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
}
