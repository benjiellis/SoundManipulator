package com.example.benji.soundmanipulator;


import java.io.IOException;

public class PCMStream {
    private final int BUFFER_LENGTH = 44100;

    public short[] getBuffer() {
        return buffer;
    }

    public void setBuffer(short[] buffer) {
        this.buffer = buffer;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    private short[] buffer = new short[BUFFER_LENGTH];
    private int location;

    PCMStream(short[] buffer) throws IOException {
        this.location = 0;
        this.in(buffer);
    }

    PCMStream() {
        this.buffer = new short[BUFFER_LENGTH];
    }

    public void in(short[] input) throws IOException {

        // include bounds checking
        if (location + input.length >= 44100) {
            throw new IOException();
        }

        for (short in : input) {
            buffer[location] = in;
            location += 1;
        }
    }

    public void in(short input) throws IOException {
        short[] list = {input};
        in(list);
    }

    public short[] out() {

        short output = buffer[location];
        location -= 1;
        short[] list = new short[1];
        list[0] = output;
        return list;
    }

    public boolean empty() {
        return (location == 0);
    }
}
