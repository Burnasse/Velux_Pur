package com.mygdx.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    public AssetManager manager = new AssetManager();

    public static final AssetDescriptor<Texture> mainBackground =
            new AssetDescriptor<>("PhotoMenuVelux.jpeg", Texture.class);

    /* Load UI */
    public static final AssetDescriptor<TextureAtlas> menuTextureAltas =
            new AssetDescriptor<>("menuAssets/UI.atlas", TextureAtlas.class);

    public static final AssetDescriptor<Skin> menuSkin =
            new AssetDescriptor<>("menuAssets/UI.json", Skin.class);

    /* Load player */
    public static final AssetDescriptor<Model> playerIdleModel =
            new AssetDescriptor<>("PlayerAssets/idleG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> playerRunningModel =
            new AssetDescriptor<>("PlayerAssets/runningG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> playerWalkModel =
            new AssetDescriptor<>("PlayerAssets/walkG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> playerJumpModel =
            new AssetDescriptor<>("PlayerAssets/jumpG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> playerDodge =
            new AssetDescriptor<>("PlayerAssets/dodgeG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> playerDance =
            new AssetDescriptor<>("PlayerAssets/dance/dance.g3db", Model.class);

    public static final AssetDescriptor<Model> playerChicken =
            new AssetDescriptor<>("PlayerAssets/dance/chicken.g3db", Model.class);

    public static final AssetDescriptor<Model> playerMacarena =
            new AssetDescriptor<>("PlayerAssets/dance/macarena.g3db", Model.class);

    public static final AssetDescriptor<Model> playerShuffle =
            new AssetDescriptor<>("PlayerAssets/dance/shuffle.g3db", Model.class);

    public static final AssetDescriptor<Model> playerThriller =
            new AssetDescriptor<>("PlayerAssets/dance/thriller.g3db", Model.class);

    /* Load npc*/
    public static final AssetDescriptor<Model> npcModel =
            new AssetDescriptor<>("npc/npc.g3db", Model.class);

    public static final AssetDescriptor<Model> sittingNpcModel =
            new AssetDescriptor<>("npc/sittingNpc.g3db", Model.class);

    /* Load sky and groud in the village */
    public static final AssetDescriptor<Model> skyBoxVillage =
            new AssetDescriptor<>("skyBox.g3db", Model.class);

    public static final AssetDescriptor<Model> groundVillage =
            new AssetDescriptor<>("groundG3D.g3db", Model.class);

    /* Load floor */
    public static final AssetDescriptor<Model> wallLevel =
            new AssetDescriptor<>("wallG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> groundLevel =
            new AssetDescriptor<>("ground.g3db", Model.class);

    /* Load enemy*/
    public static final AssetDescriptor<Model> enemyModel =
            new AssetDescriptor<>("enemies/enemyG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> enemyRun =
            new AssetDescriptor<>("enemies/enemyRunG3D.g3db", Model.class);

    public static final AssetDescriptor<Model> enemyFire =
            new AssetDescriptor<>("enemies/enemyFireG3D.g3db", Model.class);

    /**
     * Load elements specific to the menu
     */
    public void loadMenu(){
        manager.load(mainBackground);
        manager.load(menuSkin);
        manager.load(menuTextureAltas);
    }

    public void loadGame(){
        manager.load(playerIdleModel);
        manager.load(playerRunningModel);
        manager.load(playerWalkModel);
        manager.load(playerJumpModel);
        manager.load(playerDodge);
        manager.load(playerDance);
        manager.load(playerChicken);
        manager.load(playerMacarena);
        manager.load(playerShuffle);
        manager.load(playerThriller);
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
        manager.load(enemyModel);
        manager.load(enemyRun);
        manager.load(enemyFire);
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
