package com.example.benji.soundmanipulator;


import android.view.View;

public class PortOnClickListener implements View.OnClickListener {

    private CableManager manager;
    private Port onDeck;
    private PortButton parent;
    private PortOnClickListener partner;

    public PortButton getParent() { return this.parent; }

    PortOnClickListener(Port onDeck, PortButton parent) {
        this.manager = CableManager.getInstance();
        this.onDeck = onDeck;
        this.parent = parent;
    }

    PortOnClickListener(Port onDeck) {
        this.manager = CableManager.getInstance();
        this.onDeck = onDeck;
        this.parent = null;
    }

    @Override
    public void onClick(View v) {
        if (partner != null) {
            partner.getParent().defaultColor();
        }
        partner = manager.send(onDeck, this);
        if (partner != null) {
            if (this.getParent() != null) {
                this.getParent().setColor(onDeck.getColor());
            }
            if (partner.getParent() != null) {
                partner.getParent().setColor(onDeck.getColor());
            }
            // ??
        }
        else {
            this.getParent().defaultColor();
        }
    }


}
