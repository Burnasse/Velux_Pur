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
    void show(String stageName);

    /**
     * Start game. (unused in the GameScreen)
     */
    void startGame();
}
