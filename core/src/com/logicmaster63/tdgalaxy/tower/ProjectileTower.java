package com.logicmaster63.tdgalaxy.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.entity.Template;
import com.logicmaster63.tdgalaxy.projectiles.Projectile;

import java.util.ArrayList;
import java.util.EnumSet;

public class ProjectileTower extends Tower{

    private Template<? extends Projectile> projectileTemplate;

    public ProjectileTower(Vector3 pos, int hp, int health, int range, float coolDown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, String attackAnimation, Template<? extends Projectile> projectileTemplate, Vector3 attackOffset) {
        super(pos, hp, health, range, coolDown, types, effects, instance, shape, world, entities, attackAnimation, attackOffset);
        this.projectileTemplate = projectileTemplate;
    }

    @Override
    public void attack(ArrayList<Entity> targets) {
        super.attack(targets);
        try {
            Projectile projectile = projectileTemplate.create(new Vector3(pos), new Vector3(targets.get(0).getPos()).sub(pos).setLength(1));
            if(projectile != null)
                entities.put(getNextIndex(), projectile);//projectile.getClass().getConstructor(projectile.getClass(), Vector3.class, Vector3.class).newInstance(projectile, new Vector3(pos), new Vector3(targets.get(0).getPos()).sub(pos).scl(projectile.getClass().getField("SPEED").getInt(null))));
            else
                Gdx.app.error("ProjectileTower", "Failed to create " + projectileTemplate);
        } catch (Exception e) {
            Gdx.app.error("ProjectileTower", e.getMessage());
        }
    }
}
