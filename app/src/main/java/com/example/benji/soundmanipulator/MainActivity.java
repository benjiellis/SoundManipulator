package com.example.benji.soundmanipulator;

import android.content.DialogInterface;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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
    AudioInterface amp;
    SeekBar sine_freq_bar;
    Switch oscSwitch;
    Switch mixerSwitch;
    Switch waveTypeSwitch;
    SeekBar volumeBar;

    Thread oscThread;
    Thread ampThread;

    ConcurrentLinkedQueue<short[]> cable = new ConcurrentLinkedQueue<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // v importante

        oscSwitch = (Switch) this.findViewById(R.id.osc_switch);
        sine_freq_bar = (SeekBar) this.findViewById(R.id.sine_freq_bar);
        mixerSwitch = (Switch) this.findViewById(R.id.mixer_switch);
        waveTypeSwitch = (Switch) this.findViewById(R.id.wavetype_switch);
        volumeBar = (SeekBar) this.findViewById(R.id.volume_seek_bar);

        mixerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (mixerSwitch.isChecked()) {
                    amp = new AudioInterface(volumeBar, cable);
                    ampThread = new Thread(amp);
                    ampThread.start();
                }
                else {
                    amp.off();
                }

            }
        });

        oscSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (oscSwitch.isChecked()) {
                    osc = new Oscillator(sine_freq_bar, WAVETYPE.SINE, cable);
                    oscThread = new Thread(osc);
                    oscThread.start();
                }
                else {
                    osc.end();
                }

            }
        });



    } // end OnCreate

} // end class MainActivity