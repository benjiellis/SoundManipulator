package com.example.benji.soundmanipulator;


import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioInterface implements Runnable {
    private ConcurrentLinkedQueue<short[]> input;
    private AudioTrack track;
    private boolean isActive;
    private SeekBar volumeBar;

    AudioInterface(SeekBar volumeBar, ConcurrentLinkedQueue<short[]> input) {
        WaveSpecs spec = new WaveSpecs();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.isActive = false;
        this.input = input;
        this.volumeBar = volumeBar;
    }

    public boolean isActive() {
        return isActive;
    }

    public void on() {
        isActive = true;
        track.play();
        while(isActive) {
            short[] data = input.poll();
            if (data == null) {
            }
            else {
                for (short dat : data) {
                    double volume = volumeBar.getProgress() / 100.0;
                    dat *= volume;
                    short[] buffer = {dat};
                    track.write(buffer, 0, buffer.length);
                }
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
