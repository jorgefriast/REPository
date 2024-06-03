package com.introduction.rowing;

public class ShopBoat {

    private int id;
    private String name;
    private int price;
    private String imageName;
    private int speedFactor;
    private int acceleration;
    private int robustness;
    private int maneuverability;
    private int momentumFactor;
    private int fatigue;
    private boolean unlocked;
    private boolean selected;

    public ShopBoat(int id, String name, int price, String imageName, int speedFactor, int acceleration, int robustness, int maneuverability, int momentumFactor, int fatigue, boolean unlocked, boolean selected) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageName = imageName;
        this.speedFactor = speedFactor;
        this.acceleration = acceleration;
        this.robustness = robustness;
        this.maneuverability = maneuverability;
        this.momentumFactor = momentumFactor;
        this.fatigue = fatigue;
        this.unlocked = unlocked;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageName() {
        return imageName;
    }

    public int getSpeedFactor() {
        return speedFactor;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public int getRobustness() {
        return robustness;
    }

    public int getManeuverability() {
        return maneuverability;
    }

    public int getMomentumFactor() { return momentumFactor; }

    public int getFatigue() {
        return fatigue;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setUnlocked(boolean b) { this.unlocked = b; }
}
