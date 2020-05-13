package com.mygdx.game.village;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.utils.EntityPosition;

public class PlayerFactory {

    public static EntityPlayer create(EntityPosition position){
        EntityPlayer player;

        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        Model idleModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/roger.g3db", Files.FileType.Internal));
        Model runningModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/runningG3D.g3db", Files.FileType.Internal));
        Model walkModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/walkG3D.g3db", Files.FileType.Internal));
        Model jumpModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/jumpG3D.g3db", Files.FileType.Internal));
        Model upStairsModel = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/upStairsG3D.g3db", Files.FileType.Internal));
        Model downStairs = modelLoader.loadModel(Gdx.files.getFileHandle("PlayerAssets/upStairsG3D.g3db", Files.FileType.Internal));

        idleModel.animations.get(0).id = "idle";
        runningModel.animations.get(0).id = "running";
        walkModel.animations.get(0).id = "walk";
        jumpModel.animations.get(0).id = "jump";
        upStairsModel.animations.get(0).id = "upStairs";
        downStairs.animations.get(0).id = "downStairs";

        player = new EntityPlayer("Player", idleModel, position);

        player.getEntity().copyAnimation(runningModel.animations.get(0));
        player.getEntity().copyAnimation(walkModel.animations.get(0));
        player.getEntity().copyAnimation(jumpModel.animations.get(0));
        player.getEntity().copyAnimation(upStairsModel.animations.get(0));
        player.getEntity().copyAnimation(downStairs.animations.get(0));

        runningModel.dispose();
        walkModel.dispose();
        jumpModel.dispose();
        upStairsModel.dispose();
        downStairs.dispose();

        return player;
    }

}
