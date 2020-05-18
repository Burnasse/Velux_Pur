package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.instances.EntityInstance;

import java.util.TimerTask;

public class Zombie extends SteeringAgent {

    float damage;

    /**
     * Instantiates a new zombie.
     *
     * @param instance  the instance
     * @param x1 the wandering limit
     * @param x2 the wandering limit
     * @param z1 the wandering limit
     * @param z2 the wandering limit
     */

    public Zombie(EntityInstance instance, int x1, int z1, int x2, int z2) {
        super(instance, x1, z1, x2, z2);
        maxCoolDown = 1;
        coolDown = maxCoolDown;

        position = instance.transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(10, 10, 10);

        orientation = 1;
        maxLinearSpeed = 4f;
        maxLinearAcceleration = 3f;
        weaponRange = 0.8f;
        damage = 1;

        generateRandomTarget();
        behavior = new Arrive<>(this, target);
    }

    /**
     * Instantiates a new zombie.
     *
     * @param instance  the instance
     * @param x1 the wandering limit
     * @param x2 the wandering limit
     * @param z1 the wandering limit
     * @param z2 the wandering limit
     * @param damage the damage caused by the zombie
     */

    public Zombie(EntityInstance instance, int x1, int z1, int x2, int z2, float maxCoolDown, float damage) {
        super(instance, x1, z1, x2, z2, maxCoolDown);

        maxCoolDown = 1;
        coolDown = maxCoolDown;

        position = instance.transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(10, 10, 10);

        orientation = 1;
        maxLinearSpeed = 4f;
        maxLinearAcceleration = 3f;
        weaponRange = 0.8f;
        this.damage = damage;

        generateRandomTarget();
        behavior = new Arrive<>(this, target);
    }

    /**
     * updates the Zombie
     *
     * @param delta the time between this frame and the last
     */

    @Override
    public void update(float delta) {
        coolDown = coolDown + delta;

        if (playerInRoom()) {

            target.setVector(player.getEntity().transform.getTranslation(new Vector3()));
            setMaxLinearSpeed(6);
            setMaxLinearAcceleration(10);

            if ((isAround(position, target.vector, weaponRange * 3f)))
                behavior = new Arrive<>(this, target);
            else
                behavior = new Pursue<>(this, target, 0);

        } else {
            if (behavior instanceof Pursue) {
                setMaxLinearAcceleration(3);
                generateRandomTarget();
                behavior = new Arrive<>(behavior.getOwner(), target);
                setMaxLinearSpeed(4);
            } else if ((isAround(position, target.vector, 1)) && behavior.isEnabled()) {

                behavior.setEnabled(false);
                setMaxLinearSpeed(0);
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        generateRandomTarget();
                        setMaxLinearAcceleration(3);
                        setMaxLinearSpeed(4);
                        behavior.setEnabled(true);
                        behavior = new Arrive<>(behavior.getOwner(), target);
                    }
                }, 800);
            }
        }

        if ((isAround(position, target.vector, weaponRange) && playerInRoom())) {
            if (coolDown >= maxCoolDown) {
                target.setVector(player.getEntity().transform.getTranslation(new Vector3()));
                attack();
                coolDown = 0;
            }
        } else if (behavior != null) {
            behavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, delta);
        }
    }

    /**
     * the method called when the zombie attacks
     */
    @Override
    protected void attack() {
        System.out.println("Tu prends " + damage + "dégats");
    }
}