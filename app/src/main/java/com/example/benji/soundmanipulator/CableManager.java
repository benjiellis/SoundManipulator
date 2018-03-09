package com.example.benji.soundmanipulator;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class CableManager {
    private static CableManager instance;
    private Port onDeck;

    private CableManager() {
        this.onDeck = null;
    }

    public void send(Port toLink) {
        if (onDeck == null) {
            onDeck = toLink;
        }
        else if (onDeck == toLink) {
            Log.d("AV", "Cannot link port with self");
            onDeck = null;
        }
        else {
            onDeck.getLink().detach();
            toLink.getLink().detach();

            Log.d("AV", "Linking two ports.");

            Port.link(onDeck, toLink);
            onDeck = null;
        }
    }

    public static synchronized CableManager getInstance() {
        if (instance == null) {
            instance = new CableManager();
        }
        return instance;
    }

}
