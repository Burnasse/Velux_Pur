package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;

/**
 * The type Entity objects.
 */
/* Commentaires d'expliquation dans l'interface */
public class EntityObjects implements EntityInterface {

    private String objectName;
    private CharacteristicMonster characteristics;
    private EntityInstance entityInstance;

    /**
     *Instantiates a new Entity object. with a model as entry
     *
     * @param objectName the objectName name
     * @param model   the model
     * @param shape      the shape
     * @param mass       the mass
     * @param defaultPos   the position
     */
    public EntityObjects(String objectName, Model model, btBoxShape shape, float mass, EntityPosition defaultPos){
        this.objectName = objectName;
        this.characteristics = new CharacteristicMonster(0,1);
        entityInstance = new EntityInstance(model,shape,mass,defaultPos);
    }

    /**
     * Instantiates a new Entity object. with a file as entry
     *
     * @param objectName the objectName name
     * @param fileName   the file name
     * @param shape      the shape
     * @param mass       the mass
     * @param defaultPos   the position
     */
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
        entityInstance.transform.trn(position);
        return entityInstance;
    }
}
