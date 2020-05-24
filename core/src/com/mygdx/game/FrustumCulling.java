package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Entity.instances.EntityInstance;

public class FrustumCulling {

    private Array<EntityInstance> instances;
    private Environment environment;
    private Camera cam;
    private ModelBatch modelBatch;
    private ModelBatch shadowBatch;

    public FrustumCulling(Array<EntityInstance> instances, Environment environment, Camera cam, ModelBatch modelBatch) {
        this.instances = instances;
        this.environment = environment;
        this.cam = cam;
        this.modelBatch = modelBatch;
    }

    public FrustumCulling(Array<EntityInstance> instances, Environment environment, Camera cam, ModelBatch modelBatch, ModelBatch shadowBatch) {
        this(instances,environment,cam,modelBatch);
        this.shadowBatch = shadowBatch;
    }

    public void render(){
        for (final EntityInstance instance : instances) {
            if (isVisible(cam, instance))
                modelBatch.render(instance, environment);
        }
    }

    public void renderShadow(){
        try {
            for (final EntityInstance instance : instances) {
                if (isVisible(cam, instance))
                    shadowBatch.render(instance, environment);
            }
        }catch (NullPointerException e){
            throw new NullPointerException("shadowBatch not initialized");
        }

    }

    private Vector3 position = new Vector3();
    protected boolean isVisible(final Camera cam, final EntityInstance instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return cam.frustum.sphereInFrustum(position, instance.radius);
    }
}
