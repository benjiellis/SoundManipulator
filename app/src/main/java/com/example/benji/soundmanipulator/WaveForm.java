package com.example.benji.soundmanipulator;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WaveForm {

    public short[] getSound() {
        return sound;
    }

    public void setSound(short[] sound) {
        this.sound = sound;
    }

    public WaveSpecs getSpecs() {
        return specs;
    }

    public void setSpecs(WaveSpecs specs) {
        this.specs = specs;
    }

    private short[] sound;
    private WaveSpecs specs;

    WaveForm(short[] sound, WaveSpecs specs) {
        this.specs = specs;
        this.sound = sound;
    }

    WaveForm() {
        this.sound = new short[1];
        this.specs = new WaveSpecs();
    }

    WaveForm(byte[] sound, WaveSpecs specs) {
        this.sound = byteToShort(sound);
        this.specs = specs;
    }

    // UNTESTED
    short[] byteToShort(byte[] buffer) {
        short[] array = new short[buffer.length / 2];
        for (int i = 0; i < array.length; i++) {
            array[i] = (short)(( buffer[i*2] & 0xff )|( buffer[i*2 + 1] << 8 ));
        }
        return array;
    }

    private static WaveForm loadFromWAV(File file) throws IOException {

        FileInputStream stream = new FileInputStream(file);

        WaveSpecs specs = readWAVHeader(stream);

        byte[] data = new byte[specs.getDataSize()];
        stream.read(data, 0, data.length);


        return new WaveForm(data, specs);
    }

    private static WaveSpecs readWAVHeader(InputStream stream) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(44);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        stream.read(buffer.array(), buffer.arrayOffset(), buffer.capacity());

        buffer.rewind();
        buffer.position(buffer.position() + 20);
        int format = buffer.getShort();
        int channels = buffer.getShort();
        int rate = buffer.getInt();
        buffer.position(buffer.position() + 6);
        int bits = buffer.getShort();
        int dataSize = 0;
        while (buffer.getInt() != 0x61746164) {
            int size = buffer.getInt();
            stream.skip(size);

            buffer.rewind();
            stream.read(buffer.array(), buffer.arrayOffset(), 8);
            buffer.rewind();
        }
        dataSize = buffer.getInt();

        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        WaveSpecs specs = new WaveSpecs(rate, mBufferSize, channels, bits);
        specs.setDataSize(dataSize);
        return specs;
    }

    // load .wav file
    // should eventually support .mp3
    WaveForm(String location) throws IOException {
        File file = new File(location);
        WaveForm temp = WaveForm.loadFromWAV(file);
        this.sound = temp.getSound();
        this.specs = temp.getSpecs();
    }


    public void play() {

        WaveSpecs spec = this.getSpecs();
        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, spec.getRate(),
                spec.getChannels(), spec.getFormat(),
                spec.getMinimumBufferSize(), AudioTrack.MODE_STREAM);

        track.play();
        track.write(this.getSound(), 0, this.getSound().length);
        track.stop();
        track.release();
    }
}
