package com.example.benji.soundmanipulator;


import java.util.concurrent.ConcurrentLinkedQueue;

public class Cable {
    private ConcurrentLinkedQueue<double[]> buffer;

    public boolean isInputTaken() {
        return inputTaken;
    }

    public void setInputTaken(boolean inputTaken) {
        this.inputTaken = inputTaken;
    }

    public boolean isOutputTaken() {
        return outputTaken;
    }

    public void setOutputTaken(boolean outputTaken) {
        this.outputTaken = outputTaken;
    }

    private boolean inputTaken;
    private boolean outputTaken;

    Cable() {
        this.buffer = new ConcurrentLinkedQueue<double[]>();
    }

    public ConcurrentLinkedQueue<double[]> getBuffer() {
        return this.buffer;
    }
}
