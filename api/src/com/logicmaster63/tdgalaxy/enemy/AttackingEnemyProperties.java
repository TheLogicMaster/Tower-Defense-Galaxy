package com.logicmaster63.tdgalaxy.enemy;

import com.badlogic.gdx.utils.JsonValue;
import com.logicmaster63.tdgalaxy.entity.EntityProperties;

public class AttackingEnemyProperties extends EnemyProperties {

    public AttackingEnemyProperties(String propertyPath) {
        super(propertyPath);
    }

    @Override
    protected void load(JsonValue properties) {
        super.load(properties);
    }
}
