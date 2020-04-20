package com.mygdx.game.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;

public class TextButtonContainer extends Container<VerticalGroup> {

    private VerticalGroup group = new VerticalGroup();
    private Array<TextButton> buttons = new Array<>(new TextButton[0]);

    public TextButtonContainer(String ...names){
        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("visui/uiskin.atlas")));
        skin.load(Gdx.files.internal("visui/uiskin.json"));

        for(String currentName : names){
            TextButton currentButton = new TextButton(currentName, skin);
            group.addActor(currentButton);
            buttons.add(currentButton);
        }

        group.space(10);
        setFillParent(true);

        setActor(group);
    }

    public Array<TextButton> getButtons() {
        return buttons;
    }

    public TextButton getButtonByName(String name){
        for(TextButton button : buttons.items){
            if(button.getText().toString().equals(name))
                return button;
        }
        return null;
    }
}
