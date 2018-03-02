package com.example.benji.soundmanipulator;


public abstract class Box implements Runnable {
    private boolean isActive;

    Box() {
        this.isActive = false;
    }

    public boolean isActive() { return isActive; }

    @Override
    public void run() {
        this.isActive = true;
        on();
    }

    abstract void on();

    public void off() { this.isActive = false; }
}
