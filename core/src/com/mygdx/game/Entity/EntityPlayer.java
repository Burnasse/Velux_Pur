package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

    public Weapon equippedWeapon;
    public boolean isAttacking;
    public int cdAttack = 125;
    public int cdDammagesTaken =60;
    public Vector2 player2DPos = new Vector2(0,0);
    public Vector2 cursor2DPos = new Vector2(0,0);

    public int cdColisionWeaponEnnemy;


    /**
     * Instantiates a new Entity player. with a file as entry
     *
     * @param playerName the player name
     * @param fileName   the file name
     * @param position   the position
     */
    public EntityPlayer(String playerName, String fileName, EntityPosition position) {
        this.playerName = playerName;

        this.characteristics = new CharacteristicPlayer(0, 100);

        AssetManager assets = new AssetManager();
        assets.load(fileName, Model.class);
        assets.finishLoading();

        Model model = assets.get(fileName, Model.class);
        this.entityInstance = new EntityInstancePlayer(model, position);
        this.isAttacking = false;

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
        this.characteristics = new CharacteristicPlayer(0, 100);
        this.equippedWeapon = CreatedItems.getSword();
        this.entityInstance = new EntityInstancePlayer(model, position);
        this.isAttacking = false;
    }

    /**
     * Instantiates a new Entity player. with a model as entry
     * Mainly used in multiplayer
     *
     * @param playerName the player name
     * @param model      the file name
     * @param position   the position
     */
    public EntityPlayer(String playerName, Model model, float[] position) {
        this.playerName = playerName;
        this.characteristics = new CharacteristicPlayer(0, 100);
        this.equippedWeapon = CreatedItems.getSword();
        this.entityInstance = new EntityInstancePlayer(model, position);
    }

    public Weapon getWeapon() {
        return equippedWeapon;
    }

    public EntityInstance getEntityWeapon() {
        return equippedWeapon.getEntity();
    }

    public void getsAttacked(float dammages){
        characteristics.getsAttacked(dammages);
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

    public EntityPosition getPosition() {
        Vector3 pos = entityInstance.transform.getTranslation(new Vector3());

        return new EntityPosition(pos.x, pos.y, pos.z);
    }

    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        equippedWeapon.getEntity().move(new EntityPosition(getPosition().x + 1, getPosition().y + 0.5f, getPosition().z + 1));
    }

    public void attack() {
        this.isAttacking = true;
        cdAttack = 0;
    }

    public boolean getIsAttacking() {
        return this.isAttacking;
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

    public CharacteristicPlayer getCharacteristics() {
        return characteristics;
    }

    @Override
    public void dispose() {
        entityInstance.dispose();
    }

}
