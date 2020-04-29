package com.mygdx.game.Entity;

/**
 * The interface EntityiInterface.
 */
public interface EntityInterface {
    /**
     * Gets in game object.
     *
     * @return the in game object
     */
    Entity getEntity();

    Entity getEntity(EntityPosition position);

    /**
     * Dispose.
     */
    void dispose();

}
