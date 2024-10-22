package com.mygdx.game.IA;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.gameGeneration.GenerateLevel;
import com.mygdx.game.physics.CallbackFlags;
import com.mygdx.game.physics.DynamicWorld;


/**
 * the Projectile class
 */

public class Projectile {

    public EntityInstance instance;
    private Vector3 direction;
    private final float speed;
    private float remainingTime = 1.5f;
    private boolean isDone = false;

    /**
     * Instanciates a new Projectile
     *
     * @param target          the target of the shot
     * @param speed           projectile's speed
     * @param initialPosition from where the projectile is shot
     * @param world           the world where the projectile wanders
     */

    public Projectile(Vector3 target, float speed, Vector3 initialPosition, DynamicWorld world) {

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLACK)))
                .box(0.1f, 0.1f, 0.1f);
        Model model = modelBuilder.end();

        btBoxShape btBoxShape = new btBoxShape(new Vector3(0.1f, 0.1f, 0.1f));

        int projectile = 50;

        instance = new EntityInstance(model, btBoxShape, 0.1f, new EntityPosition(initialPosition.x, initialPosition.y + 1f, initialPosition.z));
        direction = new Vector3(target.x - initialPosition.x, 0, target.z - initialPosition.z);
        direction = direction.nor();

        instance.getBody().setUserValue(3000);
        instance.getBody().setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK | btCollisionObject.CollisionFlags.CF_NO_CONTACT_RESPONSE);
        instance.getBody().setActivationState(Collision.DISABLE_DEACTIVATION);
        instance.getBody().setContactCallbackFlag(projectile);
        instance.getBody().setContactCallbackFilter(CallbackFlags.PLAYER_FLAG);

        world.addRigidBody(instance.getBody());

        this.speed = speed;
    }

    /**
     * updates the projectile
     *
     * @param deltaTime the time between this frame and the last
     */

    void update(float deltaTime) {
        if (remainingTime > 0) {
            instance.transform.translate(new EntityPosition(direction.x * speed * deltaTime, 0, direction.z * speed * deltaTime));
            instance.getBody().proceedToTransform(instance.transform);
            remainingTime = remainingTime - deltaTime;
        } else {
            isDone = true;
        }
    }

    public boolean isDone() {
        return isDone;
    }


    public EntityInstance getInstance() {
        return instance;
    }
}
