package com.mygdx.game.scene.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.controller.DisplayButtonHandler;
import com.mygdx.game.controller.ButtonStageController;
import com.mygdx.game.screen.StageManager;
import com.mygdx.game.scene.TextButtonContainer;

/**
 * The type Settings menu initialize all elements in the settings menu.
 */
public class SettingsMenu implements MenuStage {

    private TextButtonContainer container;
    private ButtonStageController stage;

    /**
     * Instantiates a new Settings menu.
     *
     * @param manager the manager
     */
    public SettingsMenu(final StageManager manager) {
        container = new TextButtonContainer("Controls", "Audio", "Advanced", "Back");

        container.getButtonByName("Back").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.displayStage("Main");
                return true;
            }
        });

        container.getButtonByName("Audio").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.displayStage("Audio");
                return true;
            }
        });

        container.getButtonByName("Advanced").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.displayStage("Advanced");
                return true;
            }
        });

        container.getButtonByName("Controls").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.displayStage("Controls");
                return true;
            }
        });

        TextButton[] buttons = container.getButtons().toArray();
        DisplayButtonHandler displayButtonHandler = new DisplayButtonHandler(buttons);

        stage = new ButtonStageController(new ScreenViewport(), displayButtonHandler);
        stage.setKeyboardFocus(container.getActor());
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
