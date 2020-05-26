package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.EntityMonster;
import com.mygdx.game.Entity.instances.EntityInstance;

import java.util.ArrayList;
import java.util.TimerTask;

/**
 * The type of enemy that attacks from a certain distance
 */
public class Gunner extends SteeringAgent {

    float projectileDamage;
    private Sound attackSound;
    Assets assets;

    ArrayList<Projectile> projectilesShot = new ArrayList<>();
    Array<EntityInstance> doneProjectiles = new Array<>();

    public Array<EntityInstance> getDoneProjectiles() {
        Array<EntityInstance> temp = doneProjectiles;
        doneProjectiles.clear();
        return temp;
    }

    /**
     * Instantiates a new Gunner.
     *
     * @param object the instance
     * @param x1     the wandering limit
     * @param x2     the wandering limit
     * @param z1     the wandering limit
     * @param z2     the wandering limit
     */

    public Gunner(EntityMonster object, int x1, int z1, int x2, int z2,Assets assets) {
        super(object, x1, z1, x2, z2);
        maxCoolDown = 1f;
        coolDown = maxCoolDown;
        this.assets = assets;
        position = object.getEntity().transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(0, 0f, 0);

        orientation = 1;
        maxLinearSpeed = 2f;
        maxLinearAcceleration = 2f;
        weaponRange = 3;
        projectileDamage = 1;
        attackSound = assets.manager.get(Assets.bowSound);

    }

    /**
     * Instantiates a new Gunner.
     *
     * @param object           the instance
     * @param x1               the wandering limit
     * @param x2               the wandering limit
     * @param z1               the wandering limit
     * @param z2               the wandering limit
     * @param maxCoolDown      the coolDown of the attack
     * @param weaponRange      range of the attack
     * @param projectileDamage damage caused by the Gunner
     */

    public Gunner(EntityMonster object, int x1, int z1, int x2, int z2, float maxCoolDown, float weaponRange, float projectileDamage) {
        super(object, x1, z1, x2, z2, maxCoolDown);
        coolDown = maxCoolDown;

        this.monster = object;
        position = object.getEntity().transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(0, 0, 0);

        orientation = 1;
        maxLinearSpeed = 2;

        generateRandomTarget();
        behavior = new Arrive<>(this, target);
        this.maxCoolDown = maxCoolDown;
        this.weaponRange = weaponRange;
        this.projectileDamage = projectileDamage;
        coolDown = maxCoolDown;
    }

    /**
     * updates the Gunner
     *
     * @param delta the time between this frame and the last
     */

    @Override
    public void update(final float delta) {
        //a
        if (monster.getHealth()>0) {
            checkProjectiles();
            coolDown = coolDown + delta;
            updateProjectiles(delta);

            if (playerInRoom()) {

                target.setVector(player.getEntity().transform.getTranslation(new Vector3()));
                setMaxLinearSpeed(3);
                setMaxLinearAcceleration(10);
                behavior = new Pursue<>(this, target);

            } else {
                if (behavior instanceof Pursue) {
                    setMaxLinearAcceleration(1);
                    generateRandomTarget();
                    behavior = new Arrive<>(behavior.getOwner(), target);
                    setMaxLinearSpeed(2);
                } else if ((isAround(position, target.vector, 1)) && behavior.isEnabled()) {

                    behavior.setEnabled(false);
                    setMaxLinearSpeed(0);
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            generateRandomTarget();
                            setMaxLinearAcceleration(2);
                            setMaxLinearSpeed(2);
                            behavior.setEnabled(true);
                            behavior = new Arrive<>(behavior.getOwner(), target);
                        }
                    }, 800);
                }
            }
            behavior.calculateSteering(steeringOutput);

            Turn();

            if ((isAround(position, target.vector, weaponRange) && playerInRoom())) {
                if (coolDown >= maxCoolDown) {
                    getController().animate("fire", -1, 1.0f, null, 0.2f);
                    target.setVector(player.getEntity().transform.getTranslation(new Vector3()));
                    attack();
                    coolDown = 0;
                }
            } else {
                Move(delta);
                getController().animate("run", -1, 1.0f, null, 0.2f);
            }
            monster.getEntity().getBody().proceedToTransform(monster.getEntity().transform);
        }
    }

    /**
     * the method called when the gunner attacks
     */

    protected void attack() {
        Turn();
        projectilesShot.add(new Projectile(player.getPosition(), 5, position, getWorld()));
        attackSound.play(0.2f);
    }

    /**
     * the method called when the gunner attacks
     */
    public float getProjectileDamage(){
        return projectileDamage;
    }
    public ArrayList<Projectile> getProjectilesShot(){
        return projectilesShot;
    }

    /**
     * the method called when the gunner attacks
     */
    private void checkProjectiles() {
        for (Projectile projectile : projectilesShot) {
            if (projectile.isDone()) {
                doneProjectiles.add(projectile.getInstance());
                projectile.getInstance().getBody().setContactCallbackFilter(0);
                projectile.getInstance().getBody().setContactCallbackFlag(55);
            }
        }
    }

    /**
     * updates the projectiles the Gunner shot
     */

    private void updateProjectiles(float delta) {
        for (Projectile projectile : projectilesShot) {

            projectile.update(delta);
        }
    }

    /**
     * returns the instances of the projectiles the Gunner shot
     */

    public Array<EntityInstance> projectiles() {
        Array<EntityInstance> instances = new Array<>();
        for (Projectile projectile : projectilesShot) {
            instances.add(projectile.getInstance());
        }
        return instances;
    }

}