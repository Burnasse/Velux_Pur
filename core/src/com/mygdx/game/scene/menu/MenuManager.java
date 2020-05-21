package com.mygdx.game.scene.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;

/**
 * The type Menu manager manage the different stage from the menu.
 */
public class MenuManager {

    private HashMap<String, MenuStage> map = new HashMap<>();

    /**
     * Add stage.
     *
     * @param name  the name of the stage to find him
     * @param stage the stage
     */
    public void addMenuStage(String name, MenuStage stage) {
        map.put(name, stage);
    }

    /**
     * Get stage by name stage.
     *
     * @param name the name of the stage
     * @return the stage
     */
    public MenuStage getStageByName(String name) {
        return map.get(name);
    }

    /**
     * Change stage and set the input processor and the view.
     *
     * @param stageName the stage name
     * @param viewport  the viewport
     * @return the stage
     */
    public Stage changeStage(String stageName, Viewport viewport) {
        MenuStage menuStage = getStageByName(stageName);
        Gdx.input.setInputProcessor(menuStage.getStage());
        menuStage.getStage().setViewport(viewport);

        return menuStage.getStage();
    }

    public void dispose(){
        for(MenuStage stage : map.values())
            stage.dispose();
    }

}
