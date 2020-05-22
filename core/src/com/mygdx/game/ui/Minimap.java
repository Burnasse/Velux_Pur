package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.FloorLayout.Position;

import java.util.ArrayList;

public class Minimap {

    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;

    private final float minimapSize = 150;
    private final float blockSize;
    private float realFloorSize;

    Position[][] level;

    ArrayList<Vector3> list = new ArrayList<>();

    public Minimap(Position[][] level) {
        this.level = level;
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        realFloorSize = level.length * 5;

        blockSize = minimapSize / level.length;
        for (int i = 0; i < level[0].length; i++) {
            for (int j = 0; j < level.length; j++) {
                if (i == 0 || j == 0 || i == level[0].length - 1 || j == level.length - 1) {
                    list.add(new Vector3(((level.length-1)-i) * blockSize, j * blockSize, blockSize));
                } else if (level[i][j].getContent() == 'a') {
                    list.add(new Vector3(((level.length-1)-i) * blockSize, j * blockSize, blockSize));
                }
            }
        }
    }

    public void render(float playerX, float playerZ) {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        for (Vector3 vector3 : list)
            shapeRenderer.rect((Gdx.graphics.getWidth() - minimapSize-50 ) + vector3.x,(Gdx.graphics.getHeight() - minimapSize-50 ) +  vector3.y,vector3.z, vector3.z);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(
                (Gdx.graphics.getWidth() - minimapSize-50 ) + minimapSize - ((playerX / realFloorSize) * minimapSize) - (blockSize/2f),
                (Gdx.graphics.getHeight() - minimapSize-50 ) + ((playerZ / realFloorSize) * minimapSize) + (blockSize/2f),
                blockSize / 2f
        );
        shapeRenderer.end();
    }

    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}

