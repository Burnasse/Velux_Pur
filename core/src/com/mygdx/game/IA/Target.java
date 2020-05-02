package com.mygdx.game.IA;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector3;

public class Target implements Location<Vector3> {
    Vector3 vector;
    float orientation = 0;

    public Target(Vector3 vector) {
        this.vector = vector;
    }

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
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector3> newLocation() {
        return this;
    }
}
