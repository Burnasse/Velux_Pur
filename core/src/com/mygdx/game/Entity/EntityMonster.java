package com.mygdx.game.Entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.IA.Gunner;
import com.mygdx.game.IA.SteeringAgent;
import com.mygdx.game.IA.Zombie;
import com.mygdx.game.item.Weapon;

/**
 * The type Entity monster.
 */
public class EntityMonster implements EntityInterface {

    private String monsterName;
    private CharacteristicMonster characteristics;
    private EntityInstance entityInstance;
    private SteeringAgent behavior;
    private AnimationController animationController;

    /**
     * Instantiates a new Entity monster. with a model as entry
     *
     * @param monsterName the monster name
     * @param model       the model
     * @param shape       the shape
     * @param mass        the mass
     * @param defaultPos  the position
     */
    public EntityMonster(String monsterName, Model model, btCollisionShape shape, float mass, EntityPosition defaultPos, String typeOfMonster, int x1, int y1, int x2, int y2) {
        this.monsterName = monsterName;
        this.characteristics = new CharacteristicMonster(0, 10);
        entityInstance = new EntityInstance(model, shape, mass, defaultPos);
        animationController = new AnimationController(entityInstance);
        if (typeOfMonster.equals("Gunner"))
            behavior = new Gunner(entityInstance, x1, y1, x2, y2);
        else behavior = new Zombie(entityInstance, x1, y1, x2, y2);
        behavior.setAnimationController(animationController);
    }

    /**
     * Instantiates a new Entity monster. with a file as entry
     *
     * @param monsterName the monster name
     * @param fileName    the file name
     * @param shape       the shape
     * @param mass        the mass
     * @param defaultPos  the position
     */
    public EntityMonster(String monsterName, String fileName, btCollisionShape shape, float mass, EntityPosition defaultPos) {
        this.monsterName = monsterName;
        this.characteristics = new CharacteristicMonster(0, 10);

        AssetManager assets = new AssetManager();
        assets.load(fileName, Model.class);
        assets.finishLoading();
        Model model = assets.get(fileName, Model.class);
        entityInstance = new EntityInstance(model, shape, mass, defaultPos);
    }

    public AnimationController getAnimationController() {
        return animationController;
    }

    @Override
    public EntityInstance getEntity() {
        return entityInstance;
    }

    @Override
    public EntityInstance getEntity(EntityPosition position) {
        entityInstance.move(position);
        return entityInstance;
    }


    @Override
    public void dispose() {
        entityInstance.dispose();
    }

    public void move(EntityPosition pos) {
        entityInstance.move(pos);
    }

    public void damage(Weapon weapon) {
        characteristics.setHealth(characteristics.getHealth() - weapon.getDammage());
    }

    public int getHealth() {
        return characteristics.getHealth();
    }

    public SteeringAgent getBehavior(){
        return behavior;
    }

}
