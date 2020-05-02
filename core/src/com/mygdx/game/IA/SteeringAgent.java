package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.instances.EntityInstance;

public class SteeringAgent implements Steerable<Vector3> {

    float orientation;
    private static final SteeringAcceleration<Vector3> steeringOutput = new SteeringAcceleration<>(new Vector3());
    private Vector3 position;
    private Vector3 linearVelocity;
    private float angularVelocity = 0.5f;
    private float maxSpeed;
    private boolean independentFacing;
    private SteeringBehavior<Vector3> steeringBehavior;
    private boolean isTagged = true;
    EntityInstance object;

    public SteeringAgent(EntityInstance object) {
        this.object = object;
        position = object.transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(1f, 1f, 1f);
        independentFacing = true;
        orientation = 1;
        maxSpeed = 80f;
        Truc truc = new Truc();
        truc.setVector(new Vector3(5,20,5));
        //steeringBehavior = new Wander<>(this);
        steeringBehavior = new Seek<>(this, truc);
    }

    public static <T extends Vector<T>> float calculateOrientationFromLinearVelocity(Steerable<T> character) {
        // If we haven't got any velocity, then we can do nothing.
        if (character.getLinearVelocity().isZero(character.getZeroLinearSpeedThreshold()))
            return character.getOrientation();

        return character.vectorToAngle(character.getLinearVelocity());
    }

    private void applySteering(SteeringAcceleration<Vector3> steering, float time) {
        // Update position and linear velocity. Velocity is trimmed to maximum speed
        this.position.mulAdd(linearVelocity, time);
        object.transform.trn(linearVelocity.x * time, linearVelocity.y * time, linearVelocity.z * time);
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

    public void update(float delta) {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);

            /*
             * Here you might want to add a motor control layer filtering steering accelerations.
             *
             * For instance, a car in a driving game has physical constraints on its movement:
             * - it cannot turn while stationary
             * - the faster it moves, the slower it can turn (without going into a skid)
             * - it can brake much more quickly than it can accelerate
             * - it only moves in the direction it is facing (ignoring power slides)
             */

            applySteering(steeringOutput, delta);
        }
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

    public void translate(float x, float y, float z) {
        position.add(x, y, z);
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
        outVector.y = (float) Math.cos(angle);
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
        return maxSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
    }

    @Override
    public float getMaxLinearAcceleration() {
        return 80;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {

    }

    @Override
    public float getMaxAngularSpeed() {
        return 80;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
    }

    @Override
    public float getMaxAngularAcceleration() {
        return 80;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
    }
}
