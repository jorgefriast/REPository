package com.introduction.rowing;

import com.badlogic.gdx.InputAdapter;

public class InputProcessor extends InputAdapter {
    protected MyRowing myRowing;
    public InputProcessor(MyRowing myRowing) {
        this.myRowing = myRowing;
    }
    private static GameState gameState = GameState.LOBBY;
    private static GameSubState gameSubState = GameSubState.RACE_LEG;
    private static ShopSubState shopSubState = ShopSubState.BOATS;

    public static void setGameState(GameState gameState) {
        InputProcessor.gameState = gameState;
    }

    public static void setGameSubState(GameSubState gameSubState) {
        InputProcessor.gameSubState = gameSubState;
    }
    public static GameSubState getGameSubState() { return  gameSubState; }
    public static void setShopSubState(ShopSubState shopSubState) { InputProcessor.shopSubState = shopSubState; }
    public static ShopSubState getShopSubStates(ShopSubState shopSubState) { return shopSubState; }

    public static GameState getGameState() {
        return gameState;
    }
}
