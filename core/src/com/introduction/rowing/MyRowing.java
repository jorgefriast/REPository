package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

import static com.introduction.rowing.Constants.*;

public class MyRowing extends ApplicationAdapter {
    SpriteBatch batch;
    GameState currentState;
    MiniGameState miniGameState;
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
    ArrayList<Boat> boatsPosition = new ArrayList<>();
    Texture laneDividerTexture;
    ArrayList<LaneDivider> laneDividers;
    ArrayList<Integer> positionsRecord = new ArrayList<>();
    FinishLine finishline;
    Texture keysTutorialTexture;
    Texture loseScreenTexture;
    Texture UITutorialTexture;
    Texture finishLineTexture;
    Texture tiles;
    Texture dragonHead;
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
    ScreenViewport viewport;
    Stage stage;
    private DataManager dataManager;
    int currentShopBoatIndex = 0;
    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;


    int numberLeg = 0;
    int minigameStage = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        lobbyImage = new Texture("backgrounds/lobby.png");
        keysTutorialTexture = new Texture("backgrounds/tutorial.png");
        loseScreenTexture = new Texture("backgrounds/loose-screen.png");
        UITutorialTexture = new Texture("backgrounds/ui-tutorial.png");
        boatPicture = new Texture("boats/saoko.png");
        accelerationBarRectangle = new Texture("accelerationBarRectangle.png");
        accelerationBarBackground = new Texture("acceleration_bar_background.png");
        laneDividerTexture = new Texture("lanedivider.jpeg");
        tiles = new Texture("tile.jpg");
        dragonHead = new Texture("powerups/dragon_head.png");
        sumScreenMiniGame = new Texture("shop-background/frame_1_delay-0.1s.png");
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);


        // Load the custom font using FreeTypeFontGenerator
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Zanden.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);

        currentState = GameState.LOBBY;

        miniGameState = MiniGameState.NOT_STARTED;

        gameInputProcessor = new GameInputProcessor(this);
        tutorialInputProcessor = new TutorialInputProcessor(this);
        loseScreenInputProcessor = new LoseScreenInputProcessor(this);
        lobbyInputProcessor = new LobbyInputProcessor(this);
        miniGameInputProcessor = new MiniGameInputProcessor(this);
        shopInputProcessor = new ShopInputProcessor(this);
        dragonPlayer = new DragonHead(new Position((Gdx.graphics.getWidth() - dragonHead.getWidth()/2) / 2, (Gdx.graphics.getHeight() - dragonHead.getHeight()/2) / 2 -50), dragonHead.getWidth() /2, dragonHead.getHeight()/2, dragonHead, miniGameInputProcessor);
        countdownTimer = new CountdownTimer(3);
        randomObstacle = 1;
        money = 0;

        laneDividers = new ArrayList<>();


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
        for (int i = 0; i < NUMBER_OF_LANES; i++) {
            float multiplier = numberLeg != NUMBER_OF_LEGS ? 1 : (positionsRecord.get(i) / ((float) NUMBER_OF_LEGS * (NUMBER_OF_LANES - 1)) + 1) / 2;
            System.out.println("MULTIPLIER " + multiplier);
            Position startingPosition = new Position(currentLeftBoundary + (laneWidth / 2),  (int) (-270 * multiplier));
            if (i == 0) {
                lanes[i] = new Lane(
                        new Boat(i, startingPosition, boatPicture, true, 5, 3, 1, 2, 3, 1, inputProcessor),
                        currentLeftBoundary
                );
            } else {
                lanes[i] = new Lane(new Boat(i, startingPosition, boatPicture, false, 5, 5, 5, 5, 5, 5, null), currentLeftBoundary);
            }
            currentLeftBoundary += laneWidth;
        }
        for(int i = 1; i < 4; i++) {
            laneDividers.add(new LaneDivider(new Position((int) (i * ((float) WINDOW_WIDTH / NUMBER_OF_LANES)), 0), 10, WINDOW_HEIGHT, laneDividerTexture));
        }
    }

    public void resetGame() {
        boatsPosition.clear();
        accelerationLevel = 0;
        stateAccelerating = false;
        stateTime = 0;
        createNewGame(gameInputProcessor, numberLeg);
        System.out.println("Game reset");
    }

    public void renderLobby() {
        batch.draw(lobbyImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        font.draw(batch, "Money balance: "+ dataManager.getBalance() , 150, 150);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 1, 0, 1);
        batch.begin();
        currentState = InputProcessor.getGameState();
        switch (currentState) {
            case LOBBY:
                Gdx.input.setInputProcessor(lobbyInputProcessor);
                renderLobby();
                break;
            case PLAY_GAME:
                Gdx.input.setInputProcessor(gameInputProcessor);
                renderGame(gameInputProcessor, InputProcessor.getGameSubState());
                break;
            case PLAY_MINI_GAME:
                Gdx.input.setInputProcessor(miniGameInputProcessor);
                renderMiniGame();
                break;
            case ENTER_SHOP:
                Gdx.input.setInputProcessor(shopInputProcessor);
                renderShop();
                break;
            case LOSE_SCREEN:
                Gdx.input.setInputProcessor(loseScreenInputProcessor);
                batch.draw(loseScreenTexture, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                break;
            default:
                break;
        }
        batch.end();
    }

    private  void renderTutorial() {
        if (stateTime < LEG_DURATION) {
            batch.draw(keysTutorialTexture, 0, WINDOW_HEIGHT / 2, 1920, 540);
        } else if (stateTime >= 5 && stateTime < 10) {
            batch.draw(UITutorialTexture, 0, WINDOW_HEIGHT / 2, 1920, 540);
        }
    }

    private void renderGame(GameInputProcessor gameInputProcessor, GameSubState gameSubState) {
        if (lanes == null) {
            System.out.println("Creating new game");
            createNewGame(gameInputProcessor, numberLeg);
            //draw the lane dividers on the screen between the 4 lines
            for (int i = 1; i < laneDividers.size(); i++) {
                System.out.println("Drawing lane dividers");
                batch.draw(laneDividers.get(i).getImage(), laneDividers.get(i).position.getX(), laneDividers.get(i).position.getY(), laneDividers.get(i).getWidth(), WINDOW_HEIGHT);
            }
        }
        if (boatsPosition.size() == lanes.length) {
            System.out.println("Game is finished winner is: " + boatsPosition.get(0));
        }
        // Water flow (GIF)
        stateTime += Gdx.graphics.getDeltaTime();
        int currentFrameIndex = (int) (stateTime / frameDuration) % water.length;
        batch.draw(water[currentFrameIndex], 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        //boat movement & obstacle spawning
        for (LaneDivider laneDivider : laneDividers) {
            laneDivider.adjustPosition(0, -2);
            batch.draw(laneDivider.getImage(), laneDivider.getPosition().getX(), laneDivider.getPosition().getY(), laneDivider.getWidth(), laneDivider.getHeight());
        }
        if (stateTime > 5) {
            finishLine();
        }
        boolean crossed;
        for (Lane lane : lanes) {
            Boat currentBoat = lane.getBoat();
            batch.draw(currentBoat.getImage(), currentBoat.getPosition().getX(), (int) (-currentBoat.getHeight() * 0.5), currentBoat.getWidth(), currentBoat.getHeight());
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
            if (lane.spawnObstacleReady(Gdx.graphics.getDeltaTime())) { lane.spawnObstacles(); }
            lane.collision();

            crossed = checkFinishLineCrossed(lane.getBoat());
            if (crossed && !boatsPosition.contains(currentBoat)) {
                System.out.println("Boat " + currentBoat + " has crossed the finish line");
                boatsPosition.add(currentBoat);
                System.out.println(boatsPosition);
            }
            // The game change between substates depending on the leg number
            if (boatsPosition.size() == lanes.length) {
                System.out.println("Game is finished winner is: " + boatsPosition.get(0));
                if (gameSubState == GameSubState.RACE_LEG) {
                    InputProcessor.setGameState(GameState.PLAY_MINI_GAME);
                    numberLeg++;
                    for (int i = 0; i < NUMBER_OF_LANES; i++) {
                        int currentId = boatsPosition.get(i).getId();
                        positionsRecord.set(currentId, positionsRecord.get(currentId) + i);
                        System.out.println("Position record:" + positionsRecord);
                    }
                } else {
                    numberLeg = 0;
                    InputProcessor.setGameState(GameState.LOBBY);
                }
                resetGame();
                System.out.println("NUMBER LEG: " + numberLeg);
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
                // obstacle.adjustPosition((float) 0, (float) (-5));
                batch.draw(obstacle.getImage(), obstacle.getPosition().getX(), obstacle.getPosition().getY(), obstacle.getWidth(), obstacle.getHeight());

                // Remove obstacle if it's below the screen
                if (obstacle.getPosition().getY() < 0) {
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
                // loose if the boat breaks
                if (currentBoat.getBoatHealth() <= 0) {
                    currentBoat.setBoatHealth(0);
                    InputProcessor.setGameState(GameState.LOSE_SCREEN);
                    resetGame();
                }
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
        if (gameSubState == GameSubState.TUTORIAL) {
            renderTutorial();
        }
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
                renderRandomObstacle();
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
                System.out.println("MINIGAME SATATE " + minigameStage);
                boolean correctTileClicked = checkCorrectTileClicked();
                if (correctTileClicked) {
                    dataManager.setBalance(dataManager.getBalance() + money);
                }
                miniGameState = MiniGameState.NOT_STARTED;
                if (minigameStage == 1 ) {
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
                float tileHeight = (float) (WINDOW_HEIGHT - 100) / 4;
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

    private void renderRandomObstacle() {
        Texture texture;
        switch (randomObstacle) {
            case 1:
                texture = new Texture("rock.png");
                break;
            case 2:
                texture = new Texture("geese-bg.png");
                break;
            case 3:
                texture = new Texture("duck-bg.png");
                break;
            case 4:
                texture = new Texture("wood.png");
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
        int tileWidth = Gdx.graphics.getWidth() / 4;
        float tileHeight = (float) (Gdx.graphics.getHeight() - 100) / 4;
        int row = index % 4;
        int column = index / 4;
        float x = column * tileWidth;
        float y = (3 - row) * tileHeight;
        Texture texture;
        switch (randomObstacle) {
            case 1:
                texture = new Texture("rock_tile.png");
                break;
            case 2:
                texture = new Texture("geese_tile.png");
                break;
            case 3:
                texture = new Texture("duck_tile.png");
                break;
            case 4:
                texture = new Texture("log_tile.png");
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
        int tileWidth = Gdx.graphics.getWidth() / 4;
        float tileHeight = (float) (Gdx.graphics.getHeight() - 100) / 4;
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
                texture = new Texture("rock_tile.png");
                break;
            case 2:
                texture = new Texture("geese_tile.png");
                break;
            case 3:
                texture = new Texture("duck_tile.png");
                break;
            case 4:
                texture = new Texture("log_tile.png");
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
                        if (itemTile.getImage().toString().equals("rock_tile.png")) {
                            money += 10;
                            return true;
                        }
                        break;
                    case 2:
                        if (itemTile.getImage().toString().equals("geese_tile.png")) {
                            money += 10;
                            return true;
                        }
                        break;
                    case 3:
                        if (itemTile.getImage().toString().equals("duck_tile.png")) {
                            money += 10;
                            return true;
                        }
                        break;
                    case 4:
                        if (itemTile.getImage().toString().equals("log_tile.png")) {
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
        batch.draw(sumScreenMiniGame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        String coinsEarned = "Coins earned: " + money;
        String roundMade = "Round made: " + (money / 10);
        GlyphLayout layout = new GlyphLayout(font, coinsEarned);
        GlyphLayout layout2 = new GlyphLayout(font, roundMade);
        float width = layout.width;
        float width2 = layout2.width;
        font.draw(batch, coinsEarned, (Gdx.graphics.getWidth() - width) / 2, ((float) Gdx.graphics.getHeight() /2) + 200);
        font.draw(batch, roundMade, (Gdx.graphics.getWidth() - width2) / 2, ((float) Gdx.graphics.getHeight() /2) + 150);
    }

    public void resetMiniGame(){
        money = 0;
        miniGameState = MiniGameState.NOT_STARTED;
        dragonPlayer.setPosition((Gdx.graphics.getWidth() - dragonHead.getWidth()/2) / 2, (Gdx.graphics.getHeight() - dragonHead.getHeight()/2) / 2 -50);
        countdownTimer.reset();
        itemTiles.clear();
    }

    private void renderDragonPlayer() {
        dragonPlayer.updateKeys(Gdx.graphics.getDeltaTime());
        batch.draw(dragonPlayer.getImage(), dragonPlayer.position.getX(), dragonPlayer.position.getY(), dragonPlayer.getWidth(), dragonPlayer.getHeight());
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
