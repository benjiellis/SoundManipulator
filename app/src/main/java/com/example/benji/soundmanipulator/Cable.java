package com.example.benji.soundmanipulator;


import java.util.concurrent.ConcurrentLinkedQueue;

public class Cable {
    private ConcurrentLinkedQueue<short[]> buffer;

    Cable() {
        this.buffer = new ConcurrentLinkedQueue<short[]>();
    }

    public ConcurrentLinkedQueue<short[]> getBuffer() {
        return this.buffer;
    }
}
