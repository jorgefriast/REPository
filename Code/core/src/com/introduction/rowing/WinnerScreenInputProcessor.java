package com.introduction.rowing;

import java.util.Objects;

public class WinnerScreenInputProcessor extends InputProcessor{

    public WinnerScreenInputProcessor(MyRowing myRowing) {
        super(myRowing);
    }

    @Override
    public boolean keyDown(int keycode) {
        setGameState(GameState.LOBBY);
        setGameSubState(GameSubState.RACE_LEG);
        this.myRowing.resetGame();
        return false;
    }
}
