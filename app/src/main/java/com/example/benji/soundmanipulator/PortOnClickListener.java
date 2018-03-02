package com.example.benji.soundmanipulator;

import android.view.View;

public class PortOnClickListener implements View.OnClickListener {

    private CableManager manager;
    private Port onDeck;

    public PortOnClickListener(CableManager manager, Port onDeck) {
        this.manager = manager;
        this.onDeck = onDeck;
    }

    @Override
    public void onClick(View v)
    {
        manager.send(onDeck);
    }

};