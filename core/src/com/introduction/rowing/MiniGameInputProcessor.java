package com.introduction.rowing;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MiniGameInputProcessor extends InputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.NUM_1:
                break;
            case Input.Keys.NUM_2:
                break;
            case Input.Keys.NUM_3:
                break;
        }
        return false;
    }
}
