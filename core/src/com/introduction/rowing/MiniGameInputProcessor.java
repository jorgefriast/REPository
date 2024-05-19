package com.introduction.rowing;

import com.badlogic.gdx.Input;

public class MiniGameInputProcessor extends InputProcessor {

    public boolean moving = false;
    public int direction = 0;

    public MiniGameInputProcessor(MyRowing myRowing) {
        super(myRowing);
    }


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                this.direction = 0;
                this.moving = true;
                break;
            case Input.Keys.LEFT:
                this.direction = 1;
                this.moving = true;
                break;
            case Input.Keys.DOWN:
                this.direction = 2;
                this.moving = true;
                break;
            case Input.Keys.RIGHT:
                this.direction = 3;
                this.moving = true;
                break;
            case Input.Keys.ESCAPE:
                setGameState(GameState.LOBBY);
                myRowing.miniGameState = MiniGameState.NOT_STARTED;
                myRowing.resetMiniGame();
                break;
            case Input.Keys.ENTER:
                if(myRowing.miniGameState == MiniGameState.GAME_OVER) {
                    myRowing.miniGameState = MiniGameState.NOT_STARTED;
                    myRowing.resetMiniGame();
                }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        // Handle key up events
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.RIGHT:
            case Input.Keys.UP:
            case Input.Keys.DOWN:
                this.moving = false;
                break;

        }
        return false;
    }
}
