package com.example.benji.soundmanipulator;


import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioInterface implements Runnable {
    private ConcurrentLinkedQueue<short[]> stream;
    private AudioTrack track;
    private boolean isActive;

    AudioInterface(ConcurrentLinkedQueue<short[]> input) {
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
            short[] data = stream.poll();
            if (data == null) {
            }
            else {
                track.write(data, 0, data.length);
            }

        }
    }

    public void off() {
        isActive = false;
    }

    @Override
    public void run() {
        this.on();
    }
}
