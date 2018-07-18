package com.logicmaster63.tdgalaxy.entity;

import java.util.ArrayList;

public interface AttackingEntity {

    void attack(ArrayList<Entity> target);
    boolean canAttack();

}
