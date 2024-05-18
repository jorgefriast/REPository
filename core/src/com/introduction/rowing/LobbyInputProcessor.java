package com.introduction.rowing;

import com.badlogic.gdx.Input;

public class LobbyInputProcessor extends InputProcessor {
    public LobbyInputProcessor(MyRowing myRowing) {
        super(myRowing);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.NUM_1:
                setGameState(GameState.PLAY_GAME);
                break;
            case Input.Keys.NUM_2:
                setGameState(GameState.PLAY_MINI_GAME);
                break;
            case Input.Keys.NUM_3:
                setGameState(GameState.ENTER_SHOP);
                break;

        }
        return false;
    }
}
