package com.mygdx.game.IA;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector3;

public class Truc implements Location<Vector3> {
    Vector3 vector;

    public void setVector(Vector3 vector) {
        this.vector = vector;
    }

    @Override
    public Vector3 getPosition() {
        return vector;
    }

    @Override
    public float getOrientation() {
        return 0;
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public float vectorToAngle(Vector3 vector) {
        return 0;
    }

    @Override
    public Vector3 angleToVector(Vector3 outVector, float angle) {
        return null;
    }

    @Override
    public Location<Vector3> newLocation() {
        return null;
    }
}
