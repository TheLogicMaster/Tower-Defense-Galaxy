package com.logicmaster63.tdworld.map;

import java.awt.*;
import java.util.ArrayList;

public class Strait extends Track{

    private Point k0, k1, k2;

    public Strait(double speed) {
        super(speed);
    }

    @Override
    public ArrayList<Point> getPoints(int res) {
        ArrayList<Point> points = new ArrayList<Point>();
        for(int i = 0; i <= res; i++) {
            double t = (double) i / res;
            points.add(new Point());
            points.get(i).setLocation(Math.pow(1 - t, 2) * 2 * k0.getX() + 2 * (1 - t) * t * k1.getX() + Math.pow(t, 2) * k2.getX(), Math.pow(1 - t, 2) * 2 * k0.getY() + 2 * (1 - t) * t * k1.getY() + Math.pow(t, 2) * k2.getY());
        }
        return points;
    }
}
