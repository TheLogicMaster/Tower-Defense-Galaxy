package com.logicmaster63.tdworld.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.logicmaster63.tdworld.object.Entity;
import com.logicmaster63.tdworld.projectiles.Projectile;

import java.util.ArrayList;
import java.util.Map;

public class ProjectileTower extends Tower{

    private Projectile projectile;

    public ProjectileTower(Vector3 pos, int hp, int range, float coolDown, int types, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, Map<Integer, Entity> objects, String attackAnimation, Projectile projectile, Vector3 attackOffset, boolean isTemplate) {
        this(pos, hp, hp, range, coolDown, types, instance, shape, 0, world, objects, attackAnimation, projectile, attackOffset, isTemplate);
    }

    public ProjectileTower(Vector3 pos, int hp, int health, int range, float coolDown, int types, ModelInstance instance, btCollisionShape shape, int effects, btCollisionWorld world, Map<Integer, Entity> objects, String attackAnimation, Projectile projectile, Vector3 attackOffset, boolean isTemplate) {
        super(pos, hp, health, range, coolDown, types, instance, shape, effects, world, objects, attackAnimation, attackOffset, isTemplate);
        this.projectile = projectile;
    }

    @Override
    public void attack(ArrayList<Entity> targets) {
        super.attack(targets);
        try {
            objects.put(getNextIndex(), projectile.getClass().getConstructor(projectile.getClass(), Vector3.class, Vector3.class).newInstance(projectile, new Vector3(pos), new Vector3(targets.get(0).getPos()).sub(pos).scl(projectile.getClass().getField("SPEED").getInt(null))));
        } catch (Exception e) {
            Gdx.app.error("ProjectileTower", e.getMessage());
        }

    }
}
