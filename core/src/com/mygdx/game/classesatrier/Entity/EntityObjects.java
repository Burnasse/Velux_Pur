package com.mygdx.game.classesatrier.Entity;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.classesatrier.EntityPosition;

/* Commentaires d'expliquation dans l'interface */
public class EntityObjects implements Entity {

    private String objectName;
    private CharacteristicMonster characteristics;
    private EntityPosition position;
    public AssetManager assets;
    public boolean loading;
    private Model model;
    private OutGameEntity outGameEntity;


    public EntityObjects(String objName, String fileName, float initialX, float initialY, float initialZ) {
        this.objectName = objName;
        this.characteristics = new CharacteristicMonster(0, 1);
        this.position = new EntityPosition(initialX, initialY, initialZ);

    }

    @Override
    public void loadObject(String fileName, btCollisionShape shape) {
    }


    public InGameObject createObjectFromModel(String node, Model model, btCollisionShape shape) {
        InGameObject.Constructor constructor = new InGameObject.Constructor(model, node, shape);
        return constructor.construct();
    }

    @Override
    public InGameObject getInGameObject() {
        return null;
    }

    @Override
    public void dispose() {

    }
}
