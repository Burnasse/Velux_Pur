package com.mygdx.game.scene.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Assets;
import com.mygdx.game.PreferencesManager;
import com.mygdx.game.screen.StageManager;

/**
 * The type Advanced menu initialize all elements in the advanced menu.
 */
public class AdvancedMenu implements MenuStage {

    private Stage stage;

    /**
     * Instantiates a new Advanced menu.
     *
     * @param manager the manager
     */
    public AdvancedMenu(final StageManager manager, Assets assets) {
        final PreferencesManager prefs = new PreferencesManager();

        stage = new Stage(new ScreenViewport());

        Skin skin = assets.manager.get(Assets.menuSkin);
        skin.addRegions(assets.manager.get(Assets.menuTextureAltas));

        CheckBox checkBox = new CheckBox(" Fullscreen", skin);

        if (prefs.getPreferences().getBoolean("isFullscreen"))
            checkBox.setChecked(true);

        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (Gdx.graphics.isFullscreen())
                    prefs.setWindowed();
                else
                    prefs.setFullscreen();
            }
        });

        final SelectBox<String> selectBox = new SelectBox<>(skin);
        Array<String> list = new Array<>();
        for (Graphics.DisplayMode mode : Gdx.graphics.getDisplayModes()) {
            if (!list.contains(mode.width + "x" + mode.height, false))
                list.add(mode.width + "x" + mode.height);
        }

        selectBox.setItems(list);
        selectBox.setAlignment(Align.center);
        String select = prefs.getPreferences().getInteger("width") + "x" + prefs.getPreferences().getInteger("height");
        selectBox.setSelected(select);

        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String[] split = selectBox.getSelected().split("x");
                prefs.setResolution(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }
        });

        TextButton backButton = new TextButton("Back", skin);

        backButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.displayStage("Settings");
                return true;
            }
        });

        VerticalGroup group = new VerticalGroup();
        group.addActor(checkBox);
        group.addActor(selectBox);
        group.addActor(backButton);
        group.space(10);

        Container<VerticalGroup> container = new Container<>();
        container.setActor(group);
        container.setFillParent(true);

        stage.addActor(container);
        stage.act();
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
