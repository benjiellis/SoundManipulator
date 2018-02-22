package com.example.benji.soundmanipulator;

import android.content.DialogInterface;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ConcurrentLinkedQueue;

import static android.widget.SeekBar.*;

public class MainActivity extends AppCompatActivity {

    private static final String RIFF_HEADER = "RIFF";
    private static final String WAVE_HEADER = "WAVE";
    private static final String FMT_HEADER = "fmt ";
    private static final String DATA_HEADER = "data";

    private static final int HEADER_SIZE = 44;

    private static final String CHARSET = "ASCII";

    Oscillator osc;
    AudioInterface output;
    SeekBar sine_freq_bar;
    Button playSine;
    Button stopSine;
    Switch mixerSwitch;
    Switch waveTypeSwitch;
    SeekBar volumeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // v importante

        playSine = (Button) this.findViewById(R.id.play_sine);
        stopSine = (Button) this.findViewById(R.id.stop_sine);
        sine_freq_bar = (SeekBar) this.findViewById(R.id.sine_freq_bar);
        mixerSwitch = (Switch) this.findViewById(R.id.mixer_switch);
        waveTypeSwitch = (Switch) this.findViewById(R.id.wavetype_switch);
        volumeBar = (SeekBar) this.findViewById(R.id.volume_seek_bar);

        playSine.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConcurrentLinkedQueue<short[]> cable = new ConcurrentLinkedQueue<>();
                        output = new AudioInterface(cable);
                        Thread test = new Thread(output);
                        test.start();

                        osc = new Oscillator(sine_freq_bar, WAVETYPE.SINE, cable);
                        Thread test2 = new Thread(osc);
                        test2.start();
                        Log.d("FINISH", "playSine OnClick complete");
                    }
                }
        );
        stopSine.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

    } // end OnCreate

} // end class MainActivity