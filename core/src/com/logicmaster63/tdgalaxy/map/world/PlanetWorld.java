package com.logicmaster63.tdgalaxy.map.world;

import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdgalaxy.map.region.Region;

import java.util.List;

public class PlanetWorld extends World {

    public PlanetWorld(List<Region> regions, btCollisionWorld world) {
        super(regions, world);
    }
}
