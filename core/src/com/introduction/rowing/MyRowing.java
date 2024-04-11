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
    float frameDuration = 0.1f; // Adjust the frame duration as needed
    Texture boatPicture;
    Texture knob;
    float knobWidth = 204;
    float knobHeight = 54;
    Texture background;
    float backgroundWidth = 196;
    float backgroundHeight = 46;
    float accelerationLevel = 0;
    boolean isHoldIncreasingAccelerationLevel = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        boatPicture = new Texture("boat-top-view-2.png");
        knob = new Texture("knob.png");
        background = new Texture("acceleration_bar_background.png");

        // Water GIF setup
        water = new TextureRegion[5];
        for (int i = 0; i < water.length; i++)
            water[i] = new TextureRegion(new Texture("water-frames//frame_" + i + "_delay-0.1s.gif"));

        lanes = new Lane[Constants.NUMBER_OF_LANES];
        int laneWidth = Constants.WINDOW_WIDTH / Constants.NUMBER_OF_LANES;
        int currentLeftBoundary = 0;
        for (int i = 0; i < Constants.NUMBER_OF_LANES; i++) {
			Position startingPosition = new Position(currentLeftBoundary + (laneWidth / 2), -230);
			if (i == 2) {
				lanes[i] = new Lane(new Boat(startingPosition, boatPicture, true, 1, 3, 5, 2, 0, 0), currentLeftBoundary);
			}else {
				lanes[i] = new Lane(new Boat(startingPosition, boatPicture, false, 1, 3, 5, 1, 0, 0), currentLeftBoundary);
			}
			currentLeftBoundary += laneWidth;
        }
    }

	@Override
	public void render () {
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
            currentBoat.updateKeys(Gdx.graphics.getDeltaTime());

            // Decrease acceleration level
            if (currentBoat.getIsPlayer() && currentBoat.getAccelerating()) {
                decreaseAcceleration(Gdx.graphics.getDeltaTime(), currentBoat);
            }

            // Increase acceleration level
            if (currentFrameIndex % 5 == 0 && accelerationLevel < 100) {
                increaseAcceleration(Gdx.graphics.getDeltaTime(), currentBoat);

            }

            //update boat's y position every 5 frames
            if(currentFrameIndex % 5 == 0) {
                currentBoat.updateY(Gdx.graphics.getDeltaTime());
            }

            if (lane.spawnObstacleReady(Gdx.graphics.getDeltaTime())) {
                lane.spawnObstacles();
            }
            ArrayList<Entity> obstacles = lane.getObstacles();
            Iterator<Entity> iterator = obstacles.iterator();
            while (iterator.hasNext()) {
                Entity obstacle = iterator.next();
                obstacle.adjustPosition((float) 0, (float) (-5));
                batch.draw(obstacle.getImage(), obstacle.getPosition().getX(), obstacle.getPosition().getY(), obstacle.getWidth(), obstacle.getHeight());

                // Remove obstacle if it's below the screen
                if (obstacle.getPosition().getY() + obstacle.getHeight() < 0) {
                    iterator.remove();
                }
            }
        }
        //Progression bar
        batch.draw(knob, 1450, 800, knobWidth, knobHeight);
        batch.draw(background, 1454, 804, backgroundWidth, backgroundHeight);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        boatPicture.dispose();
    }

    private void increaseAcceleration(float deltaTime, Boat boat) {
        accelerationLevel += ACCELERATION_BAR_INCREASE_RATE * deltaTime;
        if (accelerationLevel >= 99) {
            boat.setIsAcceleratorAvailable(true);
        }
        updateAccelerationBar();
    }

    private void decreaseAcceleration(float delta, Boat boat) {
        // Decrease acceleration level over time
        float decreaseRate = 100; // Amount of acceleration decrease per second
        accelerationLevel -= decreaseRate * delta;
        if (accelerationLevel <= 0) {
            accelerationLevel = 0;
            boat.setIsAcceleratorAvailable(false);
            boat.setAccelerating(false);
        }
        updateAccelerationBar();
        boat.setAccelerating(false);
    }

    private void updateAccelerationBar() {
        float ratio = accelerationLevel / 100f;
        backgroundWidth = 200 * ratio - 4;
        if (backgroundWidth < 0) {
            backgroundWidth = 0;
        }
    }
}
