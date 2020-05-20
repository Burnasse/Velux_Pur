package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.utils.EntityPosition;

public class Pnj extends ModelInstance{

    public enum AnimationID{
        IDLE,
        SITTING
    }

    private AnimationController controller;

    public Pnj(EntityPosition entityPosition, AnimationID defaultAnimation){
        super(init());
        controller = new AnimationController(this);
        transform.trn(entityPosition).rotate(Vector3.Y,180);
        controller.animate(defaultAnimation.name().toLowerCase(), -1, 1.0f, null, 0.2f);
    }

    private static ModelInstance init(){
        AssetManager assetManager = new AssetManager();
        assetManager.load("Pnj/pnj.g3db", Model.class);
        assetManager.load("Pnj/sittingPnj.g3db", Model.class);
        assetManager.finishLoading();

        ModelInstance modelInstance = new ModelInstance(assetManager.get("Pnj/pnj.g3db",Model.class));
        modelInstance.copyAnimation(assetManager.get("Pnj/sittingPnj.g3db", Model.class).animations.get(0));
        modelInstance.animations.get(0).id = "idle";
        modelInstance.animations.get(1).id = "sitting";

        return modelInstance;
    }

    public void render(){
        controller.update(Gdx.graphics.getDeltaTime());
    }

    public AnimationController getController() {
        return controller;
    }
}
