package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.classesatrier.EntityPosition;

/**
 * The type Entity monster.
 */
/* Commentaires d'expliquation dans l'interface */
public class EntityMonster implements EntityInterface {

    private String monsterName;
    private CharacteristicMonster characteristics;
    private Entity entity;


    /**
     * Instantiates a new Entity monster with an outside file
     *
     * @param monsterName  the obj name
     * @param fileName the file name
     * @param shape    the shape
     * @param initialX the initial x
     * @param initialY the initial y
     * @param initialZ the initial z
     */
    public EntityMonster(String monsterName,String fileName,btCollisionShape shape,float initialX,float initialY,float initialZ){
        this.monsterName = monsterName;
        this.characteristics = new CharacteristicMonster(0,1);
        EntityPosition position = new EntityPosition(initialX,initialY,initialZ);
        this.entity = new Entity(fileName,shape,position);
    }

    /**
     * Instantiates a new Entity monster with a model
     *
     * @param monsterName  the obj name
     * @param model    the model
     * @param shape    the shape
     * @param initialX the initial x
     * @param initialY the initial y
     * @param initialZ the initial z
     */
    public EntityMonster(String monsterName, Model model, btCollisionShape shape, float initialX, float initialY, float initialZ){
        this.monsterName = monsterName;
        this.characteristics = new CharacteristicMonster(0,1);
        EntityPosition position = new EntityPosition(initialX,initialY,initialZ);
        this.entity = new Entity(model,shape,position);
    }

    @Override
    public InGameObject getInGameObject(){
        return entity.getInGameObject();
    }

    @Override
    public void dispose() {
    }
}
