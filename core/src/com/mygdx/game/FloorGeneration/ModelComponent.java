package com.mygdx.game.FloorGeneration;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class ModelComponent extends ModelInstance {

    float x;
    float y;
    float z;

    public ModelComponent(Model model, float x, float y, float z) {
        super(model);
        this.x = x;
        this.y = y;
        this.z = z;
        transform.set(new Vector3(x,y,z),new Quaternion());
    }
}
