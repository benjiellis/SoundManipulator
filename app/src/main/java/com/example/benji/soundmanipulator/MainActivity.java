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

    Oscillator osc1;
    Oscillator osc2;
    AudioInterface amp;
    SeekBar osc1_freqBar;
    SeekBar osc2_freqBar;
    Switch osc1_powerSwitch;
    Switch osc2_powerSwitch;
    Switch mixerSwitch;
    Switch osc1_waveTypeSwitch;
    Switch osc2_waveTypeSwitch;
    Switch fmSwitch;
    SeekBar volumeBar;
    SeekBar osc1_volumeBar;
    SeekBar osc2_volumeBar;
    SeekBar osc1_fmModBar;
    SeekBar osc2_fmModBar;
    SeekBar osc1_volModBar;
    SeekBar osc2_volModBar;

    Thread osc1Thread;
    Thread osc2Thread;
    Thread ampThread;

    Cable osc1Cable = new Cable();
    Cable osc2Cable = new Cable();
    Cable spareCable = new Cable();
    Cable spareCable2 = new Cable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // v importante

        osc1_powerSwitch = (Switch) this.findViewById(R.id.osc1_power);
        osc2_powerSwitch = (Switch) this.findViewById(R.id.osc2_power);
        osc2_freqBar = (SeekBar) this.findViewById(R.id.osc2_freq);
        osc1_freqBar = (SeekBar) this.findViewById(R.id.osc1_freq);
        osc1_volumeBar = (SeekBar) this.findViewById(R.id.osc1_volume);
        osc2_volumeBar = (SeekBar) this.findViewById(R.id.osc2_volume);
        osc1_fmModBar = (SeekBar) this.findViewById(R.id.osc1_fmmod);
        osc2_fmModBar = (SeekBar) this.findViewById(R.id.osc2_fmmod);
        osc1_volModBar = (SeekBar) this.findViewById(R.id.osc1_volmod);
        osc2_volModBar = (SeekBar) this.findViewById(R.id.osc2_volmod);
        mixerSwitch = (Switch) this.findViewById(R.id.mixer_switch);
        osc2_waveTypeSwitch = (Switch) this.findViewById(R.id.osc2_type);
        osc1_waveTypeSwitch = (Switch) this.findViewById(R.id.osc1_type);
        volumeBar = (SeekBar) this.findViewById(R.id.volume_seek_bar);
        fmSwitch = (Switch) this.findViewById(R.id.fm_switch);

        amp = new AudioInterface(volumeBar);
        osc1 = new Oscillator(osc1_freqBar, osc1_volumeBar);
        osc2 = new Oscillator(osc2_freqBar, osc2_volumeBar);

        amp.setInput1(osc1Cable);
        osc1.setOutputCable(osc1Cable);

        amp.setInput2(osc2Cable);
        osc2.setOutputCable(osc2Cable);


        mixerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (mixerSwitch.isChecked()) {
                    ampThread = new Thread(amp);
                    ampThread.start();
                }
                else {
                    amp.off();
                }

            }
        });

        osc1_powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (osc1_powerSwitch.isChecked()) {
                    osc1Thread = new Thread(osc1);
                    osc1Thread.start();
                }
                else {
                    osc1.end();
                }

            }
        });

        osc2_powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (osc2_powerSwitch.isChecked()) {
                    osc2Thread = new Thread(osc2);
                    osc2Thread.start();
                }
                else {
                    osc2.end();
                }

            }
        });

        fmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (fmSwitch.isChecked()) {
                    Cable hook = new Cable();
                    amp.setInput2();
                    osc1.setFMCable(hook);
                    osc2.setOutputCable(hook);
                }
                else {
                    Cable hook = new Cable();
                    osc1.setFMCable();
                    amp.setInput2(hook);
                    osc2.setOutputCable(hook);
                }

            }
        });


    } // end OnCreate

} // end class MainActivity