package com.introduction.rowing;

import com.badlogic.gdx.Input;

public class LobbyInputProcessor extends InputProcessor {
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.NUM_1:
                gameState = GameState.PLAY_GAME;
                break;
            case Input.Keys.NUM_2:
                gameState = GameState.PLAY_MINI_GAME;
                break;
            case Input.Keys.NUM_3:
                gameState = GameState.ENTER_SHOP;
                break;

        }
        return false;
    }
}
