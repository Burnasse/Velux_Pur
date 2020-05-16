package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.FloorLayout.Position;

public class Minimap extends Actor {

    private final Texture texture;
    private final Pixmap pixmap;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;

    private final int floorLength;
    private final int minimapSize = 200;
    private final int size;

    public Minimap(Position[][] level){
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        floorLength = level.length;
        size = minimapSize/level.length;

        pixmap = new Pixmap(2+minimapSize, 2+minimapSize, Pixmap.Format.RGBA8888);
        for (int i = 0; i < level[0].length; i++) {
            for (int j = 0; j < level.length; j++) {
                if(i == 0 || j == 0 || i == level[0].length-1 || j == level.length-1) {
                    pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
                    pixmap.fillRectangle(i*size,j*size,size,size);
                }
                else if(level[i][j].getContent() == 'a'){
                    pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
                    pixmap.fillRectangle(i*size,j*size,size,size);
                }

            }
        }
        texture = new Texture( pixmap );
    }

    public void render(float playerX, float playerZ){
        spriteBatch.begin();
        spriteBatch.draw(
                texture,
                Gdx.graphics.getWidth()-pixmap.getWidth()*1.25f,
                Gdx.graphics.getHeight()-pixmap.getHeight()*1.25f,
                pixmap.getWidth(),pixmap.getHeight(),
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                true,
                true
        );
        spriteBatch.end();

        System.out.println(playerX + " -- " + playerZ);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(
                (Gdx.graphics.getWidth()-pixmap.getWidth()*0.25f) - (playerX/floorLength)*(size*2)-(size/2),
                (Gdx.graphics.getHeight()-pixmap.getHeight()*1.25f) + (playerZ/floorLength)*size*2,
                size
        );
        shapeRenderer.end();
    }

    public void dispose(){
        texture.dispose();
        pixmap.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
