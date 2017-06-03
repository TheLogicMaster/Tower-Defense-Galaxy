package com.logicmaster63.tdworld.map.track;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Strait extends Track{

    private Vector3 point;

    public Strait(double speed, Vector3 point) {
        super(speed);
        this.point = point;
    }

    @Override
    public List<Vector3> getPoints(int res) {
        List<Vector3> points = new ArrayList<Vector3>();
        points.add(point);
        return points;
    }
}
