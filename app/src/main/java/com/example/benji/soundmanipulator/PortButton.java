package com.example.benji.soundmanipulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.Random;


public class PortButton extends AppCompatButton {

    private Port port;

    PortButton(Context context) {
        super(context);
    }

    public void setPort(Port p) {
        this.port = p;
        defaultColor();
    }

    PortButton(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setColor(RGBColor color) {
        Random rnd = new Random();
        int colorInt = Color.argb(255, color.getRed(), color.getGreen(), color.getBlue());
        Drawable background = this.getBackground();
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(colorInt);
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(colorInt);
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(colorInt);
        }
    }

    public void defaultColor() {
        if (this.port.isOut()) {
            RGBColor black = new RGBColor(0, 0, 0);
            setColor(black);
        }
        if (!this.port.isOut()) {
            RGBColor white = new RGBColor(255, 255, 255);
        }
    }

}
