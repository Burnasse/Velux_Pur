package com.mygdx.game.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The type Menu stage handle keyboard, mouse and gamepad for the menu.
 */
public class ButtonStageController extends Stage implements ControllerListener {
    private DisplayButtonController displayButtonController;

    /**
     * Instantiates a new Button stage controller.
     *
     * @param viewport       the viewport
     * @param displayButtonController control the actions of the inputs on the menu
     */
    public ButtonStageController(Viewport viewport, DisplayButtonController displayButtonController) {
        super(viewport);
        //Controllers.addListener(this);
        this.displayButtonController = displayButtonController;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.DOWN)
            displayButtonController.downKeyPressed();
        if (keycode == Input.Keys.UP)
            displayButtonController.upKeyPressed();
        if (keycode == Input.Keys.ENTER)
            displayButtonController.enterKeyPressed();
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
            displayButtonController.enterKeyPressed();
            System.out.println("helllo");
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
                displayButtonController.upKeyPressed();
            if (value == 1)
                displayButtonController.downKeyPressed();
        }

        return true;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        if (value.name().equals("south"))
            displayButtonController.downKeyPressed();
        if (value.name().equals("north"))
            displayButtonController.upKeyPressed();
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
