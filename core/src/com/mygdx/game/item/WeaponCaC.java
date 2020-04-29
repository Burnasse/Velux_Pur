package com.mygdx.game.item;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.Entity.Entity;
import com.mygdx.game.Entity.InGameObject;
import com.mygdx.game.Entity.EntityPosition;

/**
 * The interface Item ca c.
 */
public class WeaponCaC implements Weapon,Item{

private Entity weaponEntity;
/**
 * the range of Cac weapons will be fixed for now
 */
private int dammages;

    /**
     * creates weapon with a file as model
     *
     * @param dammages
     * @param fileName
     * @param shape
     * @param spawningPos
     */
    public WeaponCaC(int dammages, String fileName, btCollisionShape shape, EntityPosition spawningPos){
    this.dammages = dammages;
        this.weaponEntity = new Entity(fileName,shape,spawningPos);
    }

    /**
     * creates weapon with an already built model
     *
     * @param dammages
     * @param model
     * @param shape
     * @param spawningPos
     */
    public WeaponCaC(int dammages, Model model, btCollisionShape shape, EntityPosition spawningPos){
        this.dammages = dammages;
        this.weaponEntity = new Entity(model,shape,spawningPos);
    }

    @Override
    public InGameObject getInGameObject(){
        return weaponEntity.getInGameObject();
    }


}
