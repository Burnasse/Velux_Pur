package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Assets;

public class UIDialog {

    private Dialog dialog;
    private TextButton yesButton;
    private TextButton noButton;

    public UIDialog(String title, String text, Assets assets){
        Skin skin = assets.manager.get(Assets.menuSkin);
        skin.addRegions(assets.manager.get(Assets.menuTextureAltas));

        dialog = new Dialog(title, skin);
        yesButton = new TextButton("Yes", skin);
        noButton = new TextButton("No", skin);

        yesButton.getLabel().setAlignment(Align.center);
        noButton.getLabel().setAlignment(Align.center);

        dialog.getTitleLabel().setAlignment(Align.center);
        dialog.setSize(300, 200);
        dialog.setPosition(Gdx.graphics.getWidth()/2-150, Gdx.graphics.getHeight()/2-100);

        dialog.getButtonTable().add(yesButton,noButton);
        Label label = new Label(text,skin);
        label.setAlignment(Align.center);
        label.setWrap(true);
        label.getStyle().fontColor.set(Color.WHITE);
        dialog.text(label);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public TextButton getYesButton() {
        return yesButton;
    }

    public TextButton getNoButton() {
        return noButton;
    }
}
