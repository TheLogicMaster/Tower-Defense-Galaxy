package com.logicmaster63.tdgalaxy.projectiles;

import com.badlogic.gdx.Gdx;
import com.logicmaster63.tdgalaxy.entity.EntityTemplate;
import com.logicmaster63.tdgalaxy.tools.Tools;

import java.lang.reflect.Constructor;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class ProjectileTemplate<T extends Projectile> extends EntityTemplate {

    public ProjectileTemplate(Constructor<T> entity, Object... constants) {
        super(entity, constants);
    }

    @Override
    public T create(Object... args) {
        try {
            return ((T) constructor.newInstance(Tools.concatenate(args, constants)));
        } catch (Exception e) {
            Gdx.app.error("Template", e.getMessage());
        }
        return null;
    }
}