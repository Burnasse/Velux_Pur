package com.mygdx.game.Entity;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.mygdx.game.Entity.utils.EntityPosition;

/**
 * The type Player factory.
 */
public class PlayerFactory {

    /**
     * Create entity player.
     *
     * @param position the position
     * @return the entity player
     */
    public static EntityPlayer create(EntityPosition position) {
        EntityPlayer player;

        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        Model idleModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/idleG3D.g3db", Files.FileType.Internal));
        Model runningModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/runningG3D.g3db", Files.FileType.Internal));
        Model walkModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/walkG3D.g3db", Files.FileType.Internal));
        Model jumpModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/jumpG3D.g3db", Files.FileType.Internal));
        Model dodgeModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/dodge.g3db", Files.FileType.Internal));

        idleModel.animations.get(0).id = "idle";
        runningModel.animations.get(0).id = "running";
        walkModel.animations.get(0).id = "walk";
        jumpModel.animations.get(0).id = "jump";
        dodgeModel.animations.get(0).id = "dodge";

        player = new EntityPlayer("Player", idleModel, position);

        player.getEntity().copyAnimation(runningModel.animations.get(0));
        player.getEntity().copyAnimation(walkModel.animations.get(0));
        player.getEntity().copyAnimation(jumpModel.animations.get(0));
        player.getEntity().copyAnimation(dodgeModel.animations.get(0));

        runningModel.dispose();
        walkModel.dispose();
        jumpModel.dispose();
        dodgeModel.dispose();

        return player;
    }

}
