package com.logicmaster63.tdworld.tools;

public class Vector{

    private double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double dot(Vector vector) {
        return x * vector.getX() + y * vector.getY();
    }

    public Vector normalize() {
        return new Vector(x / magnitude(), y / magnitude());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
}
