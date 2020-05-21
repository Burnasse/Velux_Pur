package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HealthBar {

    private final ShapeRenderer shapeRenderer;
    private final Color backgroundColor;
    private final Color fullHealthColor;

    public HealthBar(){
        shapeRenderer = new ShapeRenderer();
        backgroundColor = new Color(0.8f, 0.8f, 0.8f, 1f);
        fullHealthColor = new Color(0.2f,0.7f,0.2f,0.8f);
    }

    public void render(int playerMaxLife, int playerLife){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(50,Gdx.graphics.getHeight()-80,210,30);
        shapeRenderer.setColor(fullHealthColor);
        shapeRenderer.rect(55,Gdx.graphics.getHeight()-75,(playerLife/playerMaxLife)*200,20);
        shapeRenderer.end();
    }

    public void dispose(){
        shapeRenderer.dispose();
    }
}
