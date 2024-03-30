package com.introduction.rowing;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate of the position
     * @return the x coordinate of the position
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate of the position
     * @return the y coordinate of the position
     */
    public int getY() {
        return y;
    }

    /**
     * Change the x coordinate of the position
     * @param x the new x coordinate of the position
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Change the y coordinate of the position
     * @param y the new y coordinate of the position
     */
    public void setY(int y) {
        this.y = y;
    }
}
