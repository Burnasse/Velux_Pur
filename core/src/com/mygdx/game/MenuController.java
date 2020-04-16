package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.Collections;
import java.util.LinkedList;

public class MenuController {

    private LinkedList<TextButton> buttonGroup = new LinkedList<>();
    private int currentButton = 0;

    public MenuController(TextButton ...buttons) {
        Collections.addAll(buttonGroup, buttons);
        buttonGroup.get(0).setChecked(true);
    }

    public void downKeyPressed(){
        if(currentButton < buttonGroup.size()-1)
            currentButton++;
        selectButton();
    }

    public void upKeyPressed(){
        if(currentButton > 0)
            currentButton--;
        selectButton();
    }

    public void enterKeyPressed(){
        InputEvent event1 = new InputEvent();
        event1.setType(InputEvent.Type.touchDown);
        buttonGroup.get(currentButton).fire(event1);
    }

    public void selectButton(){
        for(TextButton currentButton : buttonGroup)
            currentButton.setChecked(false);

        buttonGroup.get(currentButton).setChecked(true);
    }

}
