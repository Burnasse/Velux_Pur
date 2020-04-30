package com.mygdx.game.Entity.instances;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

/**
 * The interface Entity is use for represent the collision, shape and model of an EntityInterface.
 */
public interface Entity {

    /**
     * Gets body.
     *
     * @return the collision body
     */
    btCollisionObject getBody();

    /**
     * Dispose.
     */
    void dispose();
}
