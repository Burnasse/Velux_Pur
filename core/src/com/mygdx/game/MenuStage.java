package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuStage extends Stage {
    private MenuController controller;

    public MenuStage(MenuController controller) {
        this.controller = controller;
    }

    public MenuStage(Viewport viewport, MenuController controller) {
        super(viewport);
        this.controller = controller;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.DOWN)
            controller.downKeyPressed();
        if(keycode == Input.Keys.UP)
            controller.upKeyPressed();
        if(keycode == Input.Keys.ENTER)
            controller.enterKeyPressed();
        return true;
    }
}
