package com.introduction.rowing;

import com.badlogic.gdx.Input;

public class ShopInputProcessor extends InputProcessor {


    public ShopInputProcessor(MyRowing myRowing) {
        super(myRowing);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                myRowing.previousBoat();
                myRowing.previousShopPowerup();
                break;
            case Input.Keys.RIGHT:
                myRowing.nextBoat();
                myRowing.nextShopPowerup();
                break;
            case Input.Keys.ESCAPE:
                setGameState(GameState.LOBBY);
                break;
            case Input.Keys.ENTER:
                myRowing.buyOrSelectBoat();
                break;
            case Input.Keys.DOWN:
            case Input.Keys.UP:
                InputProcessor.switchGameSubState();
                break;


        }
        return false;
    }
}
