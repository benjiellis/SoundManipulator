package com.example.benji.soundmanipulator;


import java.util.concurrent.ConcurrentLinkedQueue;

public class Cable {
    private ConcurrentLinkedQueue<double[]> buffer;

    public Port getInPort() {
        return inPort;
    }

    public void setInPort(Port inPort) {
        this.inPort = inPort;
    }

    public Port getOutPort() {
        return outPort;
    }

    public void setOutPort(Port outPort) {
        this.outPort = outPort;
    }

    private Port inPort;
    private Port outPort;

//    public boolean isInputTaken() {
//        return inputTaken;
//    }
//
//    public void setInputTaken(boolean inputTaken) {
//        this.inputTaken = inputTaken;
//    }
//
//    public boolean isOutputTaken() {
//        return outputTaken;
//    }
//
//    public void setOutputTaken(boolean outputTaken) {
//        this.outputTaken = outputTaken;
//    }

    public boolean hasInput() {
        return inPort != null;
    }

    public boolean hasOutput() {
        return outPort != null;
    }

    private boolean inputTaken;
    private boolean outputTaken;

    Cable() {
        this.inPort = null;
        this.outPort = null;
        this.buffer = new ConcurrentLinkedQueue<double[]>();
    }

    public void detach() {
        if (hasInput()) {
            this.inPort.setLink();
            this.inPort = null;
        }
        if (hasOutput()) {
            this.outPort.setLink();
            this.outPort = null;
        }
    }

    public ConcurrentLinkedQueue<double[]> getBuffer() {
        return this.buffer;
    }
}
