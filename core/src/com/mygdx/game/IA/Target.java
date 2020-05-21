package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector3;

/**
 * The target of the AI
 */

public class Target implements Steerable<Vector3> {

    Vector3 vector;
    float orientation = 0;
    float boundingRadius = 0;

    private Vector3 position;

    private final Vector3 linearVelocity = new Vector3(1, 0, 1);
    private float maxLinearAcceleration = 1;
    private float maxLinearSpeed = 1;

    private float maxAngularAcceleration = 50;
    private float maxAngularSpeed = 50;

    public boolean isTagged = false;

    /**
     * creates a new target
     *
     * @param vector the position
     */

    public Target(Vector3 vector) {
        this.vector = vector;
    }

    /**
     * creates a new target
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     */

    public Target(float x, float y, float z) {
        vector = new Vector3(x, y, z);
    }

    public void setVector(Vector3 vector) {
        this.vector = vector;
    }

    @Override
    public Vector3 getPosition() {
        return vector;
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
    public float vectorToAngle(Vector3 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector3 angleToVector(Vector3 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.z = (float) Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector3> newLocation() {
        return this;
    }

    @Override
    public Vector3 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return 50;
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
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
    public float getZeroLinearSpeedThreshold() {
        return 0.3f;
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

}
