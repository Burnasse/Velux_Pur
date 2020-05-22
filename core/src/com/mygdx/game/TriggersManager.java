package com.mygdx.game;

import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;

import java.util.HashMap;

public class TriggersManager {

    private HashMap<String, Trigger> triggerList = new HashMap<>();
    private final btDynamicsWorld world;

    public TriggersManager(btDynamicsWorld world) {
        this.world = world;
    }

    public void add(String name, Trigger trigger){
        triggerList.put(name,trigger);
        trigger.addInWorld(world);
    }

    public Trigger getTrigger(String name){
        return triggerList.get(name);
    }

    public int getUserValueOf(String name){
        return triggerList.get(name).getUserValue();
    }
}
