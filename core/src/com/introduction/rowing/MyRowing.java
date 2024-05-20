package com.introduction.rowing;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.w3c.dom.Text;

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
    FinishLine finishline;
    Texture finishLineTexture;
    Texture tiles;
    Texture dragonHead;
    DragonHead dragonPlayer;
    CountdownTimer countdownTimer;
    int randomObstacle;
    ArrayList<Entity> itemTiles;
    Texture gameOverMiniGame;
    int money;

    GameInputProcessor gameInputProcessor;
    LobbyInputProcessor lobbyInputProcessor;
    MiniGameInputProcessor miniGameInputProcessor;
    ScreenViewport viewport;
    Stage stage;
    @Override
    public void create() {
        batch = new SpriteBatch();
        lobbyImage = new Texture("main-lobby.jpeg");
        boatPicture = new Texture("boat-top-view-2.png");
        accelerationBarRectangle = new Texture("accelerationBarRectangle.png");
        accelerationBarBackground = new Texture("acceleration_bar_background.png");
        laneDividerTexture = new Texture("lanedivider.jpeg");
        tiles = new Texture("tile.jpg");
        dragonHead = new Texture("head-removebg.png");
        gameOverMiniGame = new Texture("GameOver_score.png");
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);

        currentState = GameState.LOBBY;
        miniGameState = MiniGameState.NOT_STARTED;

        gameInputProcessor = new GameInputProcessor(this);
        lobbyInputProcessor = new LobbyInputProcessor(this);
        miniGameInputProcessor = new MiniGameInputProcessor(this);
        dragonPlayer = new DragonHead(new Position((Gdx.graphics.getWidth() - dragonHead.getWidth()/2) / 2, (Gdx.graphics.getHeight() - dragonHead.getHeight()/2) / 2 -50), dragonHead.getWidth() /2, dragonHead.getHeight()/2, dragonHead, miniGameInputProcessor);
        countdownTimer = new CountdownTimer(3);
        randomObstacle = 1;
        money = 0;

        laneDividers = new ArrayList<>();


        // Water GIF setup
        water = new TextureRegion[5];
        for (int i = 0; i < water.length; i++)
            water[i] = new TextureRegion(new Texture("water-frames//frame_" + i + "_delay-0.1s.gif"));
        // Initialize the stage and viewport
        viewport = new ScreenViewport();
        stage = new Stage(viewport, batch);

    }

    public void createNewGame() {
        finishLineTexture = new Texture("arts0587-02_0.png");
        finishline = new FinishLine(new Position(0, Gdx.graphics.getHeight()), Gdx.graphics.getWidth(), 100, finishLineTexture);
        miniGameState = MiniGameState.NOT_STARTED;
        lanes = new Lane[NUMBER_OF_LANES];
        int laneWidth = WINDOW_WIDTH / NUMBER_OF_LANES;
        int currentLeftBoundary = 0;
        for (int i = 0; i < NUMBER_OF_LANES; i++) {
            Position startingPosition = new Position(currentLeftBoundary + (laneWidth / 2), -230);
            if (i == 0) {
                lanes[i] = new Lane(new Boat(startingPosition, boatPicture, true, 5, 3, 5, 2, 3, 1, gameInputProcessor), currentLeftBoundary);
            } else {
                lanes[i] = new Lane(new Boat(startingPosition, boatPicture, false, 5, 5, 5, 5, 5, 5, null), currentLeftBoundary);
            }
            currentLeftBoundary += laneWidth;
        }
        for(int i = 1; i < 4; i++) {
            laneDividers.add(new LaneDivider(new Position((int) (i * ((float) WINDOW_WIDTH / NUMBER_OF_LANES)), 0), 10, WINDOW_HEIGHT, laneDividerTexture));
        }
        System.out.println("Game created");
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
                break;
            case PLAY_GAME:
                Gdx.input.setInputProcessor(gameInputProcessor);
                renderGame();
                break;
            case PLAY_MINI_GAME:
                Gdx.input.setInputProcessor(miniGameInputProcessor);
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
        if (lanes == null) {
            System.out.println("Creating new game");
            createNewGame();
            //draw the lane dividers on the screen between the 4 lines
            for (int i = 1; i < laneDividers.size(); i++) {
                System.out.println("Drawing lane dividers");
                batch.draw(laneDividers.get(i).getImage(), laneDividers.get(i).position.getX(), laneDividers.get(i).position.getY(), laneDividers.get(i).getWidth(), WINDOW_HEIGHT);
            }
        }
        //make the lineDividers move down the screen at the speed of the boat

        if (boatsPosition.size() == lanes.length) {
            System.out.println("Game is finished winner is: " + boatsPosition.get(0));
        }
        // Water flow (GIF)
        stateTime += Gdx.graphics.getDeltaTime();
        int currentFrameIndex = (int) (stateTime / frameDuration) % water.length;
        batch.draw(water[currentFrameIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //boat movement & obstacle spawning
        for (LaneDivider laneDivider : laneDividers) {
            laneDivider.adjustPosition(0, -2);
            batch.draw(laneDivider.getImage(), laneDivider.getPosition().getX(), laneDivider.getPosition().getY(), laneDivider.getWidth(), laneDivider.getHeight());
        }
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
                boolean correctTileClicked = checkCorrectTileClicked();
                if (correctTileClicked) {
                    miniGameState = MiniGameState.NOT_STARTED;
                } else {
                    miniGameState = MiniGameState.GAME_OVER;
                }
                break;
            case GAME_OVER:
                renderGameOverScreen();
                break;
            default:
                break;
        }
        if (miniGameState != MiniGameState.GAME_OVER) {
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
                int tileWidth = Gdx.graphics.getWidth() / 4;
                float tileHeight = (float) (Gdx.graphics.getHeight() - 100) / 4;
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
                texture = new Texture("geeses-bg.png");
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

    private void renderGameOverScreen() {
        batch.draw(gameOverMiniGame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        // Render the shop

    }

    @Override
    public void dispose() {
        batch.dispose();
        boatPicture.dispose();
        lobbyImage.dispose();
        accelerationBarRectangle.dispose();
        accelerationBarBackground.dispose();
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
