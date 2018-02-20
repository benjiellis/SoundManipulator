package com.example.benji.soundmanipulator;

import android.media.AudioFormat;
import android.media.AudioTrack;

class WaveSpecs {
    private int rate;
    private int channels;
    private int format;
    private int minimumBufferSize;

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    private int dataSize;

    public int getMinimumBufferSize() {
        return minimumBufferSize;
    }

    public void setMinimumBufferSize(int minimumBufferSize) {
        this.minimumBufferSize = minimumBufferSize;
    }

    WaveSpecs(int rate, int minimumBufferSize, int channels, int format) {
        this.rate = rate;
        this.minimumBufferSize = minimumBufferSize;
        this.channels = channels;
        this.format = format;
        this.dataSize = 0;
    }

    WaveSpecs() {
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        this.rate = 44100;
        this.minimumBufferSize = mBufferSize;
        this.channels = AudioFormat.CHANNEL_OUT_MONO;
        this.format = AudioFormat.ENCODING_PCM_16BIT;
        this.dataSize = 0;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

}
