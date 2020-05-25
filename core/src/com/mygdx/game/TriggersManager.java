package com.mygdx.game;

import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;

import java.util.HashMap;

/**
 * The type Triggers manager manage a list of Trigger.
 */
public class TriggersManager {

    private HashMap<String, Trigger> triggerList = new HashMap<>();
    private final btDynamicsWorld world;

    /**
     * Instantiates a new Triggers manager.
     *
     * @param world the world
     */
    public TriggersManager(btDynamicsWorld world) {
        this.world = world;
    }

    /**
     * Add.
     *
     * @param name    the name
     * @param trigger the trigger
     */
    public void add(String name, Trigger trigger){
        triggerList.put(name,trigger);
        trigger.addInWorld(world);
    }

    /**
     * Get trigger trigger.
     *
     * @param name the name
     * @return the trigger
     */
    public Trigger getTrigger(String name){
        return triggerList.get(name);
    }

    /**
     * Get user value of int.
     *
     * @param name the name
     * @return the int
     */
    public int getUserValueOf(String name){
        return triggerList.get(name).getUserValue();
    }

    /**
     * Dispose.
     */
    public void dispose(){
        for(Trigger trigger : triggerList.values()){
            world.removeRigidBody(trigger.getEntity().getBody());
            trigger.dispose();
        }
    }
}
