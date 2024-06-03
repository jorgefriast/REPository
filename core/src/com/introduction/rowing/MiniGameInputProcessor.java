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
            case Input.Keys.W:
            case Input.Keys.K:
                this.direction = 0;
                this.moving = true;
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
            case Input.Keys.H:
                this.direction = 1;
                this.moving = true;
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
            case Input.Keys.J:
                this.direction = 2;
                this.moving = true;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
            case Input.Keys.L:
                this.direction = 3;
                this.moving = true;
                break;
            case Input.Keys.ESCAPE:
                setGameState(GameState.LOBBY);
                myRowing.miniGameState = MiniGameState.NOT_STARTED;
                myRowing.resetMiniGame();
                break;
            case Input.Keys.ENTER:
                if(myRowing.miniGameState == MiniGameState.SUM_SCREEN) {
                    myRowing.miniGameState = MiniGameState.NOT_STARTED;
                    myRowing.resetMiniGame();
                }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        // Handle key up events
        this.moving = false;
        return false;
    }
}
