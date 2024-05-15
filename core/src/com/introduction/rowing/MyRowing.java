package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import sun.tools.jconsole.JConsole;

import java.util.ArrayList;
import java.util.Iterator;

import static com.introduction.rowing.Constants.*;

public class MyRowing extends ApplicationAdapter {
    SpriteBatch batch;
    TextureRegion[] water;
    Lane[] lanes;
    float stateTime = 0;
    float frameDuration = 0.1f;
    Texture boatPicture;
    Texture accelerationBarRectangle;
    float accelerationBarRectangleWidth = 204;
    float accelerationBarRectangleHeight = 54;
    Texture accelerationBarBackground;
    float accelerationBarBackgroundWidth = 196;
    float accelerationBarBackgroundHeight = 46;
    float accelerationLevel = 0;
    boolean stateAccelerating = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        boatPicture = new Texture("boat-top-view-2.png");
        accelerationBarRectangle = new Texture("accelerationBarRectangle.png");
        accelerationBarBackground = new Texture("acceleration_bar_background.png");

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
                lanes[i] = new Lane(new Boat(startingPosition, boatPicture, true, 1, 1, 5, 1, 1, 1), currentLeftBoundary);
            } else {
                lanes[i] = new Lane(new Boat(startingPosition, boatPicture, false, 5, 5, 5, 5, 5, 5), currentLeftBoundary);
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
            if (currentBoat.getIsPlayer()) {
                currentBoat.updateKeys(Gdx.graphics.getDeltaTime(), lane.getLeftBoundary());
            } else {
                currentBoat.avoidObstacles(lane.getObstacles(), lane.getLeftBoundary());
            }

            // Decrease acceleration level
            if (currentBoat.getIsPlayer() && currentBoat.getAccelerating()) {
                stateAccelerating = true;
                decreaseAcceleration(Gdx.graphics.getDeltaTime(), currentBoat);
            }

            // Increase acceleration level
            if (currentFrameIndex % 5 == 0 && accelerationLevel < FULL_ACCELERATION_BAR && !stateAccelerating) {
                increaseAcceleration(Gdx.graphics.getDeltaTime(), currentBoat);
            }

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
        batch.draw(accelerationBarRectangle, PBR_X_POS, PBR_Y_POS, accelerationBarRectangleWidth, accelerationBarRectangleHeight);
        batch.draw(accelerationBarBackground, PBB_X_POS, PBB_Y_POS, accelerationBarBackgroundWidth, accelerationBarBackgroundHeight);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        boatPicture.dispose();
    }

    private void increaseAcceleration(float deltaTime, Boat boat) {
        accelerationLevel += ACCELERATION_BAR_INCREASE_RATE * deltaTime;
        if (accelerationLevel >= FULL_ACCELERATION_BAR - 1) {
            boat.setIsAcceleratorAvailable(true);
        }
        updateAccelerationBar();
    }

    private void decreaseAcceleration(float delta, Boat boat) {
        float decreaseRate = FULL_ACCELERATION_BAR;
        accelerationLevel -= decreaseRate * delta;
        if (accelerationLevel <= 0) {
            accelerationLevel = 0;
            boat.setIsAcceleratorAvailable(false);
            boat.setAccelerating(false);
            stateAccelerating = false;
        }
        updateAccelerationBar();
        boat.setAccelerating(false);
    }

    private void updateAccelerationBar() {
        float ratio = accelerationLevel / FULL_ACCELERATION_BAR;
        accelerationBarBackgroundWidth = 2 * FULL_ACCELERATION_BAR * ratio - PROGRESSION_BAR_OFFSET;
        if (accelerationBarBackgroundWidth < 0) {
            accelerationBarBackgroundWidth = 0;
        }
    }
}
