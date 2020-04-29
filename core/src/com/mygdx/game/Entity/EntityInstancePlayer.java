package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.utils.Disposable;

public class EntityInstancePlayer extends ModelInstance implements Disposable, Entity {

    private btKinematicCharacterController controller;
    private btPairCachingGhostObject ghostObject;
    private btConvexShape ghostShape;

    public EntityInstancePlayer(Model model, EntityPosition defaultPosition) {
        super(model);
        transform.set(defaultPosition,new Quaternion());
        transform.rotate(Vector3.X, 90);
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

    public btPairCachingGhostObject getGhostObject(){
        return ghostObject;
    }

    public btKinematicCharacterController getController() {
        return controller;
    }

    public void move(EntityPosition position){
        controller.setWalkDirection(position);
    }

    @Override
    public btCollisionObject getBody() {
        return ghostObject;
    }
}
