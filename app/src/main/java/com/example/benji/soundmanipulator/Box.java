package com.example.benji.soundmanipulator;


import android.media.AudioFormat;
import android.media.AudioTrack;

public abstract class Box implements Runnable {
    private boolean isActive;

    public int getMinBufferSize() {
        return minBufferSize;
    }

    public int getSampleRate() { return 22050; }

    private int minBufferSize;

    Box() {
        this.minBufferSize = AudioTrack.getMinBufferSize(22050,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
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
