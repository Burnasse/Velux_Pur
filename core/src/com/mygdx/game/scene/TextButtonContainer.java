package com.mygdx.game.scene;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;

/**
 * The type Text button container create multiple button in the VerticalGroup.
 */
public class TextButtonContainer extends Container<VerticalGroup> {

    private VerticalGroup group = new VerticalGroup();
    private Array<TextButton> buttons = new Array<>(new TextButton[0]);

    /**
     * Instantiates a new Text button container with predefined skin.
     *
     * @param names the names of all TextButton
     */
    public TextButtonContainer(Assets assets, String... names) {
        Skin skin = assets.manager.get(Assets.menuSkin);
        skin.addRegions(assets.manager.get(Assets.menuTextureAltas));

        for (String currentName : names) {
            TextButton currentButton = new TextButton(currentName, skin);
            group.addActor(currentButton);
            buttons.add(currentButton);
        }

        group.space(10);
        setFillParent(true);

        setActor(group);
    }

    /**
     * Gets buttons.
     *
     * @return the button's list
     */
    public Array<TextButton> getButtons() {
        return buttons;
    }

    /**
     * Get button by name.
     * return null if no button is found.
     *
     * @param name the name
     * @return the text button
     */
    public TextButton getButtonByName(String name) {
        for (TextButton button : buttons.items) {
            if (button.getText().toString().equals(name))
                return button;
        }
        return null;
    }
}
