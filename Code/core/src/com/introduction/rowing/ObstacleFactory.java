package com.introduction.rowing;

import com.badlogic.gdx.graphics.Texture;

public class ObstacleFactory {
    public static Obstacle createObstacle(String type, Position position, int width, int height, Texture image) {
        switch (type) {
            case "Rock":
                return new Rock(position, width, height, image);
            case "Gees":
                return new Gees(position, width, height, image);
            case "Branch":
                return new Branch(position, width, height, image);
            default:
                throw new IllegalArgumentException("Unknown obstacle type: " + type);
        }
    }
}
