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
                break;
            case Input.Keys.RIGHT:
                myRowing.nextBoat();
                break;
            case Input.Keys.ESCAPE:
                setGameState(GameState.LOBBY);
                break;
            case Input.Keys.P:
                myRowing.buyOrSelectBoat();

        }
        return false;
    }
}
