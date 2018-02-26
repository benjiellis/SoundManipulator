package com.example.benji.soundmanipulator;


import android.media.AudioFormat;
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
    private ConcurrentLinkedQueue<double[]> input1;
    private ConcurrentLinkedQueue<double[]> input2;
    private AudioTrack track;
    private boolean isActive;
    private SeekBar volumeBar;
    private int bufferSize;

    AudioInterface(SeekBar volumeBar, ConcurrentLinkedQueue<double[]> input1,
                   ConcurrentLinkedQueue<double[]> input2) {

        WaveSpecs spec = new WaveSpecs();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.bufferSize = AudioTrack.getMinBufferSize(22050,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        this.isActive = false;
        this.input1 = input1;
        this.input2 = input2;
        this.volumeBar = volumeBar;
    }

    public boolean isActive() {
        return isActive;
    }

    public void on() {
        isActive = true;
        track.play();
        while(isActive) {
            double[] data1 = input1.poll();
            double[] data2 = input2.poll();
//            if (data1 == null) {
//            }
//            else {
//                for (short dat : data1) {
//                    double volume = volumeBar.getProgress() / 100.0;
//                    dat *= volume;
//                    short[] buffer = {dat};
//                    track.write(buffer, 0, buffer.length);
//                }
//            }
            if (data1 != null && data2 != null) {
                for (int i = 0; i < bufferSize; i++) {
                    double volume = volumeBar.getProgress() / 100.0;
                    data1[i] *= volume;
                    data2[i] *= volume;
                    double dat = data1[i] + data2[i];
                    if (dat < -1) {dat = -1;}
                    if (dat > 1) {dat = 1;}
                    short dat_short = (short) (dat*Short.MAX_VALUE);
                    short[] buffer = {dat_short};
                    track.write(buffer, 0, buffer.length);
                }
            }
            else if (data1 != null) {
                for (double dat : data1) {
                    double volume = volumeBar.getProgress() / 100.0;
                    dat *= volume;
                    short dat_short = (short) (dat*Short.MAX_VALUE);
                    short[] buffer = {dat_short};
                    track.write(buffer, 0, buffer.length);
                }
            }
            else if (data2 != null) {
                for (double dat : data2) {
                    double volume = volumeBar.getProgress() / 100.0;
                    dat *= volume;
                    short dat_short = (short) (dat*Short.MAX_VALUE);
                    short[] buffer = {dat_short};
                    track.write(buffer, 0, buffer.length);
                }
            }


            //track.write(input.poll(), 0, bufferSize);

        }
    }

    public void off() {
        isActive = false;
    }

    @Override
    public void run() {
        this.on();
    }

//    short[] mergeArrays(short[] first, short[] second) {
//        short[] toReturn = new short[first.length];
//        for (int i = 0; i < first.length; i++) {
//            toReturn[i] = first[i] + second[i];
//        }
//    }
}
