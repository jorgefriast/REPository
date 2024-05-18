package com.introduction.rowing;

import com.badlogic.gdx.Input;

public class ShopInputProcessor extends InputProcessor {

    private MyRowing myRowing;

    public ShopInputProcessor(MyRowing myRowing) {
        this.myRowing = myRowing;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                myRowing.previousBoat();
                break;
            case Input.Keys.RIGHT:
                myRowing.nextBoat();
                break;
        }
        return false;
    }
}
