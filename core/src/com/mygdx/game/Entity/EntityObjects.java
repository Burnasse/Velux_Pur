package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;

/**
 * The type Entity objects.
 */
/* Commentaires d'expliquation dans l'interface */
public class EntityObjects implements EntityInterface {

    private String objectName;
    private CharacteristicMonster characteristics;
    private EntityInstance entityInstance;

    public EntityObjects(String objectName, Model model, btBoxShape shape, float mass, EntityPosition defaultPos){
        this.objectName = objectName;
        this.characteristics = new CharacteristicMonster(0,1);
        entityInstance = new EntityInstance(model,shape,mass,defaultPos);
    }

    public EntityObjects(String objectName, String fileName, btBoxShape shape, float mass, EntityPosition defaultPos){
        this.objectName = objectName;
        this.characteristics = new CharacteristicMonster(0,1);

        AssetManager assets = new AssetManager();
        assets.load(fileName,Model.class);
        assets.finishLoading();
        Model model = assets.get(fileName,Model.class);
        entityInstance = new EntityInstance(model,shape,mass,defaultPos);
    }


    @Override
    public void dispose() {
        entityInstance.dispose();
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
}
