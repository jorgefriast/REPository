package com.introduction.rowing;

import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {
    private static GameState gameState = GameState.LOBBY;

    public static void setGameState(GameState gameState) {
        InputProcessor.gameState = gameState;
    }

    public static GameState getGameState() {
        return gameState;
    }
}
