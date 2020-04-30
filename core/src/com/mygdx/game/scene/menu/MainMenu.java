package com.mygdx.game.scene.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.controller.MenuController;
import com.mygdx.game.controller.ButtonStage;
import com.mygdx.game.screen.StageManager;
import com.mygdx.game.scene.TextButtonContainer;

/**
 * The type Main menu initialize all elements in the main menu.
 */
public class MainMenu implements MenuStage {

    private TextButtonContainer container;
    private ButtonStage stage;

    /**
     * Instantiates a new Main menu.
     *
     * @param manager  the manager
     * @param viewport the viewport for display correctly the element on the screen
     * @param isInGame the boolean to know if the menu is display on the main menu or in the game
     */
    public MainMenu(final StageManager manager, Viewport viewport, Boolean isInGame) {
        if (!isInGame) {
            container = new TextButtonContainer("Play", "Settings", "Quit");

            container.getButtonByName("Play").addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    manager.startGame();
                    return true;
                }
            });
        } else {
            container = new TextButtonContainer("Settings", "Quit");
        }

        container.getButtonByName("Settings").addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                manager.show("Settings");
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
        MenuController menuController = new MenuController(buttons);

        stage = new ButtonStage(viewport, menuController);
        stage.setKeyboardFocus(container.getActor());
        stage.addActor(container);

        stage.act();
    }

    @Override
    public Stage getStage() {
        return stage;
    }
}
