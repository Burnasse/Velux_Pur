package com.mygdx.game.animation;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

public class SwordAnimation extends AnimationController {
    /**
     * Construct a new AnimationController.
     *
     * @param target The {@link ModelInstance} on which the animations will be performed.
     */
    public SwordAnimation(ModelInstance target) {
        super(target);
    }
}
