package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.classesatrier.EntityPosition;

/**
 * The type Entity objects.
 */
/* Commentaires d'expliquation dans l'interface */
public class EntityObjects implements EntityInterface {

    private String objectName;
    private CharacteristicMonster characteristics;
    private Entity entity;
    private Item item;

    /**
     * Instantiates a new Entity objects with an outside file.
     *
     * @param objName  the obj name
     * @param fileName the file name
     * @param shape    the shape
     * @param initialX the initial x
     * @param initialY the initial y
     * @param initialZ the initial z
     */
    public EntityObjects(String objName,String fileName,btCollisionShape shape,float initialX,float initialY,float initialZ){
        this.objectName = objName;
        this.characteristics = new CharacteristicMonster(0,1);
        EntityPosition position = new EntityPosition(initialX,initialY,initialZ);
        this.entity = new Entity(fileName,shape,position);
    }

    /**
     * Instantiates a new Entity objects with a model.
     *
     * @param objName  the obj name
     * @param model    the model
     * @param shape    the shape
     * @param initialX the initial x
     * @param initialY the initial y
     * @param initialZ the initial z
     */
    public EntityObjects(String objName,Model model,btCollisionShape shape,float initialX,float initialY,float initialZ){
        this.objectName = objName;
        this.characteristics = new CharacteristicMonster(0,1);
        EntityPosition position = new EntityPosition(initialX,initialY,initialZ);
        this.entity = new Entity(model,shape,position);
    }

    @Override
    public InGameObject getInGameObject(){
        return entity.getInGameObject();
    }

    @Override
    public InGameObject getInGameObject(EntityPosition position){
        return entity.getInGameObject(position);
    }

    @Override
    public void dispose() {

    }
}
