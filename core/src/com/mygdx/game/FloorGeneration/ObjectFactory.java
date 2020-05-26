package com.mygdx.game.FloorGeneration;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.mygdx.game.Entity.EntityObjects;
import com.mygdx.game.Entity.utils.EntityPosition;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Object factory.
 */
public class ObjectFactory {

    private ModelBuilder modelBuilder;
    private Material material;
    private long attributes;


    /**
     * Instantiates a new Object factory.
     */
    public ObjectFactory(){
        modelBuilder = new ModelBuilder();

        material = new Material(ColorAttribute.createDiffuse(Color.GRAY));
        attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal;

    }

    /**
     * Create random entity objects.
     *
     * @param x1        the x 1
     * @param y1        the y 1
     * @param x2        the x 2
     * @param y2        the y 2
     * @param blockSize the block size
     * @return the entity objects
     */
    public EntityObjects createRandom(int x1, int y1, int x2, int y2, float blockSize){
        int rand = ThreadLocalRandom.current().nextInt(0,3);
        float x = ThreadLocalRandom.current().nextInt(x1,x2)*blockSize;
        float z = ThreadLocalRandom.current().nextInt(y1,y2)*blockSize;
        Model model = null;
        if(rand == 0){
            int rad = ThreadLocalRandom.current().nextInt(1,3);
            model = modelBuilder.createSphere(rad, rad, rad,20,20,material,attributes);
        }
        else if(rand == 1){
            model = modelBuilder.createBox(ThreadLocalRandom.current().nextInt(1,3),
                    ThreadLocalRandom.current().nextInt(1,3),
                    ThreadLocalRandom.current().nextInt(1,3),material,attributes);
        }
        else {
            model = modelBuilder.createCylinder(ThreadLocalRandom.current().nextInt(1,3),
                    ThreadLocalRandom.current().nextInt(1,3),
                    ThreadLocalRandom.current().nextInt(1,3),8,material,attributes);
        }

        return new EntityObjects("Object", model, Bullet.obtainStaticNodeShape(model.nodes),5f,new EntityPosition(x,4,z));
    }

}
