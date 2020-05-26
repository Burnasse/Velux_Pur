package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.EntityMonster;

import java.util.TimerTask;

public class Zombie extends SteeringAgent {

    float damage;
    private Sound attackSound;
    Assets assets;
    /**
     * Instantiates a new zombie.
     *
     * @param instance the instance
     * @param x1       the wandering limit
     * @param x2       the wandering limit
     * @param z1       the wandering limit
     * @param z2       the wandering limit
     */

    public Zombie(EntityMonster instance, int x1, int z1, int x2, int z2,Assets assets) {
        super(instance, x1, z1, x2, z2);
        maxCoolDown = 1;
        coolDown = maxCoolDown;
        this.assets = assets;
        position = instance.getEntity().transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(10, 10, 10);

        orientation = 1;
        maxLinearSpeed = 4f;
        maxLinearAcceleration = 3f;
        weaponRange = 0.5f;
        damage = 1;
        attackSound = assets.manager.get(Assets.zombieAttackSound);
    }

    /**
     * Instantiates a new zombie.
     *
     * @param instance the instance
     * @param x1       the wandering limit
     * @param x2       the wandering limit
     * @param z1       the wandering limit
     * @param z2       the wandering limit
     * @param damage   the damage caused by the zombie
     */

    public Zombie(EntityMonster instance, int x1, int z1, int x2, int z2, float maxCoolDown, float damage) {
        super(instance, x1, z1, x2, z2, maxCoolDown);

        maxCoolDown = 1;
        coolDown = maxCoolDown;

        position = instance.getEntity().transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(10, 10, 10);

        orientation = 1;
        maxLinearSpeed = 4f;
        maxLinearAcceleration = 3f;
        weaponRange = 0.8f;
        this.damage = damage;
        generateRandomTarget();
        behavior = new Arrive<>(this, target);
        attackSound = assets.manager.get(Assets.zombieAttackSound);
    }

    /**
     * updates the Zombie
     *
     * @param delta the time between this frame and the last
     */

    @Override
    public void update(float delta) {
        //A
        if (monster.getHealth() > 0) {
            coolDown = coolDown + delta;

            if (playerInRoom()) {

                target.setVector(player.getEntity().transform.getTranslation(new Vector3()));
                setMaxLinearSpeed(4);
                setMaxLinearAcceleration(10);
                behavior = new Pursue<>(this, target, 0);

                if ((isAround(position, target.vector, weaponRange * 3f)))
                    behavior = new Arrive<>(this, target);
            } else {
                if (behavior instanceof Pursue) {
                    setMaxLinearAcceleration(3);
                    generateRandomTarget();
                    behavior = new Arrive<>(behavior.getOwner(), target);
                    setMaxLinearSpeed(3);
                } else if ((isAround(position, target.vector, 1)) && behavior.isEnabled()) {

                    behavior.setEnabled(false);
                    setMaxLinearSpeed(0);
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            generateRandomTarget();
                            setMaxLinearAcceleration(3);
                            setMaxLinearSpeed(3);
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
                    attack();
                    coolDown = 0;
                }
            } else Move(delta);

            monster.getEntity().getBody().proceedToTransform(monster.getEntity().transform);
        }
    }

    /**
     * the method called when the zombie attacks
     */

    @Override
    protected void attack() {
        System.out.println("Tu prends " + damage + "dégats");
        attackSound.play(0.2f);
    }
}