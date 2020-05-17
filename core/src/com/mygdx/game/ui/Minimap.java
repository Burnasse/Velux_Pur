package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.FloorLayout.Position;

public class Minimap{

    private final Texture texture;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;

    private final int floorLength;
    private final int minimapSize = 150;
    private final int blockSize;

    public Minimap(Position[][] level){
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        floorLength = level.length;
        blockSize = minimapSize/level.length;
        Pixmap pixmap = new Pixmap(2+minimapSize, 2+minimapSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));

        for (int i = 0; i < level[0].length; i++) {
            for (int j = 0; j < level.length; j++) {
                if(i == 0 || j == 0 || i == level[0].length-1 || j == level.length-1) {
                    pixmap.fillRectangle(i* blockSize,j* blockSize, blockSize, blockSize);
                }
                else if(level[i][j].getContent() == 'a'){
                    pixmap.fillRectangle(i* blockSize,j* blockSize, blockSize, blockSize);
                }
            }
        }

        texture = new Texture( pixmap );
        pixmap.dispose();
    }

    public void render(float playerX, float playerZ){
        spriteBatch.begin();
        spriteBatch.draw(
                texture,
                Gdx.graphics.getWidth()-texture.getWidth()*1.25f,
                Gdx.graphics.getHeight()-texture.getHeight()*1.25f,
                texture.getWidth(),
                texture.getHeight(),
                0,
                0,
                texture.getWidth(),
                texture.getHeight(),
                true,
                true
        );
        spriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(
                (Gdx.graphics.getWidth()-texture.getWidth()*0.25f) - (playerX/floorLength)*(blockSize *2)-(blockSize /2),
                (Gdx.graphics.getHeight()-texture.getHeight()*1.25f) + (playerZ/floorLength)*(blockSize *2)+(blockSize/2),
                blockSize/2
        );
        shapeRenderer.end();
    }

    public void dispose(){
        texture.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
