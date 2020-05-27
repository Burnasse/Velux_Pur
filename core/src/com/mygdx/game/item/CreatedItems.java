package com.mygdx.game.item;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.Entity.utils.EntityPosition;

public class CreatedItems {

    public static WeaponCaC getSword() {
        AssetManager ass = new AssetManager();
        ass.load("sting.g3db",Model.class);
        ass.finishLoading();
        Model swordModel = ass.get("sting.g3db",Model.class);

        WeaponCaC sword = new WeaponCaC(5, swordModel, new btBoxShape(new Vector3(0.4f, 0.1f, 0.1f)), new EntityPosition(0, 0, 0));
        return sword;
    }
}
