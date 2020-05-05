package com.mygdx.game.screen;

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
}
