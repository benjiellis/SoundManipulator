package com.example.benji.soundmanipulator;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;

public abstract class BoxView extends ConstraintLayout {

    public Switch getPower() {
        return power;
    }
    public void setPower(Switch power) {
        this.power = power;
    }

    private Switch power;

    private Thread thread;

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    private Box box;

    public BoxView(Context context) {
        super(context, null, 0);

        init(context);
        _init(context);
    }

    public BoxView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        init(context);
        _init(context);
    }

    public BoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
        _init(context);
    }

    private void _init(Context context) {

        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position

                if (power.isChecked()) {
                    thread = new Thread(box);
                    thread.start();
                }
                else {
                    box.off();
                }

            }
        });

    }

    abstract void init(Context context);

}
