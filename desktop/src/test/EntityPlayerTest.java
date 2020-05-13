package test;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.Entity.*;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.item.CreatedItems;
import com.mygdx.game.item.Inventory;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.Weapon;

/**
 * Player class for test
 */
public class EntityPlayerTest implements EntityInterface {

    private final String playerName;
    private CharacteristicPlayer characteristics;
    private Inventory inventory;
    private Weapon equippedWeapon ;
    private EntityInstance entityInstance;


    public EntityPlayerTest(String playerName, String filename, btCollisionShape shape, EntityPosition position){
        this.playerName = playerName;
        this.characteristics = new CharacteristicPlayer(0,1);
        this.equippedWeapon = CreatedItems.getSword();
        AssetManager assets = new AssetManager();
        assets.load(filename,Model.class);
        assets.finishLoading();
        Model model = assets.get(filename,Model.class);
        this.entityInstance = new EntityInstance(model,shape,1f, position);
    }

    public EntityInstance getEntityWeapon(){
        return equippedWeapon.getEntity();
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

    @Override
    public EntityInstance getEntity() {
        return entityInstance;
    }

    @Override
    public EntityInstance getEntity(EntityPosition position){
        entityInstance.move(position);
        return entityInstance;
    }

    @Override
    public void dispose() {
        entityInstance.dispose();
    }

}

