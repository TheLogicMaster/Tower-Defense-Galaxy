package com.logicmaster63.tdworld.map;

import java.awt.*;
import java.util.ArrayList;

public class Strait extends Track{

    private Point point;

    public Strait(double speed, Point point) {
        super(speed);
        this.point = point;
    }

    @Override
    public ArrayList<Point> getPoints(int res) {
        ArrayList<Point> points = new ArrayList<Point>();
        points.add(point);
        return points;
    }
}
