package com.logicmaster63.tdgalaxy.tower.basic;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.IntMap;
import com.logicmaster63.tdgalaxy.TDGalaxy;
import com.logicmaster63.tdgalaxy.constants.Effects;
import com.logicmaster63.tdgalaxy.constants.Source;
import com.logicmaster63.tdgalaxy.constants.Types;
import com.logicmaster63.tdgalaxy.entity.Entity;
import com.logicmaster63.tdgalaxy.entity.EntityTemplate;
import com.logicmaster63.tdgalaxy.projectiles.Bullet;
import com.logicmaster63.tdgalaxy.tools.Asset;
import com.logicmaster63.tdgalaxy.tools.Dependency;
import com.logicmaster63.tdgalaxy.tower.ProjectileTower;

import java.util.*;

public class Gun extends ProjectileTower{

    public static final int HP = 20;
    public static final int PRICE = 20;
    public static final float COOLDOWN = 2f;
    public static final int RANGE = 3000;
    public static final String ATTACK_ANIMATION = "";
    public static final Vector3 ATTACK_OFFSET = Vector3.Zero;
    public static final EnumSet<Types> TYPES = EnumSet.of(Types.sharp);

    private float muzzle, muzzleInc = 0.1f;
    private boolean installed = false;
    private int legInc = 1, leg, legTo = 30;
    private Map<String, Node> nodes;

    public Gun(Matrix4 transform, int hp, int health, int range, float cooldown, EnumSet<Types> types, EnumSet<Effects> effects, ModelInstance instance, btCollisionShape shape, btCollisionWorld world, IntMap<Entity> entities, String attack, EntityTemplate<Bullet> projectile, Vector3 attackOffset, Map<String, Sound> sounds) {
        super(transform, hp, health, range, cooldown, types, effects, instance, shape, world, entities, attack, projectile, attackOffset, sounds);
        nodes = new HashMap<String, Node>();
        /*for(Node node: instance.model.nodes) {
            System.out.println(node.id);
            if (node.id.startsWith("Leg")) {
                legs.add(node);
            }
        }*/
        //legs.get(0).rotation.setEulerAngles(100, 0, 0);

        for(Node node: instance.getNode("Gun_root").getChildren())
            nodes.put(node.id, node);
    }

    public Gun(Matrix4 transform, Map<String, Model> models, btCollisionWorld world, IntMap<Entity> entities, Map<String, Sound> sounds) throws NoSuchMethodException {
        this(transform, HP, HP, RANGE, COOLDOWN, TYPES, EnumSet.noneOf(Effects.class), new ModelInstance(models.get("Gun")), new btBoxShape(models.get("Gun").calculateBoundingBox(new BoundingBox()).getDimensions(new Vector3())), world, entities, ATTACK_ANIMATION, new EntityTemplate<Bullet>(Bullet.class.getConstructors()[1], models, true, world, entities, sounds), ATTACK_OFFSET, sounds);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

    @Override
    public void render(float delta, ModelBatch modelBatch, ShapeRenderer shapeRenderer, Environment environment) {
        super.render(delta, modelBatch, shapeRenderer, environment);
        //instance.getNode("Leg0").rotation.setEulerAngles(0, 5, 0);
        //instance.calculateTransforms();
        //instance.getNode("Muzzle").inheritTransform = false;

        /*muzzle += muzzleInc;
        if(muzzleInc > 0) {
            instance.getNode("Muzzle").translation.add(0, 0, 0.1f);
            if(muzzle > 5)
                muzzleInc *= -1;
        } else {
            instance.getNode("Muzzle").translation.add(0, 0, -0.1f);
            if(muzzle < 0)
                muzzleInc *= -1;
        }
        */
        //for(Node node: legs)
            //node.rotation.setEulerAngles(45, 0, 0);
        nodes.get("GunBase").rotation.mul(new Quaternion(Vector3.Y, 1));
        nodes.get("Gun").rotation.mul(new Quaternion(Vector3.Y, 1));
        nodes.get("BarrelGuard").rotation.mul(new Quaternion(Vector3.Y, 1));

        leg += legInc;
        if(leg <= 0 || leg >= 75)
            legInc *= -1;
        if(leg == legTo) {
            legInc = 0;
            if(!installed && leg == 30) {
                installed = true;
                sounds.get("ButtonPress").setVolume(sounds.get("ButtonPress").play(), TDGalaxy.getEffectVolumeCombined());
            }
        }
        nodes.get("Leg0").rotation.set(new Quaternion(Vector3.X, leg));
        nodes.get("Leg1").rotation.set(new Quaternion(Vector3.Z, leg));
        nodes.get("Leg2").rotation.set(new Quaternion(Vector3.X, -leg));
        nodes.get("Leg3").rotation.set(new Quaternion(Vector3.Z, -leg));
        //nodes.get("Leg0").translation.add(nodes.get("Leg0").translation);
        //System.out.println(nodes.get("BarrelGuard").translation);
        instance.calculateTransforms();
    }

    public static List<Asset> getAssets() {
        return Arrays.asList(new Asset(Source.INTERNAL, "theme/basic/tower/Gun.g3db", Model.class), new Asset(Source.EXTERNAL, "ButtonPress.mp3", Sound.class));
    }

    public static List getDependencies() {
        return Arrays.asList(new Dependency(Bullet.class, "Bullet"));
    }
}
