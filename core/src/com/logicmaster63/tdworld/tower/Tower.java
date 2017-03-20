package com.logicmaster63.tdworld.tower;

public abstract class Tower {

    private int x, y;

    public Tower(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void tick(float delta);

    public abstract void render(float delta);
}
