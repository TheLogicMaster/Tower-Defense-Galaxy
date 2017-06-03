package com.logicmaster63.tdworld.map.world;

import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;

import java.util.List;

public class PlanetWorld extends World{

    public PlanetWorld(List<com.logicmaster63.tdworld.map.region.Region> regions, btCollisionWorld world) {
        super(regions, world);
    }
}
