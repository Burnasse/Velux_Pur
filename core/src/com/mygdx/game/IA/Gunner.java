package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.instances.EntityInstance;

import java.util.ArrayList;
import java.util.TimerTask;

public class Gunner extends SteeringAgent {


    float projectileDamage;

    ArrayList<Projectile> projectilesShot = new ArrayList<>();
    Array<EntityInstance> doneProjectiles = new Array<>();

    public Array<EntityInstance> getDoneProjectiles() {

        for (EntityInstance entityInstance : doneProjectiles) {
            entityInstance.dispose();
        }

        Array<EntityInstance> temp = doneProjectiles;
        doneProjectiles.clear();
        return temp;
    }

    public Gunner(EntityInstance object, int x1, int z1, int x2, int z2) {
        super(object, x1, z1, x2, z2);
        maxCoolDown = 0.1f;
        coolDown = maxCoolDown;

        position = object.transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(0, 0, 0);

        orientation = 1;
        maxLinearSpeed = 2f;
        maxLinearAcceleration = 2f;
        weaponRange = 3;

        generateRandomTarget();
        behavior = new Arrive<>(this, target);
    }

    public Gunner(EntityInstance object, int x1, int z1, int x2, int z2, float maxCoolDown, float weaponRange, float projectileDamage) {
        super(object, x1, z1, x2, z2, maxCoolDown);
        coolDown = maxCoolDown;

        this.instance = object;
        position = object.transform.getTranslation(new Vector3());
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


    @Override
    public void update(float delta) {
        checkProjectiles();
        coolDown = coolDown + delta;
        updateProjectiles(delta);

        if (playerInRoom()) {

            target.setVector(player.getEntity().transform.getTranslation(new Vector3()));
            setMaxLinearSpeed(5);
            setMaxLinearAcceleration(10);

            if ((isAround(position, target.vector, weaponRange * 1.5f)))
                behavior = new Arrive<>(this, target);
            else
                behavior = new Pursue<>(this, target, 0);

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

    protected void attack() {
        projectilesShot.add(new Projectile(target.vector, 10f, instance.transform.getTranslation(new Vector3())));
    }

    private void checkProjectiles() {
        for (Projectile projectile : projectilesShot) {
            if (projectile.isDone())
                doneProjectiles.add(projectile.getInstance());
        }
    }

    private void updateProjectiles(float delta) {
        for (Projectile projectile : projectilesShot) {
            if (projectile.isDone())
                projectile.instance.dispose();
            else projectile.update(delta);
        }
    }

    public Array<EntityInstance> projectiles() {
        Array<EntityInstance> instances = new Array<>();
        for (Projectile projectile :
                projectilesShot) {
            instances.add(projectile.getInstance());
        }
        return instances;
    }
}
