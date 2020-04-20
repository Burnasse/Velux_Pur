package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.PreferencesManager;
import com.mygdx.game.VeluxPurGame;

public class AudioScreen implements Screen {

    private Stage stage;

    public AudioScreen(final VeluxPurGame manager){
        final PreferencesManager prefs = new PreferencesManager();
        stage = new Stage(new ScreenViewport());

        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("visui/uiskin.atlas")));
        skin.load(Gdx.files.internal("visui/uiskin.json"));

        final Slider slider = new Slider(0,100,1,false, skin);
        slider.setValue(prefs.getPreferences().getInteger("volume"));

        final Label sliderLabel = new Label("",skin);
        sliderLabel.setText((int) slider.getValue());

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sliderLabel.setText((int) slider.getValue());
                prefs.getPreferences().putInteger("volume", (int) slider.getValue()).flush();
            }
        });

        TextButton backButton = new TextButton("Back", skin);

        backButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.changeScreen(new SettingsScreen(manager));
                return true;
            }
        });

        VerticalGroup group = new VerticalGroup();
        group.addActor(sliderLabel);
        group.addActor(slider);
        group.addActor(backButton);

        Container container = new Container();
        container.setActor(group);
        container.setFillParent(true);

        stage.addActor(container);
        Gdx.input.setInputProcessor(stage);
        stage.act();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
