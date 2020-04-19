package com.mygdx.game.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.controller.MenuController;

/**
 * The type Menu stage handle keyboard, mouse and gamepad for the menu.
 */
public class MenuStage extends Stage implements ControllerListener {
    private MenuController menuController;

    /**
     * Instantiates a new Menu stage.
     *
     * @param menuController control the actions of the inputs on the menu
     */
    public MenuStage(MenuController menuController) {
        this.menuController = menuController;
    }

    /**
     * Instantiates a new Menu stage.
     *
     * @param viewport       the viewport
     * @param menuController control the actions of the inputs on the menu
     */
    public MenuStage(Viewport viewport, MenuController menuController) {
        super(viewport);
        Controllers.addListener(this);
        this.menuController = menuController;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.DOWN)
            menuController.downKeyPressed();
        if(keycode == Input.Keys.UP)
            menuController.upKeyPressed();
        if(keycode == Input.Keys.ENTER)
            menuController.enterKeyPressed();
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
        if(buttonCode == Xbox.A)
            menuController.enterKeyPressed();

        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if(axisCode == Xbox.L_STICK_VERTICAL_AXIS){
            if(value == -1)
                menuController.upKeyPressed();
            if(value == 1)
                menuController.downKeyPressed();
        }

        return true;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        if(value.name().equals("south"))
            menuController.downKeyPressed();
        if(value.name().equals("north"))
            menuController.upKeyPressed();
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
