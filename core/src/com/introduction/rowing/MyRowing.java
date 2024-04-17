package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import sun.tools.jconsole.JConsole;

import java.util.ArrayList;
import java.util.Iterator;

public class MyRowing extends ApplicationAdapter {
    SpriteBatch batch;
    TextureRegion[] water;

    Lane[] lanes;

    float stateTime = 0;
    float frameDuration = 0.1f; // Adjust the frame duration as needed
    Texture boatPicture;


    @Override
    public void create() {
        batch = new SpriteBatch();
        boatPicture = new Texture("boat-top-view-2.png");

        // Water GIF setup
        water = new TextureRegion[5];
        for (int i = 0; i < water.length; i++)
            water[i] = new TextureRegion(new Texture("water-frames//frame_" + i + "_delay-0.1s.gif"));

        lanes = new Lane[Constants.NUMBER_OF_LANES];
        int laneWidth = Constants.WINDOW_WIDTH / Constants.NUMBER_OF_LANES;
        int currentLeftBoundary = 0;
        for (int i = 0; i < Constants.NUMBER_OF_LANES; i++) {
            Position startingPosition = new Position(currentLeftBoundary + (laneWidth / 2), -230);
            if (i == 0) {
                lanes[i] = new Lane(new Boat(startingPosition, boatPicture, true, 1, 3, 5, 2, 0, 0), currentLeftBoundary);
            } else {
                lanes[i] = new Lane(new Boat(startingPosition, boatPicture, false, 1, 3, 5, 1, 0, 0), currentLeftBoundary);
            }
            currentLeftBoundary += laneWidth;
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 1, 0, 1);
        batch.begin();
        // Water flow (GIF)
        stateTime += Gdx.graphics.getDeltaTime();
        int currentFrameIndex = (int) (stateTime / frameDuration) % water.length;
        batch.draw(water[currentFrameIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //boat movement & obstacle spawning
        for (Lane lane : lanes) {
            Boat currentBoat = lane.getBoat();
            batch.draw(currentBoat.getImage(), currentBoat.getPosition().getX(), currentBoat.getPosition().getY(), currentBoat.getWidth(), currentBoat.getHeight());
            if (currentBoat.isPlayer()) {
                currentBoat.updateKeys(Gdx.graphics.getDeltaTime(), lane.getLeftBoundary());
            } else {
                currentBoat.avoidObstacles(lane.getObstacles(), lane.getLeftBoundary());
            }
//            currentBoat.updateKeys(Gdx.graphics.getDeltaTime());
            //update boat's y position every 5 frames
            if (currentFrameIndex % 5 == 0) {
                currentBoat.updateY(Gdx.graphics.getDeltaTime());
            }

            if (lane.spawnObstacleReady(Gdx.graphics.getDeltaTime())) {
                lane.spawnObstacles();
            }
            lane.collision();
            //make the obstacles move
            ArrayList<Entity> obstacles = lane.getObstacles();
            Iterator<Entity> iterator = obstacles.iterator();
            while (iterator.hasNext()) {
                Entity obstacle = iterator.next();
                if (obstacle instanceof Gees) {
                    float movementSpeed = 2;
                    float deltaX = MathUtils.sinDeg(obstacle.getPosition().getY() * 5) * 5;
                    obstacle.adjustPosition(deltaX, -movementSpeed); // Move left to right with a downward speed
                } else {
                    obstacle.adjustPosition(0, -5);
                }
//                obstacle.adjustPosition((float) 0, (float) (-5));
                batch.draw(obstacle.getImage(), obstacle.getPosition().getX(), obstacle.getPosition().getY(), obstacle.getWidth(), obstacle.getHeight());

                // Remove obstacle if it's below the screen
                if (obstacle.getPosition().getY() + obstacle.getHeight() < 0) {
                    iterator.remove();
                }
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        boatPicture.dispose();
    }
}
