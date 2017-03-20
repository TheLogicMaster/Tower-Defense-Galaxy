package com.logicmaster63.tdworld.enemy;

public abstract class Enemy {

    private double x, y, speeed;

    public Enemy(double x, double y, double speeed) {
        this.x = x;
        this.y = y;
        this.speeed = speeed;
    }

    public abstract void tick(float delta);

    public abstract void render(float delta);
}
