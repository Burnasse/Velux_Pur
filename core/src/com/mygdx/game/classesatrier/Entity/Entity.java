package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.classesatrier.EntityPosition;

/**
 * The type Entity.
 */
public class Entity {

    private EntityPosition position;
    private OutGameEntity outGameEntity;
    private String fileName;

    /**
     * creates a new Entity with a file.
     *
     * @param fileName    the file name
     * @param shape       the shape
     * @param spawningPos the spawning pos
     */
    public Entity(String fileName, btCollisionShape shape,EntityPosition spawningPos){
        this.position = spawningPos;
        this.fileName = fileName;
        this.outGameEntity = new OutGameEntity(this.fileName,shape);
    }

    /**
     * creates a new Entity with a model
     *
     * @param model       the model
     * @param shape       the shape
     * @param spawningPos the spawning pos
     */
    public Entity(Model model, btCollisionShape shape,EntityPosition spawningPos){
        this.position = spawningPos;
        this.outGameEntity = new OutGameEntity(model,shape);
    }

    /**
     * Gets in game object.
     *
     * @return the in game object
     */
    public InGameObject getInGameObject() {
        return outGameEntity.createEntityInstance(this.position);
    }

    public InGameObject getInGameObject(EntityPosition position) {
        return outGameEntity.createEntityInstance(position);
    }

    /**
     * Dispose.
     */
    public void dispose() {

    }



}
