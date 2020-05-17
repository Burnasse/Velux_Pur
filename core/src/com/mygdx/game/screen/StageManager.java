package com.mygdx.game.screen;

import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * The interface Stage manager.
 */
public interface StageManager {

    /**
     * Display the stageName stage.
     *
     * @param stageName the stage name
     */
    void displayStage(String stageName);

    /**
     * Start game. (unused in the GameScreen)
     */
    void startGame();

    /**
     * Initialize the screen
     * This method must be used only in the show() method
     */
    void initScreen();

    /**
     *
     */
    ScreenViewport getViewport();
}
