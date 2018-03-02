package com.example.benji.soundmanipulator;


public abstract class Box implements Runnable {

    boolean isActive;
    private CableManager manager;

    Box(CableManager manager) {
        this.isActive = false;
        this.manager = manager;
    }

    @Override
    public void run() {
        this.isActive = true;
        on();
    }
    abstract void on();

    public void off() {
        this.isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

}
