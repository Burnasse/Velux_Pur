package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.classesatrier.EntityPosition;

/**
 * The interface EntityiInterface.
 */
public interface EntityInterface {
     /**
      * Gets in game object.
      *
      * @return the in game object
      */
     InGameObject getInGameObject();

     /**
      * Gets in game object and put it in the desired position.
      *
      * @param position the position
      * @return the in game object
      */
     public InGameObject getInGameObject(EntityPosition position);

     /**
      * Dispose.
      */
     void dispose();

}
