package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

import static com.introduction.rowing.Constants.WINDOW_HEIGHT;
import static com.introduction.rowing.Constants.WINDOW_WIDTH;

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
                    this.position.setY(Math.max(0, Math.min(WINDOW_HEIGHT-height-100, (int) (this.position.getY() + (250 * delta)))));
                    break;
                case 1:
                    this.position.setX(Math.max(0, Math.min(WINDOW_WIDTH, (int) (this.position.getX() - (250 * delta)))));
//                    this.position.setX((int) (this.position.getX() - (250 * delta)));
                    break;
                case 2:
                    this.position.setY(Math.max(0, Math.min(WINDOW_HEIGHT, (int) (this.position.getY() - (250 * delta)))));
//                    this.position.setY((int) (this.position.getY() - (250 * delta)));
                    break;
                case 3:
                    this.position.setX(Math.max(0, Math.min(WINDOW_WIDTH- width, (int) (this.position.getX() + (250 * delta)))));
//                    this.position.setX((int) (this.position.getX() + (250 * delta)));
                    break;
            }
        }
    }
}
