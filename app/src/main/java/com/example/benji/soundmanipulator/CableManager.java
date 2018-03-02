package com.example.benji.soundmanipulator;


public class CableManager {
    private Port onDeck;

    CableManager() {
        this.onDeck = null;
    }

    public void setOnDeck(Port onDeck) {
        this.onDeck = onDeck;
    }

    public Port getOnDeck(Port onDeck) {
        return this.onDeck;
    }

    public void send(Port toSend) {
        if (onDeck == null) {
            onDeck = toSend;
        }
        else {
            Port.link(onDeck, toSend);
            onDeck = null;
        }
    }
}
