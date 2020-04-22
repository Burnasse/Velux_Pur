package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.classesatrier.EntityPosition;
import com.mygdx.game.classesatrier.Position;

/* Commentaires d'expliquation dans l'interface */
public class EntityPlayer implements Entity {

    private String playerName;
    private CharacteristicMonster characteristics;
    private Position position;
    public boolean loading;
    private Model model;
    private OutGameEntity outGameEntity;


    public EntityPlayer(int attackDamage, int health, String playerName, int initialX, int initialY, String fileName) {
        this.playerName = playerName;
        this.characteristics = new CharacteristicMonster(attackDamage, health);
        this.position = new Position(initialX, initialY);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public void loadObject(String fileName, btCollisionShape shape) {
    }

    @Override
    public InGameObject createObjectFromModel(String node, Model model, btCollisionShape shape) {
        return null;
    }

    @Override
    public InGameObject getInGameObject() {
        return null;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public void dispose() {

    }
}
