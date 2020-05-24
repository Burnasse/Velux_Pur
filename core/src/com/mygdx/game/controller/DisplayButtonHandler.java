package com.mygdx.game.controller;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import java.util.Collections;
import java.util.LinkedList;

/**
 * The type Display button manager control the actions of the inputs on the menu.
 */
public class DisplayButtonHandler {

    private LinkedList<TextButton> buttonGroup;
    private int currentButton = 0;

    /**
     * Instantiates a new Menu controller.
     *
     * @param buttons the buttons
     */
    public DisplayButtonHandler(TextButton... buttons) {
        buttonGroup = new LinkedList<>();
        Collections.addAll(buttonGroup, buttons);
        buttonGroup.get(0).setChecked(true);
    }

    public DisplayButtonHandler(LinkedList<TextButton> buttons) {
        buttonGroup = buttons;
        buttonGroup.get(0).setChecked(true);
    }


    /**
     * Increment currentButton and Select the button on new currentButton index
     */
    public void downKeyPressed() {
        if (currentButton < buttonGroup.size() - 1)
            currentButton++;
        selectButton();
    }

    /**
     * Decrement currentButton and Select the button on new currentButton index
     */
    public void upKeyPressed() {
        if (currentButton > 0)
            currentButton--;
        selectButton();
    }

    /**
     * Use the selected button
     */
    public void enterKeyPressed() {
        InputEvent event1 = new InputEvent();
        event1.setType(InputEvent.Type.touchDown);
        buttonGroup.get(currentButton).fire(event1);
        buttonGroup.get(currentButton).getClickListener().cancel();
    }

    /**
     * Select the currentButton button.
     */
    private void selectButton() {
        deselectAll();
        buttonGroup.get(currentButton).setChecked(true);
    }

    private void deselectAll() {
        for (TextButton currentButton : buttonGroup) {
            currentButton.setChecked(false);
        }
    }

    public void addButton(TextButton button){
        buttonGroup.add(button);
    }

}
