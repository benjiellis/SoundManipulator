package com.example.benji.soundmanipulator;


import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PCMStream {
    private ConcurrentLinkedQueue<short[]> buffer = new ConcurrentLinkedQueue<>();

    PCMStream() {
        //
    }

    public void in(short[] input) {
        buffer.add(input);
    }

    public void in(short input) {
        short[] list = {input};
        buffer.add(list);
    }

    public short[] out() {

        short[] output = buffer.poll();
        if (output == null) {
            short[] zero = {0};
            return zero;
        }
        return output;
    }

}
