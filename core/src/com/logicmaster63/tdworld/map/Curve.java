package com.logicmaster63.tdworld.map;

import com.badlogic.gdx.math.Vector3;
import com.logicmaster63.tdworld.tools.Tools;

import java.awt.*;
import java.util.ArrayList;

public class Curve extends Track{

    private Vector3 k0, k1, k2;

    public Curve(double speed, Vector3 k0, Vector3 k1, Vector3 k2) {
        super(speed);
        this.k0 = k0;
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public ArrayList<Vector3> getPoints(int res) {
        ArrayList<Vector3> points = new ArrayList<Vector3>();
        for(int i = 0; i <= res; i++) {
            float t = (float)i / res;
            points.add(Tools.calculateBezierPoint(t, k0, k1, k2));
        }
        return points;
    }
}
