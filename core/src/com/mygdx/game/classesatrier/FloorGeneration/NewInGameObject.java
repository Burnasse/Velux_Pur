package com.mygdx.game.classesatrier.FloorGeneration;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.classesatrier.EntityPosition;

public class NewInGameObject extends ModelInstance implements Disposable {

    static class MyMotionState extends btMotionState {
        Matrix4 transform;
        @Override
        public void getWorldTransform (Matrix4 worldTrans) {
            worldTrans.set(transform);
        }
        @Override
        public void setWorldTransform (Matrix4 worldTrans) {
            transform.set(worldTrans);
        }
    }


    public String node;
    public final btRigidBody body;
    public final MyMotionState motionState;
    public final Model model;
    public final btCollisionShape shape;
    private static Vector3 localInertia = new Vector3();
    public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;

    public NewInGameObject(String node, Model model, btCollisionShape shape, float mass, EntityPosition position){
        super(model);
        this.model = model;
        this.node = node;
        this.shape = shape;
        if (mass > 0f)
            shape.calculateLocalInertia(mass, localInertia);
        else
            localInertia.set(0, 0, 0);
        this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
        motionState = new MyMotionState();
        motionState.transform = transform;
        body = new btRigidBody(constructionInfo);
        body.setMotionState(motionState);
        body.setCollisionShape(shape);

        move(position);
    }


    @Override
    public void dispose() {
        body.dispose();
        shape.dispose();
        motionState.dispose();
        constructionInfo.dispose();
    }

    public void move(EntityPosition position){
        super.transform.trn(position);
        this.body.proceedToTransform(this.transform);
    }

}
