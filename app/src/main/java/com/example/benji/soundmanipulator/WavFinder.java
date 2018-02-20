package com.example.benji.soundmanipulator;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.content.ContentValues.TAG;

public class WavFinder {
    private String resource_location;
    private AudioTrack track;

    private static final String RIFF_HEADER = "RIFF";
    private static final String WAVE_HEADER = "WAVE";
    private static final String FMT_HEADER = "fmt ";
    private static final String DATA_HEADER = "data";
    private static final int minBuffSize = -1;

    private static final int HEADER_SIZE = 44;

    private static final String CHARSET = "ASCII";

        /* ... */
//    WavFinder(String location) {
//        try {
//            this.resource_location = location;
//            File file = new File(location);
//            FileInputStream stream = new FileInputStream(file);
//            WaveSpecs info = readHeader(stream);
//            byte[] byte_array = readWavPcm(info, stream);
//            track = getMatchingAudioTrack(info);
//
//        }
//        catch (Exception e) {
//            System.out.println("Could not load wav at " + location);
//        }
//    }

    public AudioTrack getAudioTrack() {
        return this.track;
    }

    private AudioTrack getMatchingAudioTrack(WaveSpecs info) {
        AudioTrack toReturn;
        int format = AudioFormat.ENCODING_DEFAULT;
        int channels = AudioFormat.CHANNEL_OUT_MONO;
        if (info.getFormat() == 16) {
            format = AudioFormat.ENCODING_PCM_16BIT;
        }
        if (info.getChannels() == 2) {
            channels = AudioFormat.CHANNEL_OUT_STEREO;
        }

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        AudioFormat formats = new AudioFormat.Builder()
                .setEncoding(format)
                .setSampleRate(info.getRate())
                .setChannelMask(channels)
                .build();
        toReturn = new AudioTrack.Builder()
                .setAudioAttributes(attributes)
                .setAudioFormat(formats)
                .setBufferSizeInBytes(minBuffSize)
                .build();
        return toReturn;
    }

    private static WaveSpecs readHeader(InputStream wavStream)
            throws IOException, DecoderException {

        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        wavStream.read(buffer.array(), buffer.arrayOffset(), buffer.capacity());

        buffer.rewind();
        buffer.position(buffer.position() + 20);
        int format = buffer.getShort();
        checkFormat(format == 1, "Unsupported encoding: " + format); // 1 means Linear PCT
        int channels = buffer.getShort();
        checkFormat(channels == 1 || channels == 2, "Unsupported channels: " + channels);
        int rate = buffer.getInt();
        checkFormat(rate <= 48000 && rate >= 11025, "Unsupported rate: " + rate);
        buffer.position(buffer.position() + 6);
        int bits = buffer.getShort();
        checkFormat(bits == 16, "Unsupported bits: " + bits);
        int dataSize = 0;
        while (buffer.getInt() != 0x61746164) { // "data" marker
            Log.d(TAG, "Skipping non-data chunk");
            int size = buffer.getInt();
            wavStream.skip(size);

            buffer.rewind();
            wavStream.read(buffer.array(), buffer.arrayOffset(), 8);
            buffer.rewind();
        }
        dataSize = buffer.getInt();
        checkFormat(dataSize > 0, "wrong datasize: " + dataSize);

        // fix to match new wavespecs constructor
        return new WaveSpecs(rate, dataSize, channels, format);
    }

//    private static byte[] readWavPcm(WaveSpecs info, InputStream stream) throws IOException {
//        byte[] data = new byte[info)];
//        stream.read(data, 0, data.length);
//        return data;
//    }

    private static void checkFormat(boolean bool, String message) throws DecoderException {
        if (!bool) {
            System.out.println(message);
            DecoderException e = new DecoderException(message);
            throw e;
        }
    }
}

class DecoderException extends Exception {
    DecoderException(String msg) {
        super(msg);
    }
}

