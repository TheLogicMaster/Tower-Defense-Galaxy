package com.logicmaster63.tdworld.map;

import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

import java.util.List;

public class PlanetWorld extends World{

    public PlanetWorld(List<Region> regions, btCollisionWorld world) {
        super(regions, world);
    }
}
