package com.logicmaster63.tdgalaxy.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.entity.EntityTemplate;
import com.logicmaster63.tdgalaxy.projectiles.Projectile;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class ProjectileTower extends Tower{

    private EntityTemplate<? extends Projectile> projectileEntityTemplate;

    public ProjectileTower(Matrix4 transform, int hp, int health, int range, float coolDown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, String attackAnimation, EntityTemplate<? extends Projectile> projectileEntityTemplate, Vector3 attackOffset, Map<String, Sound> sounds) {
        super(transform, hp, health, range, coolDown, types, effects, instance, shape, world, entities, attackAnimation, attackOffset, sounds);
        this.projectileEntityTemplate = projectileEntityTemplate;
    }

    @Override
    public void attack(ArrayList<Entity> targets) {
        super.attack(targets);
        try {
            Projectile projectile = projectileEntityTemplate.create(new Matrix4(transform), targets.get(0).getTransform().getTranslation(new Vector3()).sub(transform.getTranslation(tempVector)).setLength(1));
            if(projectile == null)
                Gdx.app.error("ProjectileTower", "Failed to create " + projectileEntityTemplate);
        } catch (Exception e) {
            Gdx.app.error("ProjectileTower", e.getMessage());
        }
    }
}
