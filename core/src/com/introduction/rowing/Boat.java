package com.introduction.rowing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Boat extends Entity{
    private final int speedFactor;
    private final int acceleration;
    private final int robustness;
    private final int momentum;
    private final int fatigue;
    private int speedX;
    private double speedY = 1;
    private final MyInputProcessor inputProcessor;
    private boolean isPlayer;

    private int timeTicker = 0;
    private boolean accelerating = false;

    public Boat(Position position, Texture image, boolean isPlayer, int speedFactor, int acceleration, int robustness, int maneuverability, int momentum, int fatigue) {
        super(position, image.getWidth()/2, image.getHeight()/2, image);
        this.speedX = maneuverability;
        this.speedY = getCurrentSpeed();
        this.inputProcessor = new MyInputProcessor();
        this.speedFactor = speedFactor;
        this.acceleration = acceleration;
        this.robustness = robustness;
        this.momentum = momentum;
        this.fatigue = fatigue;
        Gdx.input.setInputProcessor(inputProcessor);
        if (isPlayer)
            Gdx.input.setInputProcessor(inputProcessor);
    }

    public void updateKeys(float delta) {
        if(!isPlayer) return;
        // Check if boat is moving based on input
        boolean moving = inputProcessor.moving;
        int direction = inputProcessor.direction;

        // Adjust boat position based on input direction
        float newX = position.getX();
        if (moving) {
            switch (direction) {
                case 0: // Up
                    accelerating = true;
                    updateY(delta);
                    accelerating = false;
                    break;
                case 1: // Left
                    newX -= (speedX * 2);
                    break;
                case 3: // Right
                    newX +=  (speedX * 2);
                    break;
            }
        }
        // Update boat position
        position.setX(Math.round(Math.max(0, Math.min(Gdx.graphics.getWidth() - image.getWidth()/2, newX))));

    }

    public void updateY(float delta) {
        timeTicker++;
        // Update boat speed
        speedY = getCurrentSpeed();

        // Boat cannot go higher than the middle of the screen
        if (position.getY() > Gdx.graphics.getHeight()/6) {
            position.setY(Gdx.graphics.getHeight()/6);
        }
        else {
            // Update boat position
            position.setY((int) Math.round( position.getY() + speedY));
        }
    }

    /**
     * Calculate the current speed of the boat (Speed algorithm)
     * @return the current speed of the boat between -2.5 and 2.5
     */
    public double getCurrentSpeed() {
        double accelerationWeight = 0;
        double fatigueWeight = -0.6;
        double extraSpeed = 0;

        if (accelerating) {
            accelerationWeight = 0.5;
        }

        if (timeTicker % 10 == 0) {
            extraSpeed = 0.5 * speedFactor;
        }

        // Personnal note : max new speed will be 2.5 and min -2.5
        double newSpeed =  extraSpeed + accelerationWeight * this.acceleration + fatigueWeight * this.fatigue;
        return newSpeed;
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
     * Get the speed of the boat horizontally
     * @return the speed of the boat horizontally
     */
    public int getSpeedX() {
        return speedX;
    }

    /**
     * Get the speed of the boat vertically
     * @return the speed of the boat vertically
     */
    public double getSpeedY() {
        return speedY;
    }

    /**
     * Get the position of the boat
     * @return the position of the boat
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get the speedFactor of the boat
     * @return the speedFactor of the boat
     */
    public int getSpeedFactor() {
        return speedFactor;
    }

    /**
     * Get the acceleration of the boat
     * @return the acceleration of the boat
     */
    public int getAcceleration() {
        return acceleration;
    }

    /**
     * Get the robustness of the boat
     * @return the robustness of the boat
     */
    public int getRobustness() {
        return robustness;
    }

    /**
     * Get the momentum of the boat
     * @return the momentum of the boat
     */
    public int getMomentum() {
        return momentum;
    }

    /**
     * Get the fatigue of the boat
     * @return the fatigue of the boat
     */
    public int getFatigue() {
        return fatigue;
    }
}
