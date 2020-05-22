package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;

import static com.mygdx.game.physics.CallbackFlags.TRIGGER_FLAG;

public class Trigger {
    private static int globalUserValue = 3000;
    private EntityInstance instance;
    private int userValue;

    public Trigger(float width, float height, float depth, EntityPosition position){
        instance = new EntityInstance(new Model(), new btBoxShape(new Vector3(width,height,depth)), 0f, position);
        userValue = globalUserValue++;
        instance.getBody().setUserValue(userValue);
    }

    public Trigger(Model model, float width, float height, float depth, EntityPosition position){
        instance = new EntityInstance(model, new btBoxShape(new Vector3(width,height,depth)), 0f, position);
        userValue = globalUserValue++;
        instance.getBody().setUserValue(userValue);
    }

    public void addInWorld(btDynamicsWorld world){
        instance.getBody().setCollisionFlags(instance.getBody().getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE
                | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        instance.getBody().setContactCallbackFlag(TRIGGER_FLAG);
        world.addRigidBody(instance.getBody());
    }

    public EntityInstance getEntity() {
        return instance;
    }

    public int getUserValue() {
        return userValue;
    }
}
