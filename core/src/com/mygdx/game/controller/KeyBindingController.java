package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.PreferencesManager;

/**
 * The Key binding controller class is used when the user want to change the key binding.
 * The class wait the next keyDown for change the keycode of the selected Key in the pref File.
 */
public class KeyBindingController implements InputProcessor {

    /**
     * The PreferencesManager manager used for handle the pref File.
     */
    final PreferencesManager prefs;

    private TextButton currentButton;

    private Stage currentStage;

    private Label currentLabel;

    /**
     * Instantiates a new Key binding controller.
     *
     * @param prefs The PreferencesManager manager used for handle the pref File.
     */
    public KeyBindingController(PreferencesManager prefs) {
        this.prefs = prefs;
    }

    @Override
    public boolean keyDown(int keycode) {
        String previousKey = currentLabel.getText().toString();
        prefs.getPreferences().putInteger(previousKey, keycode).flush();

        currentButton.setText(Input.Keys.toString(keycode));
        Gdx.input.setInputProcessor(currentStage);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * Set new key in ControlsMenu.
     *
     * @param stage  the stage for set the inputProcessor after changing the key
     * @param button the button for display the new KeyName
     * @param label  the label for retrieve the name of the key to change in the pref file
     */
    public void setNewKey(Stage stage, TextButton button, Label label) {
        this.currentStage = stage;
        this.currentButton = button;
        this.currentLabel = label;

        Gdx.input.setInputProcessor(this);
    }
}
