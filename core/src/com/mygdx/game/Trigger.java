package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;

import static com.mygdx.game.physics.CallbackFlags.TRIGGER_FLAG;

/**
 * The type Trigger is an object who has no collision but it can be detected by the contactListener.
 */
public class Trigger {
    private static int globalUserValue = 3000;
    private EntityInstance instance;
    private int userValue;

    /**
     * Instantiates a new Trigger.
     *
     * @param width    the width
     * @param height   the height
     * @param depth    the depth
     * @param position the position
     */
    public Trigger(float width, float height, float depth, EntityPosition position){
        instance = new EntityInstance(new Model(), new btBoxShape(new Vector3(width,height,depth)), 0f, position);
        userValue = globalUserValue++;
        instance.getBody().setUserValue(userValue);
    }

    /**
     * Instantiates a new Trigger.
     *
     * @param model    the model
     * @param width    the width
     * @param height   the height
     * @param depth    the depth
     * @param position the position
     */
    public Trigger(Model model, float width, float height, float depth, EntityPosition position){
        instance = new EntityInstance(model, new btBoxShape(new Vector3(width,height,depth)), 0f, position);
        userValue = globalUserValue++;
        instance.getBody().setUserValue(userValue);
    }

    /**
     * Add in world.
     *
     * @param world the world
     */
    public void addInWorld(btDynamicsWorld world){
        instance.getBody().setCollisionFlags(instance.getBody().getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE
                | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        instance.getBody().setContactCallbackFlag(TRIGGER_FLAG);
        world.addRigidBody(instance.getBody());
    }

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public EntityInstance getEntity() {
        return instance;
    }

    /**
     * Gets user value.
     *
     * @return the user value
     */
    public int getUserValue() {
        return userValue;
    }

    /**
     * Dispose.
     */
    public void dispose(){
        instance.dispose();
    }
}
