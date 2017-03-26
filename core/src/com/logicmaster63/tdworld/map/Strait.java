package com.logicmaster63.tdworld.map;

import com.badlogic.gdx.math.Vector3;

import java.awt.*;
import java.util.ArrayList;

public class Strait extends Track{

    private Vector3 point;

    public Strait(double speed, Vector3 point) {
        super(speed);
        this.point = point;
    }

    @Override
    public ArrayList<Vector3> getPoints(int res) {
        ArrayList<Vector3> points = new ArrayList<Vector3>();
        points.add(point);
        return points;
    }
}
