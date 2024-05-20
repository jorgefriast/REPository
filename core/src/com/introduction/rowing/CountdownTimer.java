package com.introduction.rowing;

public class CountdownTimer {
    private float time;
    private final float basicTime;
    private boolean running;

    public CountdownTimer(float time) {
        this.time = time;
        this.basicTime = time;
        this.running = false;
    }

    public void start() {
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    public void reset() {
        this.time = basicTime;
    }

    public void update(float delta) {
        if (running) {
            this.time -= delta;
        }
    }

    public boolean isRunning() {
        return running;
    }

    public float getTime() {
        return time;
    }
}
