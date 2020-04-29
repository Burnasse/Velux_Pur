package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

/**
 * The type Entity monster.
 */
/* Commentaires d'expliquation dans l'interface */
public class EntityMonster implements EntityInterface {

    private String monsterName;
    private CharacteristicMonster characteristics;
    private EntityInstance entityInstance;

    public EntityMonster(String monsterName, Model model, btBoxShape shape, float mass, EntityPosition defaultPos){
        this.monsterName = monsterName;
        this.characteristics = new CharacteristicMonster(0,1);
        entityInstance = new EntityInstance(model,shape,mass,defaultPos);
    }

    public EntityMonster(String monsterName, String fileName, btBoxShape shape, float mass, EntityPosition defaultPos){
        this.monsterName = monsterName;
        this.characteristics = new CharacteristicMonster(0,1);

        AssetManager assets = new AssetManager();
        assets.load(fileName,Model.class);
        assets.finishLoading();
        Model model = assets.get(fileName,Model.class);
        entityInstance = new EntityInstance(model,shape,mass,defaultPos);
    }


    @Override
    public EntityInstance getEntity(){
        return entityInstance;
    }

    @Override
    public EntityInstance getEntity(EntityPosition position){
        entityInstance.move(position);
        return entityInstance;
    }


    @Override
    public void dispose() {
    }



}
