package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

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
      * Dispose.
      */
     void dispose();

}
