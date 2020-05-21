package com.mygdx.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    public AssetManager manager = new AssetManager();

    /* Load UI */
    public static final AssetDescriptor<TextureAtlas> menuTextureAltas =
            new AssetDescriptor<>("menuAssets/UI.atlas", TextureAtlas.class);

    public static final AssetDescriptor<Skin> menuSkin =
            new AssetDescriptor<>("menuAssets/UI.json", Skin.class);

    /* Load player */
    public static final AssetDescriptor<Model> playerIdleModel =
            new AssetDescriptor<Model>("PlayerAssets/idleG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> playerRunningModel =
            new AssetDescriptor<Model>("PlayerAssets/runningG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> playerWalkModel =
            new AssetDescriptor<Model>("PlayerAssets/walkG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> playerJumpModel =
            new AssetDescriptor<Model>("PlayerAssets/jumpG3D.g3db", Model.class);

    /* Load npc*/
    public static final AssetDescriptor<Model> npcModel =
            new AssetDescriptor<Model>("npc/npc.g3db", Model.class);

    public static final AssetDescriptor<Model> sittingNpcModel =
            new AssetDescriptor<Model>("npc/sittingNpc.g3db", Model.class);

    /* Load sky and groud in the village */
    public static final AssetDescriptor<Model> skyBoxVillage =
            new AssetDescriptor<Model>("skyBox.g3db", Model.class);

    public static final AssetDescriptor<Model> groundVillage =
            new AssetDescriptor<Model>("groundG3D.g3db", Model.class);

    /* Load floor */
    public static final AssetDescriptor<Model> wallLevel =
            new AssetDescriptor<Model>("wallG3D.g3db", Model.class);
    public static final AssetDescriptor<Model> groundLevel =
            new AssetDescriptor<Model>("ground.g3db", Model.class);

    /**
     * Load elements specific to the menu
     */
    public void loadMenu(){
        manager.load(menuSkin);
        manager.load(menuTextureAltas);
    }

    public void loadGame(){
        manager.load(playerIdleModel);
        manager.load(playerRunningModel);
        manager.load(playerWalkModel);
        manager.load(playerJumpModel);
    }

    /**
     * Load elements specific to the village
     */
    public void loadVillage(){
        manager.load(npcModel);
        manager.load(sittingNpcModel);

        manager.load(skyBoxVillage);
        manager.load(groundVillage);
    }

    /**
     * Load elements specific to the level
     */
    public void loadLevel(){
        manager.load(wallLevel);
        manager.load(groundLevel);
    }

    /**
     * Unload elements specific to the village
     */
    public void unloadVillage(){
        manager.unload(npcModel.fileName);
        manager.unload(sittingNpcModel.fileName);
        manager.unload(skyBoxVillage.fileName);
        manager.unload(groundVillage.fileName);
    }

    /**
     * Load elements specific to the level
     */
    public void unloadLevel(){
        manager.unload(wallLevel.fileName);
        manager.unload(groundLevel.fileName);
    }

    /**
     * Load elements specific to the game
     */
    public void unloadGame(){
        manager.unload(playerIdleModel.fileName);
        manager.unload(playerRunningModel.fileName);
        manager.unload(playerWalkModel.fileName);
        manager.unload(playerJumpModel.fileName);
    }

    public void dispose(){
        manager.dispose();
    }
}
