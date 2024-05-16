package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;

import static com.introduction.rowing.Constants.*;

public class MyRowing extends ApplicationAdapter {
    SpriteBatch batch;
    GameState currentState;
    Texture lobbyImage;
    TextureRegion[] water;
    BitmapFont font;
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
    ArrayList<Boat> boatsPosition = new ArrayList<>();
    FinishLine finishline;
    Texture finishLineTexture;

    GameInputProcessor gameInputProcessor;
    LobbyInputProcessor lobbyInputProcessor;

    @Override
    public void create() {
        batch = new SpriteBatch();
        lobbyImage = new Texture("main-lobby.jpeg");
        boatPicture = new Texture("boat-top-view-2.png");
        accelerationBarRectangle = new Texture("accelerationBarRectangle.png");
        accelerationBarBackground = new Texture("acceleration_bar_background.png");
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);
        finishLineTexture = new Texture("arts0587-02_0.png");
        finishline = new FinishLine(new Position(0, Gdx.graphics.getHeight()), Gdx.graphics.getWidth(), 100, finishLineTexture);
        currentState = GameState.LOBBY;

        gameInputProcessor = new GameInputProcessor();
        lobbyInputProcessor = new LobbyInputProcessor();

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
                lanes[i] = new Lane(new Boat(startingPosition, boatPicture, true, 1, 3, 5, 2, 1, 1, gameInputProcessor), currentLeftBoundary);
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
        currentState = InputProcessor.getGameState();
        switch (currentState) {
            case LOBBY:
                batch.draw(lobbyImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                Gdx.input.setInputProcessor(lobbyInputProcessor);
                break;
            case PLAY_GAME:
                Gdx.input.setInputProcessor(gameInputProcessor);
                renderGame();
                break;
            case PLAY_MINI_GAME:
                renderMiniGame();
                break;
            case ENTER_SHOP:
                renderShop();
                break;
            default:
                break;
        }
        batch.end();
    }

    private void renderGame() {
        currentState = GameState.PLAY_GAME;
        if (boatsPosition.size() == lanes.length) {
            System.out.println("Game is finished winner is: " + boatsPosition.get(0));
        }
        // Water flow (GIF)
        stateTime += Gdx.graphics.getDeltaTime();
        int currentFrameIndex = (int) (stateTime / frameDuration) % water.length;
        batch.draw(water[currentFrameIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //boat movement & obstacle spawning
        if (stateTime > 1) {
            finishLine();
        }
        boolean crossed;
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

            crossed = checkFinishLineCrossed(lane.getBoat());
            if (crossed) {
                if (!boatsPosition.contains(currentBoat)) {
                    boatsPosition.add(currentBoat);
                }
                if (boatsPosition.size() == lanes.length) {
                    InputProcessor.setGameState(GameState.LOBBY);
                }
            }
            //make the obstacles move
            ArrayList<Obstacle> obstacles = lane.getObstacles();
            Iterator<Obstacle> iterator = obstacles.iterator();
            while (iterator.hasNext()) {
                Obstacle obstacle = iterator.next();
                if (obstacle instanceof Gees) {
                    float movementSpeed = 2;
                    float deltaX = MathUtils.sinDeg(obstacle.getPosition().getY() * 5) * 5;
                    obstacle.adjustPosition(deltaX, -movementSpeed); // Move left to right with a downward speed
                } else if (obstacle instanceof FinishLine) {
                    obstacle.adjustPosition(0, -3);
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
        font.draw(batch, ACCELERATION_BAR_TEXT, 1400, 900);
        batch.draw(accelerationBarRectangle, PBR_X_POS, PBR_Y_POS, accelerationBarRectangleWidth, accelerationBarRectangleHeight);
        batch.draw(accelerationBarBackground, PBB_X_POS, PBB_Y_POS, accelerationBarBackgroundWidth, accelerationBarBackgroundHeight);

        for (Lane lane : lanes) {
            Boat currentBoat = lane.getBoat();
            if (currentBoat.getIsPlayer()) {
                double fatiguePercentage = currentBoat.getFatigueEffect();
                String fatigueText = "Fatigue Effect: " + (int) (fatiguePercentage * 100) + "%";
                String boatHealthText = "Boat Health: " + currentBoat.getBoatHealth();
                String avoidedObstaclesText = "Avoided Obstacles: " + currentBoat.getNumberOfAvoidedObstacles();
                String momentumText = "Momentum: " + currentBoat.getCurrentMomentum();
                font.draw(batch, fatigueText, 1400, 750);
                font.draw(batch, boatHealthText, 1400, 700);
                font.draw(batch, avoidedObstaclesText, 1400, 650);
                font.draw(batch, momentumText, 1400, 600);
            }
        }
        batch.end();
    }

    private void renderMiniGame() {
        // Render the mini-game
    }

    private void renderShop() {
        // Render the shop

    }

    @Override
    public void dispose() {
        batch.dispose();
        boatPicture.dispose();
        lobbyImage.dispose();
        progressionBarRectangle.dispose();
        progressionBarBackground.dispose();
        finishLineTexture.dispose();
        for (TextureRegion textureRegion : water) {
            textureRegion.getTexture().dispose();
        }

    }

    /**
     * Method to make the finish line appear at the end of the game
     */
    public void finishLine() {
        batch.draw(finishline.getImage(), finishline.getPosition().getX(), finishline.getPosition().getY(), finishline.getWidth(), finishline.getHeight());
        finishline.adjustPosition(0, -3);
    }

    /**
     * Method to check if the boat has crossed the finish line
     */
    public boolean checkFinishLineCrossed(Boat boat) {
        return boat.getBounds().intersects(finishline.getBounds());
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
