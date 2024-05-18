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

import java.util.ArrayList;
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
        tiles = new Texture("tile.jpg");
        dragonHead = new Texture("head-removebg.png");
        gameOverMiniGame = new Texture("GameOver.png");
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
        int difficulty = 1;
        String gameTitle = "Tile master";
        GlyphLayout layout = new GlyphLayout(font, gameTitle);
        float titleWidth = layout.width;
        font.draw(batch, gameTitle, (Gdx.graphics.getWidth() - titleWidth) / 2, Gdx.graphics.getHeight() - 50);

        String coinsEarned = "Coins: "+ money + " ";
        font.draw(batch, coinsEarned, 10, Gdx.graphics.getHeight() - 40);

        String countdown = "Time: " + (int) (countdownTimer.getTime() + 1) +"s"; // Replace countdownVariable with your actual variable
        GlyphLayout countDownLayout = new GlyphLayout(font, countdown);
        float countdownWidth = countDownLayout.width;

        font.draw(batch, countdown, Gdx.graphics.getWidth() - countdownWidth - 40, Gdx.graphics.getHeight() - 40);
        // Render the mini-game
        switch (miniGameState){
            case NOT_STARTED:
                // Render the mini-game not started
                for (int i = 0; i < 4; i++){
                    for (int j = 0; j < 4; j++){
                        int tileWidth = Gdx.graphics.getWidth() / 4;
                        float tileHeight = (float) (Gdx.graphics.getHeight() - 100) / 4;
                        float x = i * tileWidth;
                        float y = (3-j) * tileHeight;
                        batch.draw(tiles, x, y, tileWidth, tileHeight);
                    }
                }
                countdownTimer.start();
                countdownTimer.update(Gdx.graphics.getDeltaTime());
                if (countdownTimer.getTime() <= 0){
                    countdownTimer.reset();
                    randomObstacle = MathUtils.random(1, 4);
                    miniGameState = MiniGameState.HIDING_ITEMS;
                }
                break;
            case HIDING_ITEMS:
                // Render the mini-game hiding items
                countdownTimer.start();
                countdownTimer.update(Gdx.graphics.getDeltaTime());
                if (countdownTimer.getTime() <= 0){
                    miniGameState = MiniGameState.SHOWING_ITEMS;
                    countdownTimer.reset();
                    itemTiles = new ArrayList<>();
                    for(int i = 0; i < 16; i++){
                        itemTiles.add(i, null);
                    }
                }
                for (int i = 0; i < 4; i++){
                    for (int j = 0; j < 4; j++){
                        int tileWidth = Gdx.graphics.getWidth() / 4;
                        float tileHeight = (float) (Gdx.graphics.getHeight() - 100) / 4;
                        float x = i * tileWidth;
                        float y = (3-j) * tileHeight;
                        batch.draw(tiles, x, y, tileWidth, tileHeight);
                    }
                }

                switch (randomObstacle){
                    case 1:
                        Texture rockTexture = new Texture("rock.png");
                        Rock rockItem = new Rock(new Position((Gdx.graphics.getWidth() - rockTexture.getWidth()/2), (Gdx.graphics.getHeight()- rockTexture.getHeight()/2)), 100, 100, rockTexture);
                        batch.draw(rockItem.getImage(), rockItem.getPosition().getX(), rockItem.getPosition().getY(), rockItem.getWidth(), rockItem.getHeight());

                        break;
                    case 2:
                        Texture geeseTexture = new Texture("geeses.png");
                        Gees geeseItem = new Gees(new Position((Gdx.graphics.getWidth() - geeseTexture.getWidth()/2), (Gdx.graphics.getHeight()- geeseTexture.getHeight()/2)), 100, 100, geeseTexture);
                        batch.draw(geeseItem.getImage(), geeseItem.getPosition().getX(), geeseItem.getPosition().getY(), geeseItem.getWidth(), geeseItem.getHeight());
                        break;
                    case 3:
                        Texture duckTexture = new Texture("duck.jpg");
                        Ducks duckItem = new Ducks(new Position((Gdx.graphics.getWidth() - duckTexture.getWidth()/2), (Gdx.graphics.getHeight()- duckTexture.getHeight()/2)), 100, 100, duckTexture);
                        batch.draw(duckItem.getImage(), duckItem.getPosition().getX(), duckItem.getPosition().getY(), duckItem.getWidth(), duckItem.getHeight());
                        break;
                    case 4:
                        Texture logTexture = new Texture("wood.png");
                        Branch logItem = new Branch(new Position((Gdx.graphics.getWidth() - logTexture.getWidth()/2) , (Gdx.graphics.getHeight()- logTexture.getHeight()/2)), 100, 100, logTexture);
                        batch.draw(logItem.getImage(), logItem.getPosition().getX(), logItem.getPosition().getY(), logItem.getWidth(), logItem.getHeight());
                        break;
                    default:
                        break;
                }
                break;
            case SHOWING_ITEMS:
                // Render the mini-game showing items
                int correctTileCount = MathUtils.random(2, 4);
                while(correctTileCount > 0){
                    int randomTile = MathUtils.random(0, 15);
                    System.out.println(itemTiles.get(randomTile));
                    if (itemTiles.get(randomTile) == null){
                        int tileWidth = Gdx.graphics.getWidth() / 4;
                        float tileHeight = (float) (Gdx.graphics.getHeight() - 100) / 4;
                        int row = randomTile % 4;
                        int column = randomTile / 4;

                        float x = column * tileWidth;
                        float y = (3 - row) * tileHeight;
                        Texture correctTileTexture = new Texture("tile.jpg");

                        switch(randomObstacle){
                            case 1:
                                 correctTileTexture = new Texture("rock_tile.png");
                                break;
                            case 2:
                                 correctTileTexture = new Texture("geese_tile.png");
                                break;
                            case 3:
                                 correctTileTexture = new Texture("duck_tile.png");
                                break;
                            case 4:
                                 correctTileTexture = new Texture("log_tile.png");
                                break;
                            default:
                                break;
                        }

                        itemTiles.set(randomTile, new Entity(new Position((int) x, (int) y), tileWidth, (int) tileHeight, correctTileTexture));
                        correctTileCount--;
                    }
                }
                for(int i = 0; i< itemTiles.size(); i++){
                    if (itemTiles.get(i) == null){
                        int tileWidth = Gdx.graphics.getWidth() / 4;
                        float tileHeight = (float) (Gdx.graphics.getHeight() - 100) / 4;
                        int row = i % 4;
                        int column = i / 4;

                        float x = column * tileWidth;
                        float y = (3 - row) * tileHeight;
                        int randomTileIncorrect = MathUtils.random(1, 4);
                        while (randomTileIncorrect == randomObstacle){
                            randomTileIncorrect = MathUtils.random(1, 4);
                        }
                        Texture inCorrectTileTexture = new Texture("tile.jpg");

                        switch(randomTileIncorrect){
                            case 1:
                                inCorrectTileTexture = new Texture("rock_tile.png");
                                break;
                            case 2:
                                inCorrectTileTexture = new Texture("geese_tile.png");
                                break;
                            case 3:
                                inCorrectTileTexture = new Texture("duck_tile.png");
                                break;
                            case 4:
                                inCorrectTileTexture = new Texture("log_tile.png");
                                break;
                            default:
                                break;
                        }

                        itemTiles.set(i, new Entity(new Position((int) x, (int) y), tileWidth, (int) tileHeight, inCorrectTileTexture));
                    }
                }
                miniGameState = MiniGameState.PLAYING;
                break;
            case PLAYING:
                // Render the mini-game playing
                for (Entity itemTile : itemTiles){
                    batch.draw(itemTile.getImage(), itemTile.getPosition().getX(), itemTile.getPosition().getY(), itemTile.getWidth(), itemTile.getHeight());
                }
                countdownTimer.start();
                countdownTimer.update(Gdx.graphics.getDeltaTime());
                if (countdownTimer.getTime() <= 0){
                    miniGameState = MiniGameState.FINISHED;
                    countdownTimer.reset();
                }
                break;
            case FINISHED:
                boolean correctTileClicked = false;
                for (Entity itemTile : itemTiles){
                    if (dragonPlayer.getBounds().intersects(itemTile.getBounds())){
                         switch(randomObstacle){
                            case 1:
                                if (itemTile.getImage().toString().equals("rock_tile.png")){
                                    money += 10;
                                    correctTileClicked = true;
                                }
                                break;
                            case 2:
                                if (itemTile.getImage().toString().equals("geese_tile.png")){
                                    money += 10;
                                    correctTileClicked = true;
                                }
                                break;
                            case 3:
                                if (itemTile.getImage().toString().equals("duck_tile.png")){
                                    money += 10;
                                    correctTileClicked = true;
                                }
                                break;
                            case 4:
                                if (itemTile.getImage().toString().equals("log_tile.png")){
                                    money += 10;
                                    correctTileClicked = true;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                // Render the mini-game finished
                if(correctTileClicked) {
                    miniGameState = MiniGameState.NOT_STARTED;
                }else {
                    miniGameState = MiniGameState.GAME_OVER;
                }
                break;
            case GAME_OVER:
                // Render the mini-game game over
                batch.draw(gameOverMiniGame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                break;
            default:
                break;

        }
        if(miniGameState != MiniGameState.GAME_OVER) {
            dragonPlayer.updateKeys(Gdx.graphics.getDeltaTime());
            batch.draw(dragonPlayer.getImage(), dragonPlayer.position.getX(), dragonPlayer.position.getY(), dragonPlayer.getWidth(), dragonPlayer.getHeight());
        }
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
