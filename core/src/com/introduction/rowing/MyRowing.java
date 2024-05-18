package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.util.ArrayList;
import java.util.Iterator;

import static com.introduction.rowing.Constants.*;

public class MyRowing extends ApplicationAdapter {
    SpriteBatch batch;
    GameState currentState;
    Texture lobbyImage;
    TextureRegion[] water;
    TextureRegion[] shopBackground;
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
    ArrayList<Boat> boatsPosition = new ArrayList<Boat>();
    FinishLine finishline;
    Texture finishLineTexture;

    GameInputProcessor gameInputProcessor;
    LobbyInputProcessor lobbyInputProcessor;
    ShopInputProcessor shopInputProcessor;
    ScreenViewport viewport;
    Stage stage;
    private DataManager dataManager;
    int currentShopBoatIndex = 0;
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;


    @Override
    public void create() {
        batch = new SpriteBatch();
        lobbyImage = new Texture("main-lobby.jpeg");
        boatPicture = new Texture("boat-top-view-2.png");
        accelerationBarRectangle = new Texture("accelerationBarRectangle.png");
        accelerationBarBackground = new Texture("acceleration_bar_background.png");

        // Load the custom font using FreeTypeFontGenerator
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Zanden.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);

        currentState = GameState.LOBBY;
        gameInputProcessor = new GameInputProcessor();
        lobbyInputProcessor = new LobbyInputProcessor();
        shopInputProcessor = new ShopInputProcessor(this);

        // Water GIF setup
        water = new TextureRegion[5];
        for (int i = 0; i < water.length; i++)
            water[i] = new TextureRegion(new Texture("water-frames//frame_" + i + "_delay-0.1s.png"));

        // Shop Background GIF
        shopBackground = new TextureRegion[6];
        for (int i = 0; i < shopBackground.length; i++)
            shopBackground[i] = new TextureRegion(new Texture("shop-background//frame_" + i + "_delay-0.1s.png"));

        // Initialize the stage and viewport
        viewport = new ScreenViewport();
        stage = new Stage(viewport, batch);

        dataManager = new DataManager();

    }

    public void createNewGame() {
        finishLineTexture = new Texture("arts0587-02_0.png");
        finishline = new FinishLine(new Position(0, Gdx.graphics.getHeight()), Gdx.graphics.getWidth(), 100, finishLineTexture);
        lanes = new Lane[Constants.NUMBER_OF_LANES];
        int laneWidth = Constants.WINDOW_WIDTH / Constants.NUMBER_OF_LANES;
        int currentLeftBoundary = 0;
        for (int i = 0; i < Constants.NUMBER_OF_LANES; i++) {
            Position startingPosition = new Position(currentLeftBoundary + (laneWidth / 2), -230);
            if (i == 0) {
                lanes[i] = new Lane(new Boat(startingPosition, boatPicture, true, 5, 3, 5, 2, 3, 1, gameInputProcessor), currentLeftBoundary);
            } else {
                lanes[i] = new Lane(new Boat(startingPosition, boatPicture, false, 5, 5, 5, 5, 5, 5, null), currentLeftBoundary);
            }
            currentLeftBoundary += laneWidth;
        }
    }

    public void resetGame() {
        boatsPosition.clear();
        accelerationLevel = 0;
        stateAccelerating = false;
        stateTime = 0;
        createNewGame();
        System.out.println("Game reset");
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
                font.draw(batch, "Money balance: "+ dataManager.getBalance() , 150, 150);
                break;
            case PLAY_GAME:
                Gdx.input.setInputProcessor(gameInputProcessor);
                renderGame();
                break;
            case PLAY_MINI_GAME:
                renderMiniGame();
                break;
            case ENTER_SHOP:
                Gdx.input.setInputProcessor(shopInputProcessor);
                renderShop();
                break;
            default:
                break;
        }
        batch.end();
    }

    private void renderGame() {
        if (lanes == null) {
            System.out.println("Creating new game");
            createNewGame();
        }
        if (boatsPosition.size() == lanes.length) {
            System.out.println("Game is finished winner is: " + boatsPosition.get(0));
        }
        // Water flow (GIF)
        stateTime += Gdx.graphics.getDeltaTime();
        int currentFrameIndex = (int) (stateTime / frameDuration) % water.length;
        batch.draw(water[currentFrameIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //boat movement & obstacle spawning
        if (stateTime > 3) {
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
                    System.out.println("Boat " + currentBoat + " has crossed the finish line");
                    boatsPosition.add(currentBoat);
                }
                if (boatsPosition.size() == lanes.length) {
                    System.out.println("Game is finished winner is: " + boatsPosition.get(0));
                    InputProcessor.setGameState(GameState.LOBBY);
                    resetGame();
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
    }

    private void renderMiniGame() {
        // Render the mini-game
    }

    private void renderShop() {
        ShopBoat shopBoat = dataManager.boats.get(currentShopBoatIndex);

        // Background GIF
        stateTime += Gdx.graphics.getDeltaTime();
        int currentFrameIndex = (int) (stateTime / frameDuration) % 6;
        batch.draw(shopBackground[currentFrameIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Texture boatTexture = new Texture(Gdx.files.internal("boats/" + shopBoat.getImageName()));
        batch.draw(boatTexture, 0, 0, 200, 200);
        font.draw(batch, "Boat Name: " + shopBoat.getName(), 250, 1000);
        font.draw(batch, "Speed Factor: " + shopBoat.getSpeedFactor(), 250, 800);
        font.draw(batch, "Acceleration: " + shopBoat.getAcceleration(), 250, 700);
        font.draw(batch, "Robustness: " + shopBoat.getRobustness(), 250, 600);
        font.draw(batch, "Maneuverability: " + shopBoat.getManeuverability(), 250, 500);
        font.draw(batch, "Momentum Factor: " + shopBoat.getMomentumFactor(), 250, 400);
        font.draw(batch, "Fatigue: " + shopBoat.getFatigue(), 250, 300);
        if (!shopBoat.isUnlocked()) {
            font.draw(batch, "Price: " + shopBoat.getPrice(), 250, 200);
        } else {
            font.draw(batch, "Unlocked", 250, 200);
            font.draw(batch, "Selected: " + shopBoat.isSelected(), 250, 100);
        }
    }

    public void nextBoat() {
        currentShopBoatIndex = (currentShopBoatIndex + 1) % dataManager.boats.size();
    }

    public void previousBoat() {
        currentShopBoatIndex = (currentShopBoatIndex - 1 + dataManager.boats.size()) % dataManager.boats.size();
    }

    public void buyOrSelectBoat() {
        ShopBoat shopBoat = dataManager.boats.get(currentShopBoatIndex);
        if (!shopBoat.isUnlocked() && dataManager.getBalance() >= shopBoat.getPrice()) {
            shopBoat.setSelected(true);
            dataManager.setBalance(dataManager.getBalance() - shopBoat.getPrice());
        } else if (shopBoat.isSelected()) {
            for (ShopBoat b : dataManager.boats) {
                b.setSelected(false);
            }
            shopBoat.setSelected(true);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        boatPicture.dispose();
        lobbyImage.dispose();
        accelerationBarRectangle.dispose();
        accelerationBarBackground.dispose();
        finishLineTexture.dispose();
        generator.dispose();
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
