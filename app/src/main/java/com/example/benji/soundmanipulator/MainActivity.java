package com.example.benji.soundmanipulator;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ConcurrentLinkedQueue;

import static android.widget.SeekBar.*;

public class MainActivity extends AppCompatActivity {

    private ListView drawer;
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // v importante

        drawer = (ListView) findViewById(R.id.navList);

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        addDrawerItems();

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast;
                switch (position) {
                    case 0:
                        // add audiointerface to view
                        addAmp();
                        toast = Toast.makeText(MainActivity.this, "Adding Audio Interface", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case 1:
                        // add oscillator to view
                        addOsc();
                        toast = Toast.makeText(MainActivity.this, "Adding Oscillator", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    case 2:
                        // add adsr to view
                        addADSR();
                        toast = Toast.makeText(MainActivity.this, "Adding ADSR", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        });

    } // end OnCreate

    private void addDrawerItems() {
        String[] osArray = { "Audio Interface", "Oscillator", "ADSR" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        drawer.setAdapter(adapter);
    }

    private void addOsc() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // final View device = inflater.inflate(R.layout.oscillator_view, null);
        final OscillatorView device = new OscillatorView(this);
        mainLayout.addView(device);
    }

    private void addAmp() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // final View device = inflater.inflate(R.layout.audio_interface_view, null);
        final AudioInterfaceView device = new AudioInterfaceView(this);
        mainLayout.addView(device);
    }

    private void addADSR() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // final View device = inflater.inflate(R.layout.audio_interface_view, null);
        final ADSRView device = new ADSRView(this);
        mainLayout.addView(device);
    }

    private void deleteView(View v) {
        // v is the button, v.getParent is the device, button should be in device layouts
        mainLayout.removeView((View) v.getParent());
    }

} // end class MainActivity