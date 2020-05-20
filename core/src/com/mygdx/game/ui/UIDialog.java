package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class UIDialog {

    private Dialog dialog;
    private TextButton yesButton;
    private TextButton noButton;

    public UIDialog(String title, String text){
        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("menuAssets/UI.atlas")));
        skin.load(Gdx.files.internal("menuAssets/UI.json"));

        dialog = new Dialog(title, skin);
        yesButton = new TextButton("Yes", skin);
        noButton = new TextButton("No", skin);

        yesButton.getLabel().setAlignment(Align.center);
        noButton.getLabel().setAlignment(Align.center);

        dialog.getTitleLabel().setAlignment(Align.center);
        dialog.setSize(300, 200);
        dialog.setPosition(Gdx.graphics.getWidth()/2-150, Gdx.graphics.getHeight()/2-100);

        dialog.getButtonTable().add(yesButton,noButton);
        yesButton.align(Align.left);
        noButton.align(Align.right);
        Label label = new Label(text,skin);
        label.setAlignment(Align.center);
        label.setWrap(true);
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
