package com.mygdx.game.FloorGeneration;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;

public class DynamicWorld {

    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private btAxisSweep3 sweep;
    private btConstraintSolver constraintSolver;
    private btDynamicsWorld dynamicsWorld;
    private btGhostPairCallback ghostPairCallback;;

    public DynamicWorld(){
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        sweep = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, sweep,constraintSolver, collisionConfig);
        ghostPairCallback = new btGhostPairCallback();
        sweep.getOverlappingPairCache().setInternalGhostPairCallback(ghostPairCallback);
        dynamicsWorld.setGravity(new Vector3(0, -10f,0));

    }

    public void dispose(){
        dynamicsWorld.dispose();
        constraintSolver.dispose();
        sweep.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();
    }

    public btDynamicsWorld getDynamicsWorld(){
        return dynamicsWorld;
    }

    public void addRigidBody(btRigidBody body){
        dynamicsWorld.addRigidBody(body);
    }
}
