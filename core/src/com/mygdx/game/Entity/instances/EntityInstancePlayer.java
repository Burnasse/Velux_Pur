package com.mygdx.game.Entity.instances;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Entity.utils.EntityPosition;

/**
 * The type EntityInstance stores the informations of
 * collisions shape, model, mass and position of an player entity.
 */
public class EntityInstancePlayer extends ModelInstance implements Disposable, Entity {

    private btKinematicCharacterController controller;
    private btPairCachingGhostObject ghostObject;
    private btConvexShape ghostShape;
    /**
     * The Motion state.
     */
    public final StaticMotionState.MotionState motionState;

    /**
     * Instantiates a new Entity instance player.
     *
     * @param model           the model
     * @param defaultPosition the default position
     */
    public EntityInstancePlayer(Model model, EntityPosition defaultPosition) {
        super(model);
        transform.trn(defaultPosition);
        transform.rotate(Vector3.X, 90);
        motionState = new StaticMotionState.MotionState();
        motionState.transform = transform;
        ghostObject = new btPairCachingGhostObject();
        ghostObject.setWorldTransform(transform);
        ghostShape = new btCapsuleShape(0.1f, .5f);
        ghostObject.setCollisionShape(ghostShape);
        ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
        controller = new btKinematicCharacterController(ghostObject, ghostShape, .35f, Vector3.Y);
    }

    /**
     * Instantiates a new Entity instance player with float[] position.
     * Mainly used in multiplayer
     *
     * @param model           the model
     * @param defaultPosition the default position
     */
    public EntityInstancePlayer(Model model, float[] defaultPosition) {
        super(model);
        transform.set(defaultPosition);
        transform.rotate(Vector3.X, 90);
        motionState = new StaticMotionState.MotionState();
        motionState.transform = transform;
        ghostObject = new btPairCachingGhostObject();
        ghostObject.setWorldTransform(transform);
        ghostShape = new btCapsuleShape(0.1f, .5f);
        ghostObject.setCollisionShape(ghostShape);
        ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
        controller = new btKinematicCharacterController(ghostObject, ghostShape, .35f, Vector3.Y);
    }

    @Override
    public void dispose() {
        controller.dispose();
        ghostObject.dispose();
        ghostShape.dispose();
    }

    /**
     * Get the ghost object (equivalent to RigidBody).
     *
     * @return the ghost object
     */
    public btPairCachingGhostObject getGhostObject() {
        return ghostObject;
    }

    /**
     * Gets controller.
     *
     * @return the controller
     */
    public btKinematicCharacterController getController() {
        return controller;
    }

    /**
     * Set the position of the entity.
     *
     * @param position the position
     */
    public void move(EntityPosition position) {
        controller.setWalkDirection(position);
    }

    @Override
    public btCollisionObject getBody() {
        return ghostObject;
    }
}
