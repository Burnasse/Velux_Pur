package com.mygdx.game.scene.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.PreferencesManager;
import com.mygdx.game.screen.StageManager;

/**
 * The type Audio menu initialize all elements in the audio menu.
 */
public class AudioMenu implements MenuStage {

    private Stage stage;

    /**
     * Instantiates a new Audio menu.
     *
     * @param manager the manager
     */
    public AudioMenu(final StageManager manager) {
        final PreferencesManager prefs = new PreferencesManager();
        stage = new Stage(new ScreenViewport());

        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("menuAssets/UI.atlas")));
        skin.load(Gdx.files.internal("menuAssets/UI.json"));

        final Slider slider = new Slider(0, 100, 1, false, skin);
        slider.setValue(prefs.getPreferences().getInteger("volume"));

        final Label sliderLabel = new Label("", skin);
        sliderLabel.setText((int) slider.getValue());

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sliderLabel.setText((int) slider.getValue());
                prefs.getPreferences().putInteger("volume", (int) slider.getValue()).flush();
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
        group.addActor(sliderLabel);
        group.addActor(slider);
        group.addActor(backButton);

        Container<VerticalGroup> container = new Container<>();
        container.setActor(group);
        container.setFillParent(true);

        stage.addActor(container);
        stage.act();
    }

    public Stage getStage() {
        return stage;
    }
}
