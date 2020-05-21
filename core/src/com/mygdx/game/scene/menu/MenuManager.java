package com.mygdx.game.scene.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;

/**
 * The type Menu manager manage the different stage from the menu.
 */
public class MenuManager {

    private HashMap<String, Stage> map = new HashMap<>();

    /**
     * Add stage.
     *
     * @param name  the name of the stage to find him
     * @param stage the stage
     */
    public void addStage(String name, Stage stage) {
        map.put(name, stage);
    }

    /**
     * Get stage by name stage.
     *
     * @param name the name of the stage
     * @return the stage
     */
    public Stage getStageByName(String name) {
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
        Stage stage = getStageByName(stageName);
        Gdx.input.setInputProcessor(stage);
        stage.setViewport(viewport);

        return stage;
    }

    public void dispose(){
        for(Stage stage : map.values())
            stage.dispose();
    }

}
