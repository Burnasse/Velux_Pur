package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Assets;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.network.ClientVelux;
import com.mygdx.game.network.ConnectionState;
import com.mygdx.game.gameGeneration.GenerateMultiplayerLevel;

/**
 * The type Multiplayer game screen.
 */
public class MultiplayerGameScreen implements Screen, StageManager {

    private GenerateMultiplayerLevel multiplayer;
    private ClientVelux client;
    private VeluxPurGame manager;
    private Assets assets;

    /**
     * Instantiates a new Multiplayer game screen.
     *
     * @param manager the manager
     * @param assets the assets
     */
    public MultiplayerGameScreen(VeluxPurGame manager, Assets assets) {
        this.assets = assets;
        this.manager = manager;
        multiplayer = new GenerateMultiplayerLevel(manager,assets);
    }

    public void initScreen() {
        assets.loadLevel();
        assets.loadGame();
        assets.manager.finishLoading();
        client = new ClientVelux(multiplayer);

        synchronized (this){
            while(!client.getClient().isConnected()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notify();
            multiplayer.initLevel(client);
        }

    }

    @Override
    public ScreenViewport getViewport() {
        return  null;
    }

    @Override
    public void show() {
        initScreen();
    }

    @Override
    public void render(float delta) {
        if (client.state.equals(ConnectionState.LOST))
            manager.setScreen(new MainMenuScreen(manager, assets));
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        multiplayer.render();
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
        multiplayer.dispose();
        client.getClient().close();
    }

    @Override
    public void displayStage(String stageName) {

    }

    @Override
    public void startGame() {

    }

}
