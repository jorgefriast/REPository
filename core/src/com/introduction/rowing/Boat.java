package com.introduction.rowing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Boat extends Entity{
    private int speedX;
    private int speedY;
    private boolean isPlayer;
    public final MyInputProcessor inputProcessor;

    public Boat(int speedX, int speedY, Position position, Texture image, boolean isPlayer) {
        super(position, image.getWidth()/2, image.getHeight()/2, image);
        this.speedX = speedX;
        this.speedY = speedY;
        this.isPlayer = isPlayer;
        this.inputProcessor = new MyInputProcessor();
        if (isPlayer)
            Gdx.input.setInputProcessor(inputProcessor);
    }

    public void update(float delta) {
        if(!isPlayer) return;
        // Check if boat is moving based on input
        boolean moving = inputProcessor.moving;
        int direction = inputProcessor.direction;

        // Adjust boat position based on input direction
        float newX = position.getX();
        if (moving) {
            switch (direction) {
                case 1: // Left
                    newX -= (speedX * 2);
                    break;
                case 3: // Right
                    newX +=  (speedX * 2);
                    break;
            }
        }
        // Update boat position
        position.setX(Math.round(Math.max(0, Math.min(Gdx.graphics.getWidth() - ((float) image.getWidth() /2), newX))));

    }
    /**
     * Change the speed of the boat vertically
     * @param speed the new speed of the boat
     */
    public void setSpeedY(int speed) {
        this.speedY = speed;
    }

    /**
     * Change the speed of the boat horizontally
     * @param speedX the new speed of the boat horizontally
     */
    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    /**
     * Get the position of the boat
     * @return the position of the boat
     */
    public Position getPosition() {
        return position;
    }

}
