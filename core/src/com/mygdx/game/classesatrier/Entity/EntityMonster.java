package com.mygdx.game.classesatrier.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.classesatrier.EntityPosition;
import com.mygdx.game.classesatrier.Position;

/* Commentaires d'expliquation dans l'interface */
public class EntityMonster implements Entity{

    private String monsterName;
    private CharacteristicMonster characteristics;
    private Position position;
    public boolean loading;
    private OutGameEntity outGameEntity;


    public EntityMonster(int attackDamage, int health, String monsterName, int initialX, int initialY){
        this.monsterName = monsterName;
        this.characteristics = new CharacteristicMonster(attackDamage,health);
        this.position = new Position(initialX,initialY);
    }

    @Override
    public void loadObject(String fileName, btCollisionShape shape) {
    }

    @Override
    public InGameObject createObjectFromModel(String node,Model model,btCollisionShape shape){
        return null;
    }

    @Override
    public InGameObject getInGameObject() {
        return null;
    }

    @Override
    public void dispose() {
    }
}
