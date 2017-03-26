package com.logicmaster63.tdworld.map;

import com.badlogic.gdx.math.Vector3;

import java.awt.*;
import java.util.ArrayList;

public abstract class Track {

    private double speed;

    public Track(double speed) {
        this.speed = speed;
    }

    public abstract ArrayList<Vector3> getPoints(int res);
}
