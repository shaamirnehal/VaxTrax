package com.example.vaxtrax;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Vaccines {

    private String name;
    private String type;
    private String stage;
    private String info;
    private Drawable display;

    Vaccines    (String name, String type, String stage, String info, Drawable display) {
        this.name = name;
        this.type = type;
        this.stage = stage;
        this.info = info;
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Drawable getDisplay()  {
        return display;
    }

    public void setDisplay(Drawable display)  {
        this.display = display;
    }
}