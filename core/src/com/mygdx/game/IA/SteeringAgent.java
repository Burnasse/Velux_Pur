package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;

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
    private float maxLinearSpeed;

    private float angularVelocity = 0;
    private float maxAngularAcceleration = 0;
    private float maxAngularSpeed = 0;


    private SteeringBehavior<Vector3> behavior;
    private Target target = new Target(0, 0, 0);

    private boolean isTagged = false;
    private boolean independentFacing;


    int x1;
    int x2;
    int z1;
    int z2;

    Timer timer = new Timer();

    public SteeringAgent(EntityInstance object, int x1, int z1, int x2, int z2) {
        this.object = object;
        position = object.transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(50, 0, 50);
        independentFacing = false;

        orientation = 0;
        maxLinearSpeed = 3f;

        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;

        generateRandomTarget();
        behavior = new Arrive<>(this, target);
    }

    public void update(float delta) {

        if (playerIsNear()) {
            target.setVector(player.getEntity().transform.getTranslation(new Vector3()));
            target.setBoundingRadius(0.1f);
            setMaxLinearSpeed(4);
            setMaxLinearAcceleration(10);
            behavior = new Pursue<>(this, target, 1);
        } else {
            if (behavior instanceof Pursue) {
                generateRandomTarget();
                behavior = new Seek<>(behavior.getOwner(), target);
                setMaxLinearSpeed(2);
            } else if ((isAround(position.x, target.vector.x, 1) || isAround(position.z, target.vector.z, 1)) && behavior.isEnabled()) {

                behavior.setEnabled(false);
                maxLinearSpeed = 0;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        generateRandomTarget();
                        maxLinearSpeed = 8000f;
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

        // Update position and linear velocity. Velocity is trimmed to maximum speed
        this.position.mulAdd(linearVelocity, time);
        object.transform.translate(new EntityPosition(linearVelocity.x * time, linearVelocity.y * time, linearVelocity.z * time));
        object.body.proceedToTransform(object.transform);
        this.linearVelocity.mulAdd(steering.linear, time).limit(this.getMaxLinearSpeed());

        // Update orientation and angular velocity
        if (independentFacing) {
            this.orientation += angularVelocity * time;
            this.angularVelocity += steering.angular * time;
        } else {
            // For non-independent facing we have to align orientation to linear velocity
            float newOrientation = calculateOrientationFromLinearVelocity(this);
            if (newOrientation != this.orientation) {
                this.angularVelocity = (newOrientation - this.orientation) * time;
                this.orientation = newOrientation;
            }
        }
    }

    private boolean isAround(float position, float target, float value) {
        return position >= target - value && position <= target + value;
    }

    public void generateRandomTarget() {
        target = new Target(ThreadLocalRandom.current().nextInt(x1, x2), 0, ThreadLocalRandom.current().nextInt(z1, z2));
    }

    private boolean playerIsNear() {
        Vector3 playerPosition = player.getEntity().transform.getTranslation(new Vector3());
        return playerPosition.x >= x1 && playerPosition.x <= x2 && playerPosition.z >= z1 && playerPosition.z <= z2;
    }

    public static <T extends Vector<T>> float calculateOrientationFromLinearVelocity(Steerable<T> character) {
        // If we haven't got any velocity, then we can do nothing.
        if (character.getLinearVelocity().isZero(character.getZeroLinearSpeedThreshold()))
            return character.getOrientation();

        return character.vectorToAngle(character.getLinearVelocity());
    }


    public static SteeringAcceleration<Vector3> getSteeringOutput() {
        return steeringOutput;
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
        return (float) Math.atan2(-vector.x, vector.z);
    }

    @Override
    public Vector3 angleToVector(Vector3 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.z = (float) Math.cos(angle);
        return outVector;
    }

    @Override
    public com.badlogic.gdx.ai.utils.Location<Vector3> newLocation() {
        return null;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.2f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
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
        maxLinearSpeed = 2f;
    }
}
