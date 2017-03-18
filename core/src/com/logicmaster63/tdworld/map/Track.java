package com.logicmaster63.tdworld.map;

import java.awt.*;
import java.util.ArrayList;

public abstract class Track {

    private double speed;

    public Track(double speed) {
        this.speed = speed;
    }

    public abstract ArrayList<Point> getPoints(int res);
}
