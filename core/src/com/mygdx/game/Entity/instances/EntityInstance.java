package com.mygdx.game.Entity.instances;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Entity.utils.EntityPosition;

/**
 * The type EntityInstance stores the informations of
 * collisions shape, model, mass and position of an non-player entity.
 */
public class EntityInstance extends ModelInstance implements Disposable, Entity {

    private final btRigidBody body;
    private final StaticMotionState.MotionState motionState;
    private final btCollisionShape shape;
    private static Vector3 localInertia = new Vector3();
    private final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    private float mass;

    /**
     * Instantiates a new Entity instance.
     *
     * @param model    the entity model
     * @param shape    the entity collision shape
     * @param mass     the mass
     * @param position the position
     */
    public EntityInstance(Model model, btCollisionShape shape, float mass, EntityPosition position) {
        super(model);
        this.mass = mass;
        this.shape = shape;
        if (mass > 0f)
            shape.calculateLocalInertia(mass, localInertia);
        else
            localInertia.set(0, 0, 0);
        this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
        motionState = new StaticMotionState.MotionState();
        motionState.transform = transform;
        body = new btRigidBody(constructionInfo);
        body.setMotionState(motionState);
        body.setCollisionShape(shape);

        move(position);
    }

    @Override
    public void dispose() {
        body.dispose();
        shape.dispose();
        motionState.dispose();
        constructionInfo.dispose();
    }

    /**
     * Set the position of the entity.
     *
     * @param position the position
     */
    public void move(EntityPosition position) {
        super.transform.trn(position);
        this.body.proceedToTransform(this.transform);
    }

    @Override
    public btCollisionObject getBody() {
        return body;
    }

    /**
     * Set the entity position
     * mainly used in multiplayer
     *
     * @param matrix4
     */
    public void setPosition(Matrix4 matrix4) {
        transform.set(matrix4);
    }
}
