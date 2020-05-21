package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.utils.EntityPosition;

public class NonPlayerCharacter extends ModelInstance{

    public enum AnimationID{
        IDLE,
        SITTING
    }

    private AnimationController controller;

    public NonPlayerCharacter(Assets assets, EntityPosition entityPosition, AnimationID defaultAnimation){
        super(init(assets));
        controller = new AnimationController(this);
        transform.trn(entityPosition).rotate(Vector3.Y,180);
        controller.animate(defaultAnimation.name().toLowerCase(), -1, 1.0f, null, 0.2f);
    }

    private static ModelInstance init(Assets assets){
        ModelInstance modelInstance = new ModelInstance(assets.manager.get(Assets.npcModel));
        modelInstance.copyAnimation(assets.manager.get(Assets.sittingNpcModel).animations.get(0));
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
