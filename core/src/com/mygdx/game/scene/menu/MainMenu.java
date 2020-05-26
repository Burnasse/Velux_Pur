package com.mygdx.game.scene.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Assets;
import com.mygdx.game.controller.DisplayButtonHandler;
import com.mygdx.game.controller.ButtonStageController;
import com.mygdx.game.screen.MainMenuScreen;
import com.mygdx.game.screen.StageManager;
import com.mygdx.game.scene.TextButtonContainer;

/**
 * The type Main menu initialize all elements in the main menu.
 */
public class MainMenu implements MenuStage {

    private TextButtonContainer container;
    private ButtonStageController stage;

    /**
     * Instantiates a new Main menu.
     *
     * @param manager  the manager
     * @param viewport the viewport for display correctly the element on the screen
     * @param isInGame the boolean to know if the menu is display on the main menu or in the game
     */
    public MainMenu(final StageManager manager, Viewport viewport, Boolean isInGame, Assets assets) {

        if (!isInGame) {
            container = new TextButtonContainer(assets,"Play", "Multiplayer", "Settings", "Quit");

            container.getButtonByName("Play").addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    manager.changeGameState();
                    return true;
                }
            });

            container.getButtonByName("Multiplayer").addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    ((MainMenuScreen) manager).startMultiplayerGame();
                    return true;
                }
            });
        } else {
            container = new TextButtonContainer(assets,"Settings", "Quit");
        }

        container.getButtonByName("Settings").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.displayStage("Settings");
                return true;
            }
        });

        container.getButtonByName("Quit").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });

        TextButton[] buttons = container.getButtons().toArray();
        DisplayButtonHandler displayButtonHandler = new DisplayButtonHandler(buttons);

        stage = new ButtonStageController(viewport, displayButtonHandler);
        stage.setKeyboardFocus(container.getActor());
        stage.addActor(container);

        stage.act();
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    public void dispose(){
        Controllers.removeListener(stage);
        stage.dispose();
    }
}
