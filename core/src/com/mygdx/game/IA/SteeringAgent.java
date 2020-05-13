package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;

import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

public abstract class SteeringAgent implements Steerable<Vector3> {

    EntityPlayer player;
    EntityInstance instance;


    float orientation;
    static final SteeringAcceleration<Vector3> steeringOutput = new SteeringAcceleration<>(new Vector3());

    Vector3 position;

    Vector3 linearVelocity;
    float maxLinearAcceleration;
    float maxLinearSpeed;

    float angularVelocity = 0;
    float maxAngularAcceleration = 0;
    float maxAngularSpeed = 0;

    float weaponRange;

    SteeringBehavior<Vector3> behavior;
    Target target = new Target(0, 0, 0);

    boolean isTagged = false;
    final boolean independentFacing = false;

    int x1;
    int x2;
    int z1;
    int z2;

    Timer timer = new Timer();

    float coolDown;
    float maxCoolDown;

    public SteeringAgent(EntityInstance instance, int x1, int z1, int x2, int z2) {
        this.instance = instance;
        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;
    }

    public SteeringAgent(EntityInstance instance, int x1, int z1, int x2, int z2, float maxCoolDown) {
        this.instance = instance;
        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;
        this.maxCoolDown = maxCoolDown;
    }

    public void update(float delta) {
    }

    void applySteering(SteeringAcceleration<Vector3> steering, float time) {

        // Update position and linear velocity. Velocity is trimmed to maximum speed
        this.position.mulAdd(linearVelocity, time);
        instance.transform.translate(new EntityPosition(linearVelocity.x * time, linearVelocity.y * time, linearVelocity.z * time));
        this.linearVelocity.mulAdd(steering.linear, time).limit(this.getMaxLinearSpeed());
        instance.body.proceedToTransform(instance.transform);

        // Update orientation and angular velocity
        if (independentFacing) {
            this.orientation += angularVelocity * time;
            this.angularVelocity += steering.angular * time;
        }

        if (!independentFacing) {
            // For non-independent facing we have to align orientation to linear velocity
            float newOrientation = vectorToAngle(target.vector);
            if (Math.abs(newOrientation) != this.orientation) {
                this.angularVelocity = newOrientation - orientation * time;
                this.orientation = Math.abs(newOrientation);
                instance.transform.rotateRad(instance.transform.getTranslation(new Vector3()),orientation);
                instance.body.proceedToTransform(instance.transform);
            }
        }


    }

    boolean isAround(Vector3 position, Vector3 target, float radius) {
        return Math.pow((position.x - target.x), 2) + Math.pow((position.z - target.z), 2) <= radius * radius;
    }

    public void generateRandomTarget() {
        target = new Target(ThreadLocalRandom.current().nextInt(x1, x2), 0, ThreadLocalRandom.current().nextInt(z1, z2));
    }

    boolean playerInRoom() {
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
}
