package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import static com.introduction.rowing.Constants.*;

public class MyRowing extends ApplicationAdapter {
    SpriteBatch batch;
    GameState currentState;
    MiniGameState miniGameState;
    Texture lobbyImage;
    TextureRegion[] water;
    Texture shopBackground;
    BitmapFont font;
    BitmapFont fontWhite;
    Lane[] lanes;
    float stateTime = 0;
    float frameDuration = 0.1f;
    Texture boatPicture;
    Texture accelerationBarRectangle;
    Texture correct;
    Texture error;
    Texture horizontalScroll;
    Texture verticalScroll;
    Texture panel;
    Texture rightArrow;
    Texture podiumTexture;
    Texture upperArrow;
    Texture bottomArrow;
    Texture leftArrow;
    Texture shopDescription;
    float accelerationBarRectangleWidth = 204;
    float accelerationBarRectangleHeight = 54;
    Texture accelerationBarBackground;
    float accelerationBarBackgroundWidth = 196;
    float accelerationBarBackgroundHeight = 46;
    float accelerationLevel = 0;
    boolean stateAccelerating = false;
    Texture progressBarRectangle;
    Texture progressBarBackground;
    float progressBarRectangleWidth = WINDOW_WIDTH / 2;
    float progressBarRectangleHeight = 54;
    float progressBarBackgroundWidth = WINDOW_WIDTH / 2;
    float progressBarBackgroundHeight = 46;
    float progressLevel = 0;
    float finishLineY;
    float invulnerabilityTimer = 0;
    Powerup availablePowerup;
    ArrayList<Boat> boatsPosition = new ArrayList<>();
    Texture laneDividerTexture;
    ArrayList<LaneDivider> laneDividers;
    ArrayList<Integer> positionsRecord = new ArrayList<>();
    FinishLine finishline;
    Texture keysTutorialTexture;
    Texture loseScreenTexture;
    Texture powerupSlot;
    Texture UITutorialTexture;
    Texture finishLineTexture;
    Texture tiles;
    Texture dragonHead;
    Texture goldMedal;
    Texture silverMedal;
    Texture bronzeMedal;
    Texture fourthMedal;
    DragonHead dragonPlayer;
    CountdownTimer countdownTimer;
    int randomObstacle;
    ArrayList<Entity> itemTiles;
    Texture sumScreenMiniGame;
    int money;

    GameInputProcessor gameInputProcessor;
    LoseScreenInputProcessor loseScreenInputProcessor;
    TutorialInputProcessor tutorialInputProcessor;
    LobbyInputProcessor lobbyInputProcessor;
    ShopInputProcessor shopInputProcessor;
    MiniGameInputProcessor miniGameInputProcessor;
    WinnerScreenInputProcessor winnerScreenInputProcessor;
    ScreenViewport viewport;
    Stage stage;
    private DataManager dataManager;
    int currentShopBoatIndex = 0;
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;
    FreeTypeFontParameter parameter2;

    GlyphLayout layoutCongrats;

    Sound crash;
    Sound lobbyMusic;
    Sound gameMusic;
    Sound shopMusic;
    Sound win;
    Sound lose;

    int numberLeg = 0;
    int minigameStage = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        crash = Gdx.audio.newSound(Gdx.files.local("music/crash.mp3"));
        lobbyMusic = Gdx.audio.newSound(Gdx.files.local("music/menu_theme.mp3"));
        gameMusic = Gdx.audio.newSound(Gdx.files.local("music/main_theme.mp3"));
        shopMusic = Gdx.audio.newSound(Gdx.files.local("music/shop_menu.mp3"));
        win = Gdx.audio.newSound(Gdx.files.local("music/win.mp3"));
        lose = Gdx.audio.newSound(Gdx.files.local("music/lose.mp3"));
        lobbyImage = new Texture("backgrounds/lobby.png");
        keysTutorialTexture = new Texture("backgrounds/tutorial.png");
        loseScreenTexture = new Texture("backgrounds/loose-screen.png");
        panel = new Texture("backgrounds/panel.png");
        UITutorialTexture = new Texture("backgrounds/ui-tutorial.png");
        shopBackground = new Texture("backgrounds/shop.png");
        horizontalScroll = new Texture("backgrounds/horizontal_scroll.png");
        verticalScroll = new Texture("backgrounds/vertical_scroll.png");
        rightArrow = new Texture("backgrounds/right_arrow.png");
        leftArrow = new Texture("backgrounds/left_arrow.png");
        upperArrow = new Texture("backgrounds/upper_arrow.png");
        bottomArrow = new Texture("backgrounds/bottom_arrow.png");
        shopDescription = new Texture("backgrounds/shop_description.png");
        boatPicture = new Texture("boats/saoko.png");
        accelerationBarRectangle = new Texture("accelerationBarRectangle.png");
        accelerationBarBackground = new Texture("acceleration_bar_background.png");
        progressBarRectangle = new Texture("accelerationBarRectangle.png");
        progressBarBackground = new Texture("progress_bar.png");
        laneDividerTexture = new Texture("backgrounds/lane-separator.png");
        podiumTexture = new Texture("backgrounds/podio.png");
        correct = new Texture("tick.png");
        error = new Texture("error.png");
        tiles = new Texture("tiles/tile.jpg");
        dragonHead = new Texture("powerups/dragon_head.png");
        sumScreenMiniGame = new Texture("shop-background/frame_1_delay-0.1s.png");
        goldMedal = new Texture("medals/gold.png");
        silverMedal = new Texture("medals/silver.png");
        bronzeMedal = new Texture("medals/bronze.png");
        fourthMedal = new Texture("medals/fourth.png");
        powerupSlot = new Texture("powerups/powerup-slot.png");

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);

        fontWhite = new BitmapFont();
        fontWhite.setColor(Color.RED);
        fontWhite.getData().setScale(3);

        layoutCongrats = new GlyphLayout();


        // Load the custom font using FreeTypeFontGenerator
        generator = new FreeTypeFontGenerator(Gdx.files.internal("JainiPurva-Regular.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);

        parameter2 = new FreeTypeFontParameter();
        parameter2.size = 64;
        parameter2.color = Color.RED;
        fontWhite = generator.generateFont(parameter2);

        currentState = GameState.LOBBY;

        miniGameState = MiniGameState.NOT_STARTED;

        gameInputProcessor = new GameInputProcessor(this);
        tutorialInputProcessor = new TutorialInputProcessor(this);
        loseScreenInputProcessor = new LoseScreenInputProcessor(this);
        lobbyInputProcessor = new LobbyInputProcessor(this);
        miniGameInputProcessor = new MiniGameInputProcessor(this);
        shopInputProcessor = new ShopInputProcessor(this);
        winnerScreenInputProcessor = new WinnerScreenInputProcessor(this);
        dragonPlayer = new DragonHead(new Position((Gdx.graphics.getWidth() - dragonHead.getWidth()/2) / 2, (Gdx.graphics.getHeight() - dragonHead.getHeight()/2) / 2 -50), dragonHead.getWidth() /6, dragonHead.getHeight()/6, dragonHead, miniGameInputProcessor);
        countdownTimer = new CountdownTimer(3);
        randomObstacle = 1;
        money = 0;

        laneDividers = new ArrayList<>();


        // Water GIF setup
        water = new TextureRegion[5];
        for (int i = 0; i < water.length; i++)
            water[i] = new TextureRegion(new Texture("water-frames//frame_" + i + "_delay-0.1s.png"));


        // Initialize the stage and viewport
        viewport = new ScreenViewport();
        stage = new Stage(viewport, batch);

        dataManager = new DataManager();

        for (int i = 0; i < NUMBER_OF_LANES; i++) {
            positionsRecord.add(0);
        }
    }

    public void createNewGame(GameInputProcessor inputProcessor, int numberLeg) {
        finishLineTexture = new Texture("arts0587-02_0.png");
        finishline = new FinishLine(new Position(0, Gdx.graphics.getHeight()), WINDOW_WIDTH, 100, finishLineTexture);
        miniGameState = MiniGameState.NOT_STARTED;
        lanes = new Lane[NUMBER_OF_LANES];
        int laneWidth = WINDOW_WIDTH / NUMBER_OF_LANES;
        int currentLeftBoundary = 0;
        int boatWidth = (int) ((WINDOW_WIDTH / NUMBER_OF_LANES) * BOAT_WIDTH_FRACTION);
        int boatHeight = (boatPicture.getHeight() * boatWidth) / boatPicture.getWidth();
        for (int i = 0; i < NUMBER_OF_LANES; i++) {
            float multiplier = numberLeg != NUMBER_OF_LEGS ? 1 : (positionsRecord.get(i) / ((float) NUMBER_OF_LEGS * (NUMBER_OF_LANES - 1)) + 1) / 2;
            System.out.println("MULTIPLIER " + multiplier);
            Position startingPosition = new Position(currentLeftBoundary + (laneWidth / 2), (int) (boatHeight * 0.5 * multiplier));
            if (i == 0) {
                lanes[i] = new Lane(new Boat(i, startingPosition, true, inputProcessor, dataManager.getSelectedBoat()), currentLeftBoundary);
            } else {
                Random random = new Random();
                int randomInt = random.nextInt(4);
                lanes[i] = new Lane(new Boat(i, startingPosition,  false, inputProcessor, dataManager.boats.get(randomInt)), currentLeftBoundary);
            }
            currentLeftBoundary += laneWidth;
        }
        for(int i = 1; i < NUMBER_OF_LANES; i++) {
            laneDividers.add(new LaneDivider(new Position((int) (i * ((float) WINDOW_WIDTH / NUMBER_OF_LANES)), 0), 10, laneDividerTexture.getHeight(), laneDividerTexture));
        }
        availablePowerup = dataManager.getPowerup(this);
    }

    public void resetGame() {
        boatsPosition.clear();
        accelerationLevel = 0;
        stateAccelerating = false;
        stateTime = 0;
        createNewGame(gameInputProcessor, numberLeg);
        lanes = null;
        laneDividers.clear();
        if (InputProcessor.getGameSubState() == GameSubState.FINAL_LEG) {
            dataManager.setPowerup(-1);
        }
        gameMusic.pause();
    }

    boolean lobbyEntered = false;
    public void renderLobby() {
        if (!lobbyEntered) {
            lobbyEntered = true;
            lobbyMusic.loop();
        }
        batch.draw(lobbyImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        font.draw(batch, " " + dataManager.getBalance() , (float) (WINDOW_WIDTH* 0.13), (float) (WINDOW_HEIGHT*0.11));
    }


    @Override
    public void render() {
        ScreenUtils.clear(0, 1, 0, 1);
        batch.begin();
        currentState = InputProcessor.getGameState();
        switch (currentState) {
            case LOBBY:
                enteredShop = false;
                enteredLoseScreen = false;
                shopMusic.pause();
                lose.pause();
                Gdx.input.setInputProcessor(lobbyInputProcessor);
                renderLobby();
                break;
            case PLAY_GAME:
                lobbyEntered = false;
                enteredShop = false;
                Gdx.input.setInputProcessor(gameInputProcessor);
                shopMusic.pause();
                lobbyMusic.pause();
                renderGame(gameInputProcessor);
                break;
            case WINNER:
                Gdx.input.setInputProcessor(winnerScreenInputProcessor);
                renderWinner();
                break;
            case PLAY_MINI_GAME:
                lobbyEntered = false;
                enteredShop = false;
                Gdx.input.setInputProcessor(miniGameInputProcessor);
                shopMusic.pause();
                lobbyMusic.pause();
                renderMiniGame();
                break;
            case ENTER_SHOP:
                Gdx.input.setInputProcessor(shopInputProcessor);
                lobbyEntered = false;
                lobbyMusic.pause();
                renderShop();
                break;
            case LOSE_SCREEN:
                Gdx.input.setInputProcessor(loseScreenInputProcessor);
                lobbyMusic.pause();
                shopMusic.pause();
                enteredShop = false;
                lobbyEntered = false;
                renderLostScreen();
                break;
            default:
                break;
        }
        batch.end();
    }

    private Texture getTexture(int index) {
        Texture correctMedal = null;
        String congrats = "";
        float textWidth = 0;
        float x = WINDOW_WIDTH;
        float y = (float) (WINDOW_HEIGHT * 0.8);

        switch (index) {
            case 0:
                //put a text congratulating the player in the center
                congrats = "Congrats you won first place! + 100 coins";
                positionReward = 100;
                layoutCongrats.setText(fontWhite, congrats);
                textWidth = layoutCongrats.width;
                x = (WINDOW_WIDTH - textWidth) / 2;
                correctMedal = goldMedal;
                break;
            case 1:
                congrats = "Congrats you won second place! + 50 coins";
                positionReward = 50;
                layoutCongrats.setText(fontWhite, congrats);
                textWidth = layoutCongrats.width;
                x = (WINDOW_WIDTH - textWidth) / 2;
                correctMedal = silverMedal;
                break;
            case 2:
                congrats = "Congrats you won third place! + 25 coins";
                positionReward = 25;
                layoutCongrats.setText(fontWhite, congrats);
                textWidth = layoutCongrats.width;
                x = (WINDOW_WIDTH - textWidth) / 2;
                correctMedal = bronzeMedal;
                break;
            case 3:
                congrats = "You arrived fourth place!";
                layoutCongrats.setText(fontWhite, congrats);
                textWidth = layoutCongrats.width;
                x = (WINDOW_WIDTH - textWidth) / 2;
                correctMedal = fourthMedal;
                break;
            default:
                break;

        }
        dataManager.setBalance(dataManager.getBalance() + positionReward);
        fontWhite.draw(batch, congrats, x, y);
        assert correctMedal != null;
        return correctMedal;
    }

    private void renderTutorial() {
        double tutorial_scaling = 0.7;
        if (stateTime < 5) {
            batch.draw(keysTutorialTexture, ((float) WINDOW_WIDTH / 3) - ((float) (int) (keysTutorialTexture.getWidth() * tutorial_scaling) / 2), (float) WINDOW_HEIGHT / 4, (int) (keysTutorialTexture.getWidth() * tutorial_scaling), (int) (keysTutorialTexture.getHeight() * tutorial_scaling));
        } else if (stateTime >= 5 && stateTime < 10) {
            batch.draw(UITutorialTexture, ((float) WINDOW_WIDTH / 3) - ((float) (int) (keysTutorialTexture.getWidth() * tutorial_scaling) / 2), (float) WINDOW_HEIGHT / 4, (int) (keysTutorialTexture.getWidth() * tutorial_scaling), (int) (keysTutorialTexture.getHeight() * tutorial_scaling));
        }
    }

    boolean enteredLoseScreen = false;
    private void renderLostScreen() {
        if (!enteredLoseScreen) {
            enteredLoseScreen = true;
            lose.play();
        }
        batch.draw(loseScreenTexture, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void renderWinner(){
        batch.draw(podiumTexture, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        // check for the position of boat number 0 in the boatsPosition list
        int index = boatsPosition.indexOf(lanes[0].getBoat());
        Texture correctMedal = getTexture(index);
        double factor = 0.3;
        float centeredX = (float) (WINDOW_WIDTH / 2 - ((float) correctMedal.getWidth() * factor / 2));
        float centeredY = (float) (WINDOW_HEIGHT * 0.9 - ((float) correctMedal.getHeight() * factor / 2));
        batch.draw(correctMedal, centeredX, centeredY, (float) (correctMedal.getWidth() * factor), (float) (correctMedal.getHeight() * factor));
    }

    private void renderGame(GameInputProcessor gameInputProcessor) {
        boolean finishedGame = false;
        if (lanes == null) {
            System.out.println("Creating new game");
            gameMusic.loop();
            createNewGame(gameInputProcessor, numberLeg);
        }
        // Water flow (GIF)
        stateTime += Gdx.graphics.getDeltaTime();
        int currentFrameIndex = (int) (stateTime / frameDuration) % water.length;
        batch.draw(water[currentFrameIndex], 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        //draw the lane dividers on the screen between the 4 lines
        for (LaneDivider laneDivider : laneDividers) {
            laneDivider.adjustPosition(0,-2 );
            batch.draw(laneDivider.getImage(), laneDivider.getPosition().getX(), laneDivider.getPosition().getY(), laneDivider.getWidth(), laneDivider.getHeight());
            if (laneDivider.getPosition().getY() < -laneDivider.getHeight()/2) {
                laneDivider.adjustPosition(0, (float) laneDivider.getHeight() /2);
            }
        }

        if (stateTime > LEG_DURATION) {
            finishLine();
        }

        boolean crossed;
        for (Lane lane : lanes) {
            Boat currentBoat = lane.getBoat();
            if (currentBoat.isVisible()) {
                batch.draw(currentBoat.getImage(), currentBoat.getPosition().getX(), currentBoat.getPosition().getY() - currentBoat.getHeight(), currentBoat.getWidth(), currentBoat.getHeight());
            }
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

            // Generate obstacles
            if (lane.spawnObstacleReady(Gdx.graphics.getDeltaTime(), numberLeg)) {
                lane.spawnObstacles();
            }
            lane.collision(this);

            crossed = checkFinishLineCrossed(lane.getBoat());
            if (crossed && !boatsPosition.contains(currentBoat)) {
                System.out.println("Boat " + currentBoat + " has crossed the finish line");
                boatsPosition.add(currentBoat);
                System.out.println(boatsPosition);
                if (boatsPosition.get(0).getId() == 0) {
                    win.play();
                }
            }
            checkCollisions();


            //make the obstacles move
            ArrayList<Obstacle> obstacles = lane.getObstacles();
            for (Obstacle obstacle : obstacles) {
                if (obstacle instanceof Gees) {
                    float movementSpeed = 2;
                    float deltaX = MathUtils.sinDeg(obstacle.getPosition().getY() * 5) * 5;
                    obstacle.adjustPosition(deltaX, -movementSpeed); // Move left to right with a downward speed
                } else if (obstacle instanceof FinishLine) {
                    obstacle.adjustPosition(0, -3);
                } else {
                    obstacle.adjustPosition(0, -5);
                }
                batch.draw(obstacle.getImage(), obstacle.getPosition().getX(), obstacle.getPosition().getY(), obstacle.getWidth(), obstacle.getHeight());
            }
            currentBoat.updateCooldown(Gdx.graphics.getDeltaTime());
        }

        batch.draw(panel, (float) (WINDOW_WIDTH*0.8), (float) (WINDOW_HEIGHT*0.55), (float) (panel.getWidth() * 0.3), (float) (panel.getHeight()* 0.4));
        font.draw(batch, ACCELERATION_BAR_TEXT, (float) (WINDOW_WIDTH * 0.82), (float) (WINDOW_HEIGHT *0.84));
        batch.draw(accelerationBarRectangle,  (float) (WINDOW_WIDTH * 0.82), (float) (WINDOW_HEIGHT *0.75), accelerationBarRectangleWidth, accelerationBarRectangleHeight);
        batch.draw(accelerationBarBackground, (float) (WINDOW_WIDTH * 0.82)+4, (float) (WINDOW_HEIGHT *0.75)+4, accelerationBarBackgroundWidth, accelerationBarBackgroundHeight);

        batch.draw(progressBarBackground, (float) (WINDOW_WIDTH / 3.97) + 5, (float) (WINDOW_HEIGHT * 0.905), progressBarRectangleWidth * this.getProgress(this.getPlayerBoat(), stateTime) - 15, progressBarBackgroundHeight);
        batch.draw(progressBarRectangle, (float) WINDOW_WIDTH / 2 - progressBarBackgroundWidth / 2, (float) (WINDOW_HEIGHT * 0.9), progressBarRectangleWidth, progressBarRectangleHeight);
        font.draw(batch, "Leg # : "+ (numberLeg+1), (float) WINDOW_WIDTH / 3, (float)(WINDOW_HEIGHT*0.88));
        // Render powerup
        float powerUpSlotFactor = 3;
        int space = (int) (WINDOW_HEIGHT * 0.01);
        float powerUpSlotHeight = (WINDOW_HEIGHT - space) - powerupSlot.getHeight() * powerUpSlotFactor;
        batch.draw(
                powerupSlot,
                space,
                Math.round(powerUpSlotHeight),
                powerUpSlotFactor * powerupSlot.getHeight(),
                powerUpSlotFactor * powerupSlot.getWidth()
        );
        if (this.availablePowerup != null) {
            // The mathematical expression is used to scale the image to fit in the slot without altering its proportions
            batch.draw(
                    availablePowerup.getTexture(),
                    space,
                    Math.round(powerUpSlotHeight),
                    Math.round(availablePowerup.getTexture().getWidth()
                            * powerUpSlotFactor
                            * powerupSlot.getHeight()
                            / availablePowerup.getTexture().getHeight()
                            * 0.9),
                    Math.round(powerUpSlotFactor * powerupSlot.getHeight() * 0.9)
            );
        }

        for (Lane lane : lanes) {
            String invulnerableText = "Invulnerable";
            if (this.getPlayerBoat().isInvulnerable()) {
                font.draw(batch, invulnerableText, 1400, 580);
            }
            Boat currentBoat = lane.getBoat();
            if (currentBoat.getIsPlayer()) {
                // loose if the boat breaks
                if (currentBoat.getBoatHealth() <= 0 && currentBoat.getIsPlayer()) {
                    currentBoat.setBoatHealth(0);
                    InputProcessor.setGameState(GameState.LOSE_SCREEN);
                    this.resetPowerup();
                    finishedGame = true;
                }
                double fatiguePercentage = currentBoat.getFatigueEffect();
                String fatigueText = "Fatigue Effect: " + (int) (fatiguePercentage * 100) + "%";
                String boatHealthText = "Boat Health: " + currentBoat.getBoatHealth();
                String momentumText = "Momentum: " + currentBoat.getCurrentMomentum();
                font.draw(batch, fatigueText,(float) (WINDOW_WIDTH * 0.82), (float) (WINDOW_HEIGHT *0.70));
                font.draw(batch, boatHealthText,(float) (WINDOW_WIDTH * 0.82), (float) (WINDOW_HEIGHT *0.65));
                font.draw(batch, momentumText, (float) (WINDOW_WIDTH * 0.82), (float) (WINDOW_HEIGHT *0.6));
            }
        }
        // Invulnerability  powerup logic
        invulnerabilityTimer += Gdx.graphics.getDeltaTime();
        if (this.getPlayerBoat().isInvulnerable() && invulnerabilityTimer >= 1) {
            invulnerabilityTimer = 0;
            this.getPlayerBoat().decreaseInvulnerabilityTime();
        }
        if (InputProcessor.getGameSubState() == GameSubState.TUTORIAL) {
            renderTutorial();
        }
        // The game change between substates depending on the leg number
        if (boatsPosition.size() == lanes.length) {
            System.out.println("Game is finished winner is: " + boatsPosition.get(0));
            if (InputProcessor.getGameSubState() == GameSubState.RACE_LEG) {
                InputProcessor.setGameState(GameState.PLAY_MINI_GAME);
                numberLeg++;
                for (int i = 0; i < NUMBER_OF_LANES; i++) {
                    int currentId = boatsPosition.get(i).getId();
                    positionsRecord.set(currentId, positionsRecord.get(currentId) + i);
                    System.out.println("Position record:" + positionsRecord);
                }
                finishedGame = true;
            }else if(InputProcessor.getGameSubState() == GameSubState.FINAL_LEG){
                    InputProcessor.setGameState(GameState.WINNER);
            } else {
                numberLeg = 0;
                InputProcessor.setGameState(GameState.LOBBY);
                finishedGame = true;
            }
        }
        if (InputProcessor.getGameSubState() == GameSubState.TUTORIAL) {
            renderTutorial();
        }
        if (finishedGame){
            resetGame();
        }
    }
    private void checkCollisions() {
        for (int i = 0; i < lanes.length; i++) {
            Boat boat1 = lanes[i].getBoat();
            for (int j = i + 1; j < lanes.length; j++) {
                Boat boat2 = lanes[j].getBoat();
                if (boat1.canCollide() && boat2.canCollide() && boat1.getBounds().intersects(boat2.getBounds())) {
                    handleCollision(boat1, boat2);
                }
            }

            for (Lane lane : lanes) {
                if (lane != lanes[i]) {
                    for (Obstacle obstacle : lane.getObstacles()) {
                        if (boat1.canCollide() && boat1.getBounds().intersects(obstacle.getBounds())) {
                            handleCollision(boat1, obstacle);
                        }
                    }
                }
            }
        }
    }

    private void handleCollision(Boat boat1, Boat boat2) {
        boat1.setBoatHealth(boat1.getBoatHealth() - 10);
        boat2.setBoatHealth(boat2.getBoatHealth() - 10);
        boat1.setCollisionCooldown(2);
        boat2.setCollisionCooldown(2);
    }

    private void handleCollision(Boat boat, Obstacle obstacle) {
        boat.setBoatHealth(boat.getBoatHealth() - obstacle.getDamage());
        boat.setCollisionCooldown(2);
    }


    private void renderMiniGame() {
        renderUI();
        switch (miniGameState) {
            case NOT_STARTED:
                renderTiles();
                updateCountdownTimer();
                if (countdownTimer.getTime() <= 0) {
                    prepareHidingItems();
                }

                break;
            case HIDING_ITEMS:
                renderTiles();
                updateCountdownTimer();
                if (countdownTimer.getTime() <= 0) {
                    prepareShowingItems();
                }
                renderRandomPowerup();
                break;
            case SHOWING_ITEMS:
                preparePlayingState();
                miniGameState = MiniGameState.PLAYING;
                break;
            case PLAYING:
                renderItemTiles();
                updateCountdownTimer();
                if (countdownTimer.getTime() <= 0) {
                    miniGameState = MiniGameState.FINISHED;
                    countdownTimer.reset();
                }
                break;
            case FINISHED:
                minigameStage++;
                boolean correctTileClicked = checkCorrectTileClicked();
                if (correctTileClicked) {
                    win.play();
                    dataManager.setBalance(dataManager.getBalance() + money);
                    //batch.draw(correct, WINDOW_WIDTH, WINDOW_HEIGHT , correct.getWidth(), correct.getHeight());
                } else
                    lose.play();
                miniGameState = MiniGameState.NOT_STARTED;
                if (minigameStage >= NUMBER_OF_MINIGAMES) {
                    miniGameState = MiniGameState.SUM_SCREEN;
                }
                break;
            case SUM_SCREEN:
                minigameStage = 0;
                renderSumScreen();
                InputProcessor minigameSumScreenIP;
                if (numberLeg < NUMBER_OF_LEGS)
                    minigameSumScreenIP = new MinigameSumScreenInputProcessor(this, "game");
                else {
                    minigameSumScreenIP = new MinigameSumScreenInputProcessor(this, "final-game");
                }
                Gdx.input.setInputProcessor(minigameSumScreenIP);
                break;
            default:
                break;
        }
        if (miniGameState != MiniGameState.SUM_SCREEN) {
            renderDragonPlayer();
        }
    }

    private void renderUI() {
        String gameTitle = "Tile master";
        GlyphLayout layout = new GlyphLayout(font, gameTitle);
        float titleWidth = layout.width;
        font.draw(batch, gameTitle, (Gdx.graphics.getWidth() - titleWidth) / 2, Gdx.graphics.getHeight() - 50);

        String coinsEarned = "Coins: " + money + " ";
        font.draw(batch, coinsEarned, 10, Gdx.graphics.getHeight() - 40);

        String countdown = "Time: " + (int) (countdownTimer.getTime() + 1) + "s";
        GlyphLayout countDownLayout = new GlyphLayout(font, countdown);
        float countdownWidth = countDownLayout.width;
        font.draw(batch, countdown, Gdx.graphics.getWidth() - countdownWidth - 40, Gdx.graphics.getHeight() - 40);
    }

    private void renderTiles() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int tileWidth = WINDOW_WIDTH / 4;
                float tileHeight = (float) (WINDOW_HEIGHT ) / 4;
                float x = i * tileWidth;
                float y = (3 - j) * tileHeight;
                batch.draw(tiles, x, y, tileWidth, tileHeight);
            }
        }
    }

    private void updateCountdownTimer() {
        countdownTimer.start();
        countdownTimer.update(Gdx.graphics.getDeltaTime());
    }

    private void prepareHidingItems() {
        countdownTimer.reset();
        randomObstacle = MathUtils.random(1, 4);
        miniGameState = MiniGameState.HIDING_ITEMS;
    }

    private void renderRandomPowerup() {
        Texture texture;
        switch (randomObstacle) {
            case 1:
                texture = new Texture("powerups/cat.png");
                break;
            case 2:
                texture = new Texture("powerups/koi.png");
                break;
            case 3:
                texture = new Texture("powerups/cookie.png");
                break;
            case 4:
                texture = new Texture("powerups/sakura_flower.png");
                break;
            default:
                return;
        }
        renderObstacle(texture);
    }


    private void renderObstacle(Texture texture) {
        float centeredX = ((float) WINDOW_WIDTH / 2) - ((float) texture.getWidth() / 2);
        float centeredY = ((float) WINDOW_HEIGHT / 2) - ((float) texture.getHeight() / 2);

        Entity obstacle = new Entity(
                new Position((int) centeredX, (int) centeredY),
                texture.getWidth(), texture.getHeight(), texture
        );
        batch.draw(obstacle.getImage(), obstacle.getPosition().getX(), obstacle.getPosition().getY(), obstacle.getWidth(), obstacle.getHeight());
    }

    private void prepareShowingItems() {
        countdownTimer.reset();
        itemTiles = new ArrayList<>(Collections.nCopies(16, null));
        miniGameState = MiniGameState.SHOWING_ITEMS;
    }

    private void preparePlayingState() {
        int correctTileCount = MathUtils.random(2, 4);
        while (correctTileCount > 0) {
            int randomTile = MathUtils.random(0, 15);
            if (itemTiles.get(randomTile) == null) {
                itemTiles.set(randomTile, createCorrectTile(randomTile));
                correctTileCount--;
            }
        }
        fillIncorrectTiles();
    }

    private Entity createCorrectTile(int index) {
        //int tileWidth = Gdx.graphics.getWidth() / 4;
        int tileWidth = WINDOW_WIDTH / 4;
        float tileHeight = (float)  WINDOW_HEIGHT / 4;
        int row = index % 4;
        int column = index / 4;
        float x = column * tileWidth;
        float y = (3 - row) * tileHeight;
        Texture texture;
        switch (randomObstacle) {
            case 1:
                texture = new Texture("tiles/cat.jpg");
                break;
            case 2:
                texture = new Texture("tiles/koi.jpg");
                break;
            case 3:
                texture = new Texture("tiles/cookie.jpg");
                break;
            case 4:
                texture = new Texture("tiles/sakura_flower.jpg");
                break;
            default:
                return null;
        }
        return new Entity(new Position((int) x, (int) y), tileWidth, (int) tileHeight, texture);
    }

    private void fillIncorrectTiles() {
        for (int i = 0; i < itemTiles.size(); i++) {
            if (itemTiles.get(i) == null) {
                itemTiles.set(i, createIncorrectTile(i));
            }
        }
    }

    private Entity createIncorrectTile(int index) {
        int tileWidth = WINDOW_WIDTH / 4;
        float tileHeight = (float) (WINDOW_HEIGHT) / 4;
        int row = index % 4;
        int column = index / 4;
        float x = column * tileWidth;
        float y = (3 - row) * tileHeight;
        int randomTileIncorrect;
        do {
            randomTileIncorrect = MathUtils.random(1, 4);
        } while (randomTileIncorrect == randomObstacle);
        Texture texture;
        switch (randomTileIncorrect) {
            case 1:
                texture = new Texture("tiles/cat.jpg");
                break;
            case 2:
                texture = new Texture("tiles/koi.jpg");
                break;
            case 3:
                texture = new Texture("tiles/cookie.jpg");
                break;
            case 4:
                texture = new Texture("tiles/sakura_flower.jpg");
                break;
            default:
                return null;
        }
        return new Entity(new Position((int) x, (int) y), tileWidth, (int) tileHeight, texture);
    }

    private void renderItemTiles() {
        for (Entity itemTile : itemTiles) {
            batch.draw(itemTile.getImage(), itemTile.getPosition().getX(), itemTile.getPosition().getY(), itemTile.getWidth(), itemTile.getHeight());
        }
    }

    private boolean checkCorrectTileClicked() {
        for (Entity itemTile : itemTiles) {
            if (isDragonPlayerInsideTile(dragonPlayer, itemTile)){
                switch (randomObstacle) {
                    case 1:
                        if (itemTile.getImage().toString().equals("tiles/cat.jpg")) {
                            money += 10;
                            return true;
                        }
                        break;
                    case 2:
                        if (itemTile.getImage().toString().equals("tiles/koi.jpg")) {
                            money += 10;
                            return true;
                        }
                        break;
                    case 3:
                        if (itemTile.getImage().toString().equals("tiles/cookie.jpg")) {
                            money += 10;
                            return true;
                        }
                        break;
                    case 4:
                        if (itemTile.getImage().toString().equals("tiles/sakura_flower.jpg")) {
                            money += 10;
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return false;
    }
    private boolean isDragonPlayerInsideTile(Entity dragonPlayer, Entity itemTile) {
        Rectangle dragonBounds = dragonPlayer.getBounds();
        Rectangle tileBounds = itemTile.getBounds();

        return tileBounds.contains(dragonBounds.x, dragonBounds.y) &&
                tileBounds.contains(dragonBounds.x + dragonBounds.width, dragonBounds.y) &&
                tileBounds.contains(dragonBounds.x, dragonBounds.y + dragonBounds.height) &&
                tileBounds.contains(dragonBounds.x + dragonBounds.width, dragonBounds.y + dragonBounds.height);
    }

    private void renderSumScreen() {
        batch.draw(shopBackground, 0, 0, WINDOW_WIDTH, WINDOW_WIDTH);
        String coinsEarned = "Coins earned: " + money;
        String roundMade = "Round made: " + (money / 10);
        GlyphLayout coinsEarnedGlyph = new GlyphLayout(font, coinsEarned);
        GlyphLayout roundMadeGlyph = new GlyphLayout(font, roundMade);
        batch.draw(verticalScroll, (float) (WINDOW_WIDTH * 0.5 - verticalScroll.getWidth() * 0.5), (float) (WINDOW_HEIGHT * 0.7 - verticalScroll.getHeight() * 0.5), verticalScroll.getWidth(), verticalScroll.getHeight());
        font.draw(batch, coinsEarnedGlyph, (float) (WINDOW_WIDTH * 0.5 - coinsEarnedGlyph.width * 0.5), (float) (WINDOW_HEIGHT * 0.73 - coinsEarnedGlyph.height * 0.5));
        font.draw(batch, roundMadeGlyph, (float) (WINDOW_WIDTH * 0.5 - coinsEarnedGlyph.width * 0.5), (float) (WINDOW_HEIGHT * 0.67 - coinsEarnedGlyph.height * 0.5));
    }

    public void resetMiniGame(){
        money = 0;
        miniGameState = MiniGameState.NOT_STARTED;
        dragonPlayer.setPosition((Gdx.graphics.getWidth() - dragonHead.getWidth()/2) / 2, (Gdx.graphics.getHeight() - dragonHead.getHeight()/2) / 2 -50);
        countdownTimer.reset();
        if (itemTiles != null)
            itemTiles.clear();
    }

    private void renderDragonPlayer() {
        dragonPlayer.updateKeys(Gdx.graphics.getDeltaTime());
        batch.draw(dragonPlayer.getImage(), dragonPlayer.position.getX(), dragonPlayer.position.getY(), dragonPlayer.getWidth(), dragonPlayer.getHeight());
    }

    private void renderShopLayout() {
        batch.draw(shopBackground,0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        batch.draw(verticalScroll,(float) (WINDOW_WIDTH*0.1), (float) (WINDOW_HEIGHT*0.28), (float) (verticalScroll.getWidth() * 2.15), (float) (verticalScroll.getHeight()* 2.18));
        batch.draw(shopDescription, (float) (WINDOW_WIDTH*0.6), (float) (WINDOW_HEIGHT*0.1), (float) (shopDescription.getWidth()*4), (float) (shopDescription.getHeight()*4));
        batch.draw(leftArrow, (float) (WINDOW_WIDTH*0.05), (float) (WINDOW_HEIGHT*0.1), leftArrow.getWidth(), leftArrow.getHeight());
        batch.draw(rightArrow, (float) (WINDOW_WIDTH*0.38), (float) (WINDOW_HEIGHT*0.1), rightArrow.getWidth(), rightArrow.getHeight());
        batch.draw(horizontalScroll, (float) (WINDOW_WIDTH*0.12), (float) (WINDOW_HEIGHT*0.12), horizontalScroll.getWidth(), (float) (horizontalScroll.getHeight()*0.3));
        batch.draw(upperArrow, (float) (WINDOW_WIDTH * 0.03), (float) (WINDOW_HEIGHT * 0.6), (float) (horizontalScroll.getWidth() * 0.3), (float) (horizontalScroll.getHeight() * 0.3));
        batch.draw(bottomArrow, (float) (WINDOW_WIDTH*0.03), (float) (WINDOW_HEIGHT*0.4), (float) (horizontalScroll.getWidth() * 0.3), (float) (horizontalScroll.getHeight()*0.3));
    }
    private void renderBoatsShop() {
        ShopBoat shopBoat = dataManager.boats.get(currentShopBoatIndex);
        Texture boatTexture = new Texture(Gdx.files.internal("boats/" + shopBoat.getImageName()));

        this.renderShopLayout();

        batch.draw(boatTexture,(float) (WINDOW_WIDTH*0.20), (float) (WINDOW_HEIGHT*0.38),(float) (boatTexture.getWidth()*0.25), (float) (boatTexture.getHeight()*0.25));

        font.draw(batch, shopBoat.getName(), (float) (WINDOW_WIDTH*0.22), (float) (WINDOW_HEIGHT*0.18));
        font.draw(batch, "STATS", (float) (WINDOW_WIDTH*0.74), (float) (WINDOW_HEIGHT*0.85));
        font.draw(batch, "Speed Factor: " + shopBoat.getSpeedFactor(), (float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.75));
        font.draw(batch, "Acceleration: " + shopBoat.getAcceleration(),(float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.68));
        font.draw(batch, "Robustness: " + shopBoat.getRobustness(), (float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.61));
        font.draw(batch, "Maneuverability: " + shopBoat.getManeuverability(), (float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.54));
        font.draw(batch, "Momentum Factor: " + shopBoat.getMomentumFactor(),  (float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.46));
        font.draw(batch, "Fatigue: " + shopBoat.getFatigue(), (float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.39));
        if (!shopBoat.isUnlocked()) {
            font.draw(batch, "Price: " + shopBoat.getPrice(), (float) (WINDOW_WIDTH*0.73), (float) (WINDOW_HEIGHT*0.24));
            font.draw(batch, "Press enter to unlock", (float) (WINDOW_WIDTH*0.7), (float) (WINDOW_HEIGHT*0.19));
        } else {
            font.draw(batch, "Unlocked", (float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.25));
            if (shopBoat.isSelected())
                font.draw(batch, "Selected", (float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.17));
            else
                font.draw(batch, "Not Selected", (float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.17));
        }
        boatTexture.dispose();
    }

    int currentShopPowerup = 0;
    private void renderPowerupsShop() {
        // Update powerup
        this.availablePowerup = dataManager.getPowerup(this);
        Powerup powerup = dataManager.getPowerupById(currentShopPowerup, this);
        Texture powerupTexture = powerup.getTexture();

        this.renderShopLayout();

        batch.draw(powerupTexture,(float) (WINDOW_WIDTH*0.20), (float) (WINDOW_HEIGHT*0.50),(float) (powerupTexture.getWidth()*0.25), (float) (powerupTexture.getHeight()*0.25));

        font.draw(batch, powerup.getName(), (float) (WINDOW_WIDTH*0.22), (float) (WINDOW_HEIGHT*0.18));
        font.draw(batch, "Description: " + powerup.getDescription(), (float) (WINDOW_WIDTH*0.63), (float) (WINDOW_HEIGHT*0.6));
        if (this.availablePowerup != null && this.availablePowerup.getName().equals(powerup.getName())) {
            font.draw(batch, "Acquired", (float) (WINDOW_WIDTH*0.73), (float) (WINDOW_HEIGHT*0.22));
        } else{
            font.draw(batch, "Price: " + powerup.getPrice(), (float) (WINDOW_WIDTH*0.73), (float) (WINDOW_HEIGHT*0.24));
            font.draw(batch, "Press enter to buy", (float) (WINDOW_WIDTH*0.71), (float) (WINDOW_HEIGHT*0.19));
        }
    }

    boolean enteredShop = false;
    private void renderShop() {
        if (!enteredShop) {
            enteredShop = true;
            shopMusic.loop();
        }
        switch (InputProcessor.getShopSubStates()) {
            case BOATS:
                this.renderBoatsShop();
                break;
            case POWERUPS:
                this.renderPowerupsShop();
                break;
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
             dataManager.getSelectedBoat().setSelected(false);
             shopBoat.setSelected(true);
             shopBoat.setUnlocked(true);
             dataManager.setBalance(dataManager.getBalance() - shopBoat.getPrice());
         } else if (shopBoat.isUnlocked()) {
             dataManager.getSelectedBoat().setSelected(false);
             shopBoat.setSelected(true);
         }

         System.err.println(shopBoat.isSelected());
         System.err.println(dataManager.boats.get(1).isSelected());
         dataManager.saveShopBoats();
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
        goldMedal.dispose();
        silverMedal.dispose();
        bronzeMedal.dispose();
        fourthMedal.dispose();

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
        accelerationLevel -= FULL_ACCELERATION_BAR * delta;
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

    private float getProgress(Boat boat, float currentTime) {
        float progress = currentTime / LEG_DURATION;
        return progress >= 1 ? 1 : progress;
        }

    public Boat getPlayerBoat() {
        return lanes[0].getBoat();
    }

    public void usePowerup() {
        if (availablePowerup != null) {
            this.availablePowerup.use();
            this.availablePowerup = null;
        }
    }

    public void nextShopPowerup() {
        this.currentShopPowerup = (this.currentShopPowerup + 1) % 4;
    }

    public void previousShopPowerup() {
        this.currentShopPowerup--;
        if (this.currentShopPowerup < 0) {
            this.currentShopPowerup = 3;
        }
    }

    public void resetPowerup() {
        this.availablePowerup = null;
        dataManager.setPowerup(-1);
    }

    public void buyPowerup() {
        Powerup selected = dataManager.getPowerupById(this.currentShopPowerup, this);
        if (this.availablePowerup == null && selected.getPrice() <= dataManager.getBalance()) {
            dataManager.setPowerup(this.currentShopPowerup);
            this.availablePowerup = selected;
            dataManager.setBalance(dataManager.getBalance() - selected.getPrice());
        }
    }

    public void deleteAllPlayerObstacles() {
        this.lanes[0].obstacles.clear();
    }

}