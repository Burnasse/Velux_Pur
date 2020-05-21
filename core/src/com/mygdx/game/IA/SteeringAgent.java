package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.physics.DynamicWorld;


import java.util.Timer;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The general behavior of the enemies
 */

public abstract class SteeringAgent implements Steerable<Vector3> {

    protected EntityPlayer player;
    protected EntityInstance instance;
    private DynamicWorld world = null;


    protected float orientation;
    protected static final SteeringAcceleration<Vector3> steeringOutput = new SteeringAcceleration<>(new Vector3());

    protected Vector3 position;

    protected Vector3 linearVelocity;
    protected float maxLinearAcceleration;
    protected float maxLinearSpeed;

    protected float angularVelocity = 1;
    protected float maxAngularAcceleration = 1;
    protected float maxAngularSpeed = 1;

    protected float weaponRange;

    protected SteeringBehavior<Vector3> behavior;
    protected Target target = new Target(0, 0, 0);

    private boolean isTagged = false;

    private int x1;
    private int x2;
    private int z1;
    private int z2;

    protected Timer timer = new Timer();

    protected float coolDown;
    protected float maxCoolDown;

    float blockSize = 1;

    /**
     * Instantiates a new behavior.
     *
     * @param x1 the wandering limit
     * @param x2 the wandering limit
     * @param z1 the wandering limit
     * @param z2 the wandering limit
     */

    public SteeringAgent(EntityInstance instance, int x1, int z1, int x2, int z2) {
        this.instance = instance;
        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;
    }

    /**
     * Instantiates a new Gunner.
     *
     * @param x1          the wandering limit
     * @param x2          the wandering limit
     * @param z1          the wandering limit
     * @param z2          the wandering limit
     * @param maxCoolDown the coolDown of the attack
     */

    public SteeringAgent(EntityInstance instance, int x1, int z1, int x2, int z2, float maxCoolDown) {
        this.instance = instance;
        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;
        this.maxCoolDown = maxCoolDown;
    }

    /**
     * updates the behavior
     *
     * @param delta the time between this frame and the last
     */

    public void update(float delta) {
    }

    /**
     * apply the movement to the behavior
     *
     * @param time the time between this frame and the last
     */

    void Move(float time) {
        instance.transform.trn(new EntityPosition(linearVelocity.x * time, linearVelocity.y * time, linearVelocity.z * time));
        position = instance.transform.getTranslation(new Vector3());
        linearVelocity.mulAdd(SteeringAgent.steeringOutput.linear, time).limit(this.getMaxLinearSpeed());
    }

    /**
     * faces the target
     */

    void Turn() {
        Vector3 direction = new Vector3(target.vector.x - position.x, target.vector.y - position.y, target.vector.z - position.z);
        direction = direction.nor();
        float newOrientation = vectorToAngle(direction);
        if (Math.round(orientation * 10) / 10 != Math.round(newOrientation * 10) / 10) {
            this.orientation = newOrientation;
            instance.transform.rotateRad(new Vector3(0, 1, 0), orientation);
        }
    }

    /**
     * check if position is in a circle that has target as a center
     *
     * @param position the position to check
     * @param target   the target
     * @param radius   the radius
     */

    boolean isAround(Vector3 position, Vector3 target, float radius) {
        return Math.pow((position.x - target.x), 2) + Math.pow((position.z - target.z), 2) <= radius * radius;
    }

    /**
     * Generates a new target within the room
     */

    protected void generateRandomTarget() {
        target = new Target(ThreadLocalRandom.current().nextInt(x1, x2), blockSize, ThreadLocalRandom.current().nextInt(z1, z2));
    }

    /**
     * checks if the player is in the room
     */

    protected boolean playerInRoom() {
        Vector3 playerPosition = player.getEntity().transform.getTranslation(new Vector3());
        return playerPosition.x >= x1 && playerPosition.x <= x2 && playerPosition.z >= z1 - 1 && playerPosition.z <= z2 + 1;
    }

    /**
     * return an angle from characters linear velocity
     *
     * @param character the character from who the angle is taken
     */

    protected static <T extends Vector<T>> float calculateOrientationFromLinearVelocity(Steerable<T> character) {
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

    /**
     * convert a vector to an angle
     *
     * @param vector the vector to convert
     */

    @Override
    public float vectorToAngle(Vector3 vector) {
        return (float) Math.atan2(-vector.x, vector.z);
    }

    /**
     * create an angle from a vector
     *
     * @param outVector the vector to return
     * @param angle     the angle to convert
     */

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

    public DynamicWorld getWorld() {
        return world;
    }

    public void Surroundings(EntityPlayer player, DynamicWorld world) {
        this.player = player;
        this.world = world;
    }

    protected void attack() {
    }

    /**
     * Adapt the AI to the blocks of the floor
     *
     * @param blockSize the size of the floor's blocks
     */

    public void adaptToFloor(int blockSize) {
        x1 = x1 * blockSize;
        z1 = z1 * blockSize;
        x2 = x2 * blockSize;
        z2 = z2 * blockSize;
        this.blockSize = blockSize + 1;
        position.y = blockSize + 1;
        generateRandomTarget();
        behavior = new Arrive<>(this, target);
    }
}