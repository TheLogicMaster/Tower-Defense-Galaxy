package com.logicmaster63.tdgalaxy.map.region;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

public class SphereRegion extends Region {

    private float radius;

    public SphereRegion(Vector3 pos, float radius) {
        super(pos);
        this.radius = radius;
    }

    @Override
    public btCollisionObject createCollisionObject() {
        btCollisionObject collisionObject = new btCollisionObject();
        collisionObject.setCollisionShape(new btSphereShape(radius));
        collisionObject.setWorldTransform(new Matrix4().setTranslation(pos));
        return collisionObject;
    }

    @Override
    public boolean test(float x, float y, float z) {
        return Math.pow(pos.x - x, 2) + Math.pow(pos.y - y, 2) + Math.pow(pos.z - z, 2) < Math.pow(radius, 2) + 1;
    }
}
