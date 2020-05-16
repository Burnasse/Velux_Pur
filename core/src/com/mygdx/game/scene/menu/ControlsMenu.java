package com.mygdx.game.scene.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.game.PreferencesManager;
import com.mygdx.game.controller.KeyBindingController;
import com.mygdx.game.controller.PrefKeys;
import com.mygdx.game.screen.StageManager;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Controls menu initialize all elements in the controls menu.
 */
public class ControlsMenu implements MenuStage {

    private Stage stage;

    /**
     * Instantiates a new Controls menu.
     *
     * @param manager the manager
     */
    public ControlsMenu(final StageManager manager) {
        stage = new Stage();

        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("menuAssets/UI.atlas")));
        skin.load(Gdx.files.internal("menuAssets/UI.json"));

        Table table = createListKey(skin);

        TextButton buttonApply = new TextButton("Apply", skin);

        buttonApply.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PrefKeys.initKeys(new PreferencesManager().getPreferences());
                return true;
            }
        });

        TextButton buttonBack = new TextButton("Back", skin);

        buttonBack.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.displayStage("Settings");
                return true;
            }
        });

        table.add(buttonApply).colspan(1).space(10);
        table.add(buttonBack).colspan(2).space(10);


        Container<Table> container = new Container<>();
        container.setFillParent(true);
        container.setActor(table);

        stage.addActor(container);
        stage.act();
    }

    private Table createListKey(Skin skin) {
        PreferencesManager pref = new PreferencesManager();
        final KeyBindingController controller = new KeyBindingController(pref);

        final HashMap<String, Integer> keyMap = PrefKeys.getKeyMap();
        System.out.println(keyMap.size());

        Table table = new Table();

        for (Map.Entry<String, Integer> entry : keyMap.entrySet()) {
            String keyName = entry.getKey();
            Integer keyCode = entry.getValue();

            final Label label = new Label(keyName, skin);
            final TextButton textButton = new TextButton(Input.Keys.toString(keyCode), skin);

            textButton.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    controller.setNewKey(getStage(), textButton, label);
                    return true;
                }
            });

            table.add(label).colspan(1).space(10);
            table.add(textButton).colspan(2).space(10);
            table.row();
        }

        return table;
    }

    public Stage getStage() {
        return stage;
    }
}
