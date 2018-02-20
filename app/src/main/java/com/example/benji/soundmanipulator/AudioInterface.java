package com.example.benji.soundmanipulator;


import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.IOException;

public class AudioInterface extends Thread {
    private PCMStream stream;
    private AudioTrack track;
    private boolean isActive;

    AudioInterface(PCMStream input) {
        WaveSpecs spec = new WaveSpecs();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.isActive = false;
        this.stream = input;
    }

    public void on() {
        isActive = true;
        track.play();
        while(isActive) {
            if (!stream.empty()) {
                track.write(stream.out(), 0, 1);
            }
        }
    }

    public void off() {
        isActive = false;
    }

    public void in(short[] input) throws IOException {
        if (!isActive) {
            throw new IOException();
        }
        stream.in(input);
    }

    @Override
    public void run() {
        this.on();
    }
}
