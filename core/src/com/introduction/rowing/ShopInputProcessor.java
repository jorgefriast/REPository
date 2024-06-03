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
            case Input.Keys.H:
            case Input.Keys.A:
                myRowing.previousBoat();
                myRowing.previousShopPowerup();
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.L:
            case Input.Keys.D:
                myRowing.nextBoat();
                myRowing.nextShopPowerup();
                break;
            case Input.Keys.ESCAPE:
                setGameState(GameState.LOBBY);
                break;
            case Input.Keys.ENTER:
                switch (InputProcessor.getShopSubStates()) {
                    case POWERUPS:
                        myRowing.buyPowerup();
                        break;
                    case BOATS:
                        myRowing.buyOrSelectBoat();
                        break;
                }
                myRowing.buyOrSelectBoat();
                break;
            case Input.Keys.DOWN:
            case Input.Keys.UP:
            case Input.Keys.J:
            case Input.Keys.K:
            case Input.Keys.W:
            case Input.Keys.S:
                InputProcessor.switchGameSubState();
                break;


        }
        return false;
    }
}
