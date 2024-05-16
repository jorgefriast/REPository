package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class Branch extends Obstacle{
    public Branch(Position position, int width, int height, Texture image) {
        super(position, width, height, image, 20, 25);
    }
}
