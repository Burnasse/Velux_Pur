package com.mygdx.game.IA;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.FloorLayout.Position;

import java.util.concurrent.ThreadLocalRandom;

public class SteeringAgent implements Steerable<Vector3> {

    float orientation;
    private static final SteeringAcceleration<Vector3> steeringOutput = new SteeringAcceleration<>(new Vector3());
    private Vector3 position;
    private Vector3 linearVelocity;
    private float angularVelocity = 0.5f;
    private float maxSpeed;
    private boolean independentFacing;
    private SteeringBehavior<Vector3> behavior;
    private boolean isTagged = true;
    EntityInstance object;
    Target target = new Target(0,0,0);
    int x1;
    int x2;
    int z1;
    int z2;


    public SteeringAgent(EntityInstance object) {
        this.object = object;
        position = object.transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(1f, 1f, 1f);
        independentFacing = true;
        orientation = 1;
        maxSpeed = 10f;
    }

    public SteeringAgent(EntityInstance object, Position center, int x1, int x2, int z1, int z2) {
        this.object = object;
        position = object.transform.getTranslation(new Vector3());
        linearVelocity = new Vector3(1f, 0, 1f);
        independentFacing = true;
        orientation = 1;
        maxSpeed = 5;
        behavior = new Seek<>(this);
        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;
        generateRandomTarget();
        //routine.setWanderRadius(Math.min(Math.abs(y1-y2)/2,Math.abs(x1-x2)/2));
        //routine.setTarget(new Target(x1, 0 , center.getY()));
    }

    public void generateRandomTarget() {
        if (behavior instanceof Seek) {
            int dim = ThreadLocalRandom.current().nextInt(1, 3);
            if (dim == 1)
                target = new Target(new Vector3(ThreadLocalRandom.current().nextInt(x1, x2), 1, target.getPosition().z));

            if (dim == 2)
                target = new Target(new Vector3(target.getPosition().x, 1, ThreadLocalRandom.current().nextInt(z1, z2)));
            ((Seek<Vector3>) behavior).setTarget(target);
        }
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
        if (behavior != null) {
            behavior.calculateSteering(steeringOutput);

            if (behavior instanceof Seek) {
                if ((int)position.x == (int)target.vector.x || (int)position.z == (int)target.vector.z)
                    generateRandomTarget();
            }

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
