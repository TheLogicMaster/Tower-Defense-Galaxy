package com.logicmaster63.tdworld.map.track;

import com.badlogic.gdx.math.Vector3;

import java.util.List;

public abstract class Track {

    private double speed;

    public Track(double speed) {
        this.speed = speed;
    }

    public abstract List<Vector3> getPoints(int res);
}
