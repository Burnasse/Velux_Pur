package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.instances.EntityInstancePlayer;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.item.CreatedItems;
import com.mygdx.game.item.Inventory;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.Weapon;

/**
 * Player class
 */
public class EntityPlayer implements EntityInterface {

    private final String playerName;
    private CharacteristicPlayer characteristics;
    private EntityInstancePlayer entityInstance;
    private Inventory inventory;
    private Weapon equippedWeapon;

    /**
     * Instantiates a new Entity player. with a file as entry
     *
     * @param playerName the player name
     * @param fileName   the file name
     * @param position   the position
     */
    public EntityPlayer(String playerName, String fileName, EntityPosition position) {
        this.playerName = playerName;
        this.characteristics = new CharacteristicPlayer(0, 1);
        this.equippedWeapon = CreatedItems.getSword();

        AssetManager assets = new AssetManager();
        assets.load(fileName, Model.class);
        assets.finishLoading();
        Model model = assets.get(fileName, Model.class);
        this.entityInstance = new EntityInstancePlayer(model, position);
    }

    /**
     * Instantiates a new Entity player. with a model as entry
     *
     * @param playerName the player name
     * @param model      the file name
     * @param position   the position
     */
    public EntityPlayer(String playerName, Model model, EntityPosition position) {
        this.playerName = playerName;
        this.characteristics = new CharacteristicPlayer(0, 1);
        this.equippedWeapon = CreatedItems.getSword();
        this.entityInstance = new EntityInstancePlayer(model, position);
    }

    /**
     * Instantiates a new Entity player. with a model as entry
     *
     * @param playerName the player name
     * @param model      the file name
     * @param position   the position
     */
    public EntityPlayer(String playerName, Model model, float[] position) {
        this.playerName = playerName;
        this.characteristics = new CharacteristicPlayer(0, 1);
        this.equippedWeapon = CreatedItems.getSword();
        this.entityInstance = new EntityInstancePlayer(model, position);
    }

    public EntityInstance getEntityWeapon() {
        return equippedWeapon.getEntity();
    }

    public void usePotion() {
        inventory.usePotion();
    }

    public void getPotion() {
        inventory.getNewPotion();
    }

    public void getNewItem(Item item) {
        inventory.addItemInInventory(item);
    }

    @Override
    public EntityInstancePlayer getEntity() {
        return entityInstance;
    }

    @Override
    public EntityInstancePlayer getEntity(EntityPosition position) {
        entityInstance.move(position);
        return entityInstance;
    }

    @Override
    public void dispose() {
        entityInstance.dispose();
    }

}
