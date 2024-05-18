package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class DragonHead extends Entity{
    MiniGameInputProcessor inputProcessor;
    public DragonHead(Position position, int width, int height, Texture image, MiniGameInputProcessor inputProcessor) {
        super(position, width, height, image);
        this.inputProcessor = inputProcessor;
    }

    public void updateKeys(float delta) {
        if (inputProcessor.moving) {
            switch (inputProcessor.direction) {
                case 0:
                    this.position.setY((int) (this.position.getY() + (175 * delta)));
                    break;
                case 1:
                    this.position.setX((int) (this.position.getX() - (150 * delta)));
                    break;
                case 2:
                    this.position.setY((int) (this.position.getY() - (150 * delta)));
                    break;
                case 3:
                    this.position.setX((int) (this.position.getX() + (175 * delta)));
                    break;
            }
        }
    }
}
