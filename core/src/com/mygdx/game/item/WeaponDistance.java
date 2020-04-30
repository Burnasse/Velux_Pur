package com.mygdx.game.item;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;

/**
 * The interface Item distance.
 */
public class WeaponDistance implements Weapon,Item{

    private EntityInstance weaponEntityInstance;

    private Projectile projectile;
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
    public WeaponDistance(int dammages, String fileName, btCollisionShape shape, EntityPosition spawningPos){
        this.dammages = dammages;
        AssetManager assets = new AssetManager();
        assets.load(fileName,Model.class);
        assets.finishLoading();
        Model model = assets.get(fileName,Model.class);
        this.weaponEntityInstance = new EntityInstance(model,shape,1f,spawningPos);
    }

    /**
     * creates weapon with an already built model
     *
     * @param dammages
     * @param model
     * @param shape
     * @param spawningPos
     */
    public WeaponDistance(int dammages, Model model, btCollisionShape shape, EntityPosition spawningPos){
        this.dammages = dammages;
        this.weaponEntityInstance = new EntityInstance(model,shape,1f,spawningPos);
    }

    @Override
    public EntityInstance getEntity(){
        return weaponEntityInstance;
    }
}
