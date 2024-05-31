package com.introduction.rowing;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;

public class SaokoBoat extends Boat {
    private Texture texture;
    public SaokoBoat(int id, Position position, boolean isPlayer, GameInputProcessor inputProcessor) {
        super(id, position, new Texture("boats/saoko.png"), isPlayer, 5, 3, 1, 2, 3, 1, inputProcessor);
        this.texture = new Texture("boats/saoko.png");
    }
}
