package com.mygdx.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The type Menu stage handle keyboard, mouse and gamepad for the menu.
 */
public class ButtonStageController extends Stage implements ControllerListener {
    private DisplayButtonHandler displayButtonHandler;

    /**
     * Instantiates a new Button stage controller.
     *
     * @param viewport             the viewport
     * @param displayButtonHandler control the actions of the inputs on the menu
     */
    public ButtonStageController(Viewport viewport, DisplayButtonHandler displayButtonHandler) {
        super(viewport);
        this.displayButtonHandler = displayButtonHandler;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.DOWN)
            displayButtonHandler.downKeyPressed();
        if (keycode == Input.Keys.UP)
            displayButtonHandler.upKeyPressed();
        if (keycode == Input.Keys.ENTER)
            displayButtonHandler.enterKeyPressed();
        return true;
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode == Xbox.A) {
            displayButtonHandler.enterKeyPressed();
        }

        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (axisCode == Xbox.L_STICK_VERTICAL_AXIS) {
            if (value == -1)
                displayButtonHandler.upKeyPressed();
            if (value == 1)
                displayButtonHandler.downKeyPressed();
        }

        return true;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        if (value.name().equals("south"))
            displayButtonHandler.downKeyPressed();
        if (value.name().equals("north"))
            displayButtonHandler.upKeyPressed();
        return true;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
