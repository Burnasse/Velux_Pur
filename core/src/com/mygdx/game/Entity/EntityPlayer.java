package com.mygdx.game.Entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.item.CreatedItems;
import com.mygdx.game.item.Inventory;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.Weapon;

/**
 * Player class
 */
public class EntityPlayer implements EntityInterface {

    private String playerName;
    private CharacteristicPlayer characteristics;
    private Entity entity;
    private Inventory inventory;
    private Weapon equippedWeapon ;

    /**
     * Instantiates a new Entity player. with a file as entry
     *
     * @param playerName the player name
     * @param fileName   the file name
     * @param shape      the shape
     * @param initialX   the initial x
     * @param initialY   the initial y
     * @param initialZ   the initial z
     */

    public EntityPlayer(String playerName,String fileName,btCollisionShape shape,float initialX,float initialY,float initialZ){
        this.playerName = playerName;
        this.characteristics = new CharacteristicPlayer(0,1);
        EntityPosition position = new EntityPosition(initialX,initialY,initialZ);
        this.entity = new Entity(fileName,shape,position);
        this.equippedWeapon = CreatedItems.getSword();
    }

    /**
     * Instantiates a new Entity player. with a model as entry
     *
     * @param playerName the player name
     * @param model      the model
     * @param shape      the shape
     * @param initialX   the initial x
     * @param initialY   the initial y
     * @param initialZ   the initial z
     */
    public EntityPlayer(String playerName,Model model,btCollisionShape shape,float initialX,float initialY,float initialZ){
        this.playerName = playerName;
        this.characteristics = new CharacteristicPlayer(0,1);
        EntityPosition position = new EntityPosition(initialX,initialY,initialZ);
        this.entity = new Entity(model,shape,position);
        this.equippedWeapon = CreatedItems.getSword();
    }

    @Override
    public InGameObject getInGameObject(){
        return entity.getInGameObject();
    }

    @Override
    public InGameObject getInGameObject(EntityPosition position){
        return entity.getInGameObject(position);
    }

    @Override
    public void dispose() {

    }

    public InGameObject getInGameObjectWeapon(){
        return equippedWeapon.getInGameObject();
    }

    public void usePotion(){
        inventory.usePotion();
    }

    public void getPotion(){
        inventory.getNewPotion();
    }

    public void getNewItem(Item item){
        inventory.addItemInInventory(item);
    }

}
