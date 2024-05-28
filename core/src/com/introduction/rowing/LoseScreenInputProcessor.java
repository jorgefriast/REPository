package com.introduction.rowing;

import com.badlogic.gdx.Input;

public class LoseScreenInputProcessor extends InputProcessor {
    public LoseScreenInputProcessor(MyRowing myRowing) {
        super(myRowing);
    }

    @Override
    public boolean keyDown(int keycode) {
        setGameState(GameState.LOBBY);
        return false;
    }
}
