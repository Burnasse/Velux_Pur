package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Display the Healthbar.
 */
public class HealthBar {

    private final ShapeRenderer shapeRenderer;
    private final Color backgroundColor;
    private final Color fullHealthColor;
    private final Color MidHealthColor;
    private final Color LowHealthColor;

    /**
     * Instantiates a new Health bar.
     */
    public HealthBar(){
        shapeRenderer = new ShapeRenderer();
        backgroundColor = new Color(0.8f, 0.8f, 0.8f, 1f);
        fullHealthColor = new Color(0.2f,0.7f,0.2f,0.8f);
        MidHealthColor = new Color(1f,0.6f,0f,0.8f);
        LowHealthColor = new Color(1f,0.1f,0f,0.8f);
    }

    /**
     * Render the healthBar.
     * The player's life is scale by this function : (playerLife/playerMaxLife)*HealthBarSize
     *
     * @param playerMaxLife the player max life
     * @param playerLife    the player life
     */
    public void render(float playerMaxLife, float playerLife){
        float life = (playerLife/playerMaxLife)*200;
        System.out.println(life);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(50,Gdx.graphics.getHeight()-80,210,30);
        if(life > 120)
            shapeRenderer.setColor(fullHealthColor);
        else if(life > 60)
            shapeRenderer.setColor(MidHealthColor);
        else
            shapeRenderer.setColor(LowHealthColor);
        shapeRenderer.rect(55,Gdx.graphics.getHeight()-75,life,20);
        shapeRenderer.end();
    }

    /**
     * Dispose.
     */
    public void dispose(){
        shapeRenderer.dispose();
    }
}
