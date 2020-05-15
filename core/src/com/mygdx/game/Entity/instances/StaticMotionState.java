package com.mygdx.game.Entity.instances;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class StaticMotionState {

    /**
     * The type My motion state is called when an Entity is transformed.
     * It's useful for avoid iteration on render() method.
     */
    public static class MotionState extends btMotionState {

        Matrix4 transform;

        @Override
        public void getWorldTransform(Matrix4 worldTrans) {
            worldTrans.set(transform);
        }

        @Override
        public void setWorldTransform(Matrix4 worldTrans) {
            transform.set(worldTrans);
        }
    }
}


