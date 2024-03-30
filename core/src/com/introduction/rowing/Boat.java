package com.introduction.rowing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Boat extends Entity{
    private int speedX;
    private int speedY;
    private final int lane;
    private final MyInputProcessor inputProcessor;

    public Boat(int speedX, int speedY, int lane, Position position, Texture image) {
        super(position, image.getWidth(), image.getHeight(), image);
        this.speedX = speedX;
        this.speedY = speedY;
        this.lane = lane;
        this.inputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    public void update(float delta) {
        // Check if boat is moving based on input
        boolean moving = inputProcessor.moving;
        int direction = inputProcessor.direction;

        // Adjust boat position based on input direction
        float newX = position.getX();
        float newY = position.getY();
        if (moving) {
            switch (direction) {
                case 0: // Up
                    newY += (speedY * 2);
                    break;
                case 1: // Left
                    newX -= (speedX * 2);
                    break;
                case 2: // Down
                    newY -= (speedY * 2);
                    break;
                case 3: // Right
                    newX +=  (speedX * 2);
                    break;
            }
        }
        System.out.println("new X : " + newX + "new Y : "+ newY);
        // Update boat position
        position.setX(Math.round(Math.max(0, Math.min(1920 - image.getWidth(), newX))));
        System.out.println("width : "+ image.getWidth() + " height: "+ image.getHeight());
        position.setY(Math.round(Math.max(0, Math.min(1080 - image.getHeight(), newY))));
    }
    public int deplacementBoatX(boolean moving, int direction, int delta) {

        if (moving) {
            switch (direction) {
                case 1:
                    //left
                    if (position.getX() <= 0) {
                        this.position.setX(0);
                    } else {
                        this.position.setX((int) Math.round(this.position.getX() - 0.5 * this.speedX));
                    }
                    break;
                case 3:
                    //right
                    if (this.position.getX() >= 1000 - image.getHeight()) {
                        this.position.setX(1000 - image.getHeight());
                    } else {
                        this.position.setX((int) Math.round(this.position.getX() + 0.5 * this.speedX));
                    }
                    break;
            }
        }
        return this.position.getX();
    }

    public int deplacementBoatY(boolean moving, int direction, int delta) {

        if (moving) {
            switch (direction) {
                case 0:
                    //up
                    if (this.position.getY() <= 0) {
                        this.position.setY(0);
                    } else {
                        this.position.setY((int) Math.round(this.position.getY() - 0.5 * this.speedY));
                    }
                    break;
                case 2:
                    //down
                    if (this.position.getY() >= 1500 - image.getHeight()) {
                        this.position.setY(1500 - image.getHeight());
                    } else{
                        this.position.setY((int) Math.round(this.position.getY() + 0.5 * this.speedY));
                    }
                    break;
            }
        }
        return this.position.getY();
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
     * Get the lane of the boat
     * @return the lane of the boat
     */
    public int getLane() {
        return lane;
    }

    /**
     * Get the position of the boat
     * @return the position of the boat
     */
    public Position getPosition() {
        return position;
    }

}
