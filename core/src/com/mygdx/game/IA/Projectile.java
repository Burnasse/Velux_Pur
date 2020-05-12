package com.mygdx.game.IA;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.utils.EntityPosition;

public class Projectile {

    private EntityInstance instance;
    private Vector3 target;
    private float speed;
    private float remainingTime = 1;

    public Projectile(Vector3 target, float speed, Vector3 initialPosition) {

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                .box(0.1f, 0.1f, 0.1f);
        Model model = modelBuilder.end();

        Bullet.init();
        btBoxShape btBoxShape = new btBoxShape(new Vector3(0.1f, 0.1f, 0.1f));

        instance = new EntityInstance(model, btBoxShape, 50f, new EntityPosition(initialPosition.x,1,initialPosition.z));

        this.target = target;
        this.speed = speed;
    }

    void Update(float deltaTime)
    {
        remainingTime = remainingTime - deltaTime;
        Vector3 projectilePos = instance.transform.getTranslation(new Vector3());

        float xDistance = (target.x - projectilePos.x);
        float zDistance = (target.z - projectilePos.z);

        // Integrate the velocity into the position.

        instance.transform.translate(new EntityPosition( speed * deltaTime, 0,   speed * deltaTime));


        if (remainingTime <= 0)

        instance.body.proceedToTransform(instance.transform);
    }

    public EntityInstance getInstance() {
        return instance;
    }
}
