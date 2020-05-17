package com.mygdx.game.IA;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.mygdx.game.Entity.instances.Entity;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FloorGeneration.DynamicWorld;

public class Projectile {

    public EntityInstance instance;
    private Vector3 direction;
    private float speed;
    private float remainingTime = 1f;
    private boolean isDone = false;

    public Projectile(Vector3 target, float speed, Vector3 initialPosition, DynamicWorld world) {

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                .box(0.1f, 0.1f, 0.1f);
        Model model = modelBuilder.end();

        btBoxShape btBoxShape = new btBoxShape(new Vector3(0.1f, 0.1f, 0.1f));

int projectile = 50;

        instance = new EntityInstance(model, btBoxShape, 0.1f, new EntityPosition(initialPosition.x, 1, initialPosition.z));
        direction = new Vector3(target.x - initialPosition.x, target.y - initialPosition.y, target.z - initialPosition.z);
        direction = direction.nor();
        instance.getBody().setUserValue(1500);
        instance.getBody().setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        instance.getBody().setContactCallbackFlag(projectile);
        instance.getBody().setContactCallbackFilter(100);
        world.addRigidBody((btRigidBody) instance.getBody());



        this.speed = speed;
    }

    void update(float deltaTime) {
        if (remainingTime > 0) {
            instance.transform.translate(new EntityPosition(direction.x * speed * deltaTime, 0, direction.z * speed * deltaTime));
            instance.body.proceedToTransform(instance.transform);
            remainingTime = remainingTime - deltaTime;
        }
        else{
            isDone = true;
        }
    }

    boolean checkCollision (EntityInstance mInstance){
        return mInstance.model.calculateBoundingBox(new BoundingBox()).contains(instance.model.calculateBoundingBox(new BoundingBox()));
    }


    public boolean isDone() {
        return isDone;
    }


    public EntityInstance getInstance() {
        return instance;
    }
}
