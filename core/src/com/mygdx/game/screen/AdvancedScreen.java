package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.PreferencesManager;
import com.mygdx.game.VeluxPurGame;

public class AdvancedScreen implements Screen {

    private Stage stage;

    public AdvancedScreen(final VeluxPurGame manager){
        final PreferencesManager prefs = new PreferencesManager();
        stage = new Stage(new ScreenViewport());

        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("visui/uiskin.atlas")));
        skin.load(Gdx.files.internal("visui/uiskin.json"));

        CheckBox checkBox = new CheckBox("Fullscreen", skin);

        if(Gdx.graphics.isFullscreen())
            checkBox.setChecked(true);

        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(Gdx.graphics.isFullscreen())
                    prefs.setWindowed();
                else
                    prefs.setFullscreen();
            }
        });

        final SelectBox<String> selectBox = new SelectBox<>(skin);
        Array<String> list = new Array<>();
        for(Graphics.DisplayMode mode : Gdx.graphics.getDisplayModes()){
            if(!list.contains(mode.width + "x" + mode.height, false))
                list.add(mode.width + "x" + mode.height);
        }

        selectBox.setItems(list);
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

        backButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.changeScreen(new SettingsScreen(manager));
                return true;
            }
        });

        VerticalGroup group = new VerticalGroup();
        group.addActor(checkBox);
        group.addActor(selectBox);
        group.addActor(backButton);

        Container<VerticalGroup> container = new Container<>();
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
        stage.getViewport().update(width, height, true);
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
