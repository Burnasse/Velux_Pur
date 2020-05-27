package com.mygdx.game.desktop;

import com.mygdx.game.Assets;

public class AssetsTest {
    Assets assets;

    public AssetsTest(){
        assets = new Assets();

        assets.loadVillage();
        assets.loadAudio();
        assets.loadMenu();
        assets.loadGame();
        assets.loadLevel();

        assets.manager.finishLoading();

        assets.unloadGame();
        assets.unloadVillage();
        assets.unloadLevel();

        assets.dispose();
    }

    public static void main(String[] args) {
        new AssetsTest();
    }
}

