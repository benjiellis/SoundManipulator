package com.example.benji.soundmanipulator;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioInterface extends Box {
    public Port getInput1() {
        return input1;
    }

    public Port getInput2() {
        return input2;
    }

    //    private ConcurrentLinkedQueue<double[]> input1;
//    private ConcurrentLinkedQueue<double[]> input2;
    private Port input1;
    private Port input2;
    private AudioTrack track;
    private SeekBar volumeBar;
    private int bufferSize;

    AudioInterface(SeekBar volumeBar, CableManager manager, Button input1Btn, Button input2Btn) {
        super(manager);
        WaveSpecs spec = new WaveSpecs();
        this.track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);
        this.bufferSize = AudioTrack.getMinBufferSize(22050,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        this.input2 = new Port("input1", false, manager, input2Btn);
        this.input1 = new Port("input2", false, manager, input1Btn);
        this.volumeBar = volumeBar;
    }

    public void on() {
        track.play();
        while(isActive) {
            double[] data1 = input1.getLinkBuffer().poll();
            double[] data2 = input2.getLinkBuffer().poll();

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

        } // while(isActive)

    } // on()

} // class AudioInterface
