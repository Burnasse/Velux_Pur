package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.classesatrier.EntityPosition;

/**
 * this lass represents an entity that has not been instanciated in the game yet.
 */
public class OutGameEntity {


    public Model model;

    public btCollisionShape shape;



    private InGameObject.Constructor constructor ;

    /**
     * create a new Out game entity from an outside .obj or .g3db ( which is the best).Create the constructor to instanciate the entity.
     *
     * @param filename the filename
     * @param shape    the shape
     */
    public OutGameEntity(String filename,btCollisionShape shape){
        AssetManager assets = new AssetManager();
        assets.load(filename,Model.class);
        assets.finishLoading();
        this.model = assets.get(filename,Model.class);
        this.shape = shape;
        this.constructor = new InGameObject.Constructor(this.model,this.shape);
    }

    /**
     * createsa new Out game entity from an already existing Model.
     *
     * @param model the model
     * @param shape the shape
     */
    public OutGameEntity(Model model,btCollisionShape shape){
        this.model = model;
        this.shape = shape;
        this.constructor = new InGameObject.Constructor(this.model,this.shape);

    }

    /**
     * create and return an entity Instance of the model we wants.
     *
     * @return the in game object
     */
    public InGameObject createEntityInstance(){
        return this.constructor.construct();
    }

    public InGameObject createEntityInstance(EntityPosition position){
        InGameObject instanciatedEntity = this.constructor.construct();
        instanciatedEntity.mooveEntity(position);
        return instanciatedEntity;
    }
}
