package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.VeluxPurGame;

public class DesktopLauncher {
    public static void main(String[] arg) {

        System.setProperty("user.name","Jeremy");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.samples = 8; // MSAA x8
        config.resizable = false;
        new LwjglApplication(new VeluxPurGame(), config);
    }
}
