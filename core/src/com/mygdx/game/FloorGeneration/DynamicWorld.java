package com.mygdx.game.FloorGeneration;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;

/**
 * The type Dynamic world create a new world with gravity.
 */
public class DynamicWorld {

    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private btAxisSweep3 sweep;
    private btConstraintSolver constraintSolver;
    private btDynamicsWorld dynamicsWorld;
    private btGhostPairCallback ghostPairCallback;;

    /**
     * Instantiates a new Dynamic world.
     */
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

    /**
     * Instantiates a new Dynamic world.
     */
    public DynamicWorld(DebugDrawer debugDrawer){
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        sweep = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, sweep,constraintSolver, collisionConfig);
        ghostPairCallback = new btGhostPairCallback();
        sweep.getOverlappingPairCache().setInternalGhostPairCallback(ghostPairCallback);
        dynamicsWorld.setGravity(new Vector3(0, -10,0));

        dynamicsWorld.setDebugDrawer(debugDrawer);
    }

    /**
     * Dispose.
     */
    public void dispose(){
        dynamicsWorld.dispose();
        constraintSolver.dispose();
        sweep.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();
    }

    /**
     * Get the dynamics world .
     *
     * @return the bt dynamics world
     */
    public btDynamicsWorld getDynamicsWorld(){
        return dynamicsWorld;
    }

    /**
     * Add rigid body in the world.
     *
     * @param body the body
     */
    public void addRigidBody(btRigidBody body){
        dynamicsWorld.addRigidBody(body);
    }

    public void addRigidBody(btRigidBody body, int arg1, int arg2){
        dynamicsWorld.addRigidBody(body,arg1,arg2);
    }
}
