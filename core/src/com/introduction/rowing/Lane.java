package com.introduction.rowing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static com.introduction.rowing.Constants.*;
import java.util.ArrayList;
import java.util.Random;

public class Lane {
    // This class should contain the logic for the lanes, each lane contains a boat and a list of obstacles which are an Entity

    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    Boat boat;
    int leftBoundary;
    private double newObstacleReady = 0;

    public Lane(Boat boat, int leftBoundary) {
        this.boat = boat;
        this.leftBoundary = leftBoundary;
    }

    public Boat getBoat() {
        return boat;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getLeftBoundary() {
        return leftBoundary;
    }

    /**
     * Method to check if the lane is ready to spawn an obstacle
     * @param delta
     * @param numberLegs
     */
    public boolean spawnObstacleReady(float delta, int numberLegs) {
        Random rnd3 = new Random();
        double baseRechargeTime = 0.1 + ((double) numberLegs /20);
        int temps = rnd3.nextInt(5) + 1;

        double rechargeTime = baseRechargeTime * temps;
        newObstacleReady += rechargeTime * delta;
        if (newObstacleReady >= 0.8) {
            newObstacleReady = 0;
            return true;
        }
        return false;
    }

    public void spawnObstacles() {
        Random rnd = new Random();
        int random = rnd.nextInt(4);
        int LANE_WIDTH = WINDOW_WIDTH / NUMBER_OF_LANES;
        int randomWidth = rnd.nextInt(LANE_WIDTH) - 50;
        Texture gees = new Texture("obstacles/duck.png");
        Texture branch = new Texture("obstacles/log.png");
        Texture rock = new Texture("obstacles/rock.png");

        if (random == 0) {
            obstacles.add(ObstacleFactory.createObstacle("Gees", new Position(leftBoundary + randomWidth, Gdx.graphics.getHeight() + gees.getHeight()), 100, 100, gees));
        } else if (random == 1) {
            obstacles.add(ObstacleFactory.createObstacle("Rock", new Position(leftBoundary + randomWidth,  Gdx.graphics.getHeight()-50+rock.getHeight()), 100, 100, rock));
        } else {
            obstacles.add(ObstacleFactory.createObstacle("Branch", new Position(leftBoundary + randomWidth,  Gdx.graphics.getHeight()-50+branch.getHeight()), 100, 100, branch));
        }
    }

    public void collision(MyRowing myRowing){
        for (Obstacle obstacle : obstacles) {
            if (boat.getBounds().intersects(obstacle.getBounds()) && !boat.isInvulnerable()) {
                obstacles.remove(obstacle);
                boat.setPosition(boat.getPosition().getX(), Math.max(boat.getHeight() / 2, boat.getPosition().getY() - obstacle.pushBack));
                boat.resetNumberOfAvoidedObstacles();
                boat.deactivateMomemtumPowerup();
                boat.damageBoat(obstacle.getDamage());
                myRowing.crash.play();
                break;
            }
            else if (obstacle.getPosition().getY() + obstacle.getHeight() < 0) {
                obstacles.remove(obstacle);
                boat.increaseNumberOfAvoidedObstacles();
                break;
            }
        }
    }
}
