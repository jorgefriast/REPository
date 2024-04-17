package com.introduction.rowing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Random;

public class Lane {
    // This class should contain the logic for the lanes, each lanes contains a boat and a list of obstacles which are an Entity

    ArrayList<Entity> obstacles = new ArrayList<Entity>();
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

    public ArrayList<Entity> getObstacles() {
        return obstacles;
    }

    public int getLeftBoundary() {
        return leftBoundary;
    }

    //create a method to check if its time to add an obstacle
    public boolean spawnObstacleReady(float delta) {
        Random rnd3 = new Random();
        double tempsRecharge = 0.2;
        int temps = rnd3.nextInt(5);
        tempsRecharge = tempsRecharge * temps;
        newObstacleReady += tempsRecharge * delta;
        if (newObstacleReady >= 0.8) {
            newObstacleReady = 0;
            return true;
        }
        return false;
    }

    public void spawnObstacles() {
        Random rnd = new Random();
        int random = rnd.nextInt(3);
        int LANE_WIDTH = Gdx.graphics.getWidth() / 4;
        int randomWidth = rnd.nextInt(LANE_WIDTH) - 50;
        Texture gees = new Texture("geeses-bg.png");
        Texture ducks = new Texture("duck-bg.png");
        Texture branch = new Texture("wood.png");

        if (random == 0) {
            obstacles.add(new Gees(new Position(leftBoundary + randomWidth, Gdx.graphics.getHeight()), 100, 100, gees));
        } else if (random == 1) {
            obstacles.add(new Ducks(new Position(leftBoundary + randomWidth,  Gdx.graphics.getHeight()-50), 100, 100, ducks));
        } else {
            obstacles.add(new Branch(new Position(leftBoundary + randomWidth,  Gdx.graphics.getHeight()-50), 100, 100, branch));
        }
    }

    public void collision(){
        for (Entity obstacle : obstacles) {
            if (boat.getBounds().intersects(obstacle.getBounds())) {
                obstacles.remove(obstacle);
                boat.setPosition(boat.getPosition().getX(), boat.getPosition().getY() - 50);
                break;
            }
        }
    }
}
