package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class SaokoBoat extends Boat {
    private static Texture texture = new Texture("boats/saoko.png");
    public SaokoBoat(int id, Position position, boolean isPlayer, GameInputProcessor inputProcessor) {
        super(id, position, SaokoBoat.texture, isPlayer, 5, 3, 1, 2, 3, 1, inputProcessor);
    }
}
