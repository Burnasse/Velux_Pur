package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class SteeringAgent implements Steerable<Vector3> {

    private EntityPlayer player;
    private EntityInstance object;


    private float orientation;
    private static final SteeringAcceleration<Vector3> steeringOutput = new SteeringAcceleration<>(new Vector3());

    private Vector3 position;

    private Vector3 linearVelocity;
    private float maxLinearAcceleration = 1;
    private float maxLinearVelocity;

    private float angularVelocity = 5000;
    private float maxAngularAcceleration = 5000;
    private float maxAngularSpeed = 5000;

    float weaponRange;

    private ArrayList<Projectile> projectilesShot = new ArrayList<>();

    private SteeringBehavior<Vector3> behavior;
    private Target target = new Target(0, 0, 0);

    private boolean isTagged = false;
    private boolean independentFacing = false;

    int x1;
    int x2;
    int z1;
    int z2;

    Timer timer = new Timer();

    public SteeringAgent(EntityInstance object, int x1, int z1, int x2, int z2) {
        this.object = object;
        position = object.transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(50, 50, 50);
        independentFacing = false;

        orientation = 1;
        maxLinearVelocity = 3f;

        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;

        generateRandomTarget();
        behavior = new Arrive<>(this, target);
        weaponRange = 3f;

    }

    public void update(float delta) {

        if (playerInRoom()) {

            target.setVector(player.getEntity().transform.getTranslation(new Vector3()));
            setMaxLinearSpeed(3);
            setMaxLinearAcceleration(10);

            if ((isAround(position, target.vector, weaponRange * 1.5f) && weaponRange < 1))
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
                maxLinearVelocity = 0f;
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        generateRandomTarget();
                        maxLinearVelocity = 8000f;
                        behavior.setEnabled(true);
                        behavior = new Arrive<>(behavior.getOwner(), target);
                    }
                }, 800);
            }
        }

        if (behavior != null) {
            behavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, delta);
        }
    }

    private void applySteering(SteeringAcceleration<Vector3> steering, float time) {
        updateProjectiles(time);
        // Update position and linear velocity. Velocity is trimmed to maximum speed
        this.position.mulAdd(linearVelocity, time);
        object.transform.translate(new EntityPosition(linearVelocity.x * time, linearVelocity.y * time, linearVelocity.z * time));
        object.body.proceedToTransform(object.transform);
        this.linearVelocity.mulAdd(steering.linear, time).limit(this.getMaxLinearSpeed());

        // Update orientation and angular velocity
        if (independentFacing) {
            this.orientation += angularVelocity * time;
            this.angularVelocity += steering.angular * time;

        }
        if (!independentFacing) {
            // For non-independent facing we have to align orientation to linear velocity
            float newOrientation = vectorToAngle(linearVelocity);
            if (newOrientation != this.orientation) {
                this.angularVelocity = (newOrientation - this.orientation) * time;
                this.orientation = newOrientation;
            }
        }
        if ((isAround(position, target.vector, weaponRange) && playerInRoom()) && weaponRange > 1 && !(behavior instanceof Evade)) {
            shootProjectile();
        }

    }

    private boolean isAround(Vector3 position, Vector3 target, float radius) {
        return Math.pow((position.x - target.x), 2) + Math.pow((position.z - target.z), 2) <= radius * radius;
    }

    public void generateRandomTarget() {
        target = new Target(ThreadLocalRandom.current().nextInt(x1, x2), 0, ThreadLocalRandom.current().nextInt(z1, z2));
    }

    private boolean playerInRoom() {
        Vector3 playerPosition = player.getEntity().transform.getTranslation(new Vector3());
        return playerPosition.x >= x1 && playerPosition.x <= x2 && playerPosition.z >= z1 - 1 && playerPosition.z <= z2 + 1;
    }

    public static <T extends Vector<T>> float calculateOrientationFromLinearVelocity(Steerable<T> character) {
        // If we haven't got any velocity, then we can do nothing.
        if (character.getLinearVelocity().isZero(character.getZeroLinearSpeedThreshold()))
            return character.getOrientation();

        return character.vectorToAngle(character.getLinearVelocity());
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public Vector3 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return 0;
    }

    @Override
    public boolean isTagged() {
        return isTagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.isTagged = tagged;
    }

    @Override
    public float vectorToAngle(Vector3 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector3 angleToVector(Vector3 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.z = (float) Math.tan(angle);
        return outVector;
    }

    @Override
    public com.badlogic.gdx.ai.utils.Location<Vector3> newLocation() {
        return null;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearVelocity;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearVelocity = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }


    public SteeringAgent(EntityInstance object) {  //semble inutile, peut-être que je le virerai dans le futur
        this.object = object;
        position = object.transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(0f, 0, 0f);
        independentFacing = true;
        orientation = 80;
        maxLinearVelocity = 2f;
    }

    private void shootProjectile() {
        projectilesShot.add(new Projectile(target.vector, 8f, position));
    }

    private void updateProjectiles(float delta) {
        for (Projectile projectile : projectilesShot)
            projectile.Update(delta);
    }

    public Array<? extends EntityInstance> projectiles() {
        Array<EntityInstance> instances = new Array<>();
        for (Projectile projectile :
                projectilesShot) {
            instances.add(projectile.getInstance());
        }
        return instances;
    }
}
