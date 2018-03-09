package com.example.benji.soundmanipulator;

import android.view.View;


public class PortOnLongClickListener implements View.OnLongClickListener {

    private Port port;

    PortOnLongClickListener(Port port) {
        this.port = port;
    }

    @Override
    public boolean onLongClick(View v) {
        port.getLink().detach();
        return true;
    }
}
