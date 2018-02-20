package com.example.benji.soundmanipulator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class Sound {
    public static WaveForm getSineTone(int freq, int duration) {
        duration *= 44100;

        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        WaveSpecs spec = new WaveSpecs();

        double[] mSound = new double[duration];
        short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            mSound[i] = Math.sin((2.0*Math.PI * i/(44100/freq)));
            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
        }

        return new WaveForm(mBuffer, spec);
    }
    public static WaveForm getSawTone(int freq, int duration) {
        duration *= 44100;

        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        WaveSpecs spec = new WaveSpecs();

        double[] mSound = new double[duration];
        short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            mSound[i] = 2*(i%(44100/freq))/(44100/freq)-1;
            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
        }

        return new WaveForm(mBuffer, spec);
    }
}
