package com.mygdx.game.Entity;

import com.mygdx.game.Entity.instances.Entity;
import com.mygdx.game.Entity.utils.EntityPosition;

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
