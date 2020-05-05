package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector3;

public class Target implements Steerable<Vector3> {

    Vector3 vector;
    float orientation = 0;

    public Target(Vector3 vector) {
        this.vector = vector;
    }

    public Target(float x, float y, float z) {
        vector = new Vector3(x, y, z);
        orientation = vectorToAngle(vector);
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
        return (float) Math.atan2(-vector.x, vector.z);
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
        return new Vector3(0,0,0);
    }

    @Override
    public float getAngularVelocity() {
        return 1;
    }

    @Override
    public float getBoundingRadius() {
        return 1f;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {

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
        return 1;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {

    }

    @Override
    public float getMaxLinearAcceleration() {
        return 1;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {

    }

    @Override
    public float getMaxAngularSpeed() {
        return 1;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {

    }

    @Override
    public float getMaxAngularAcceleration() {
        return 1;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {

    }
}
