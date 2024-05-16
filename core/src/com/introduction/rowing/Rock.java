package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class Rock extends Obstacle{
    public Rock(Position position, int width, int height, Texture image) {
        super(position, width, height, image, 35, 50);
    }
}
