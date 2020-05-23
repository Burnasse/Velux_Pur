package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FloorLayout.Position;

import java.util.ArrayList;

public class Minimap {

    static class CellMap{
        public float x;
        public float y;
        public boolean isVisibile = false;

        public CellMap(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void setVisibile(){
            isVisibile = true;
        }
    }

    private final SpriteBatch spriteBatch;
    private final Texture wallTexture;
    private final Texture playerTexture;
    private final Texture exitTexture;
    private Circle circle;

    private final float minimapSize = 150;
    private final float blockSize;
    private float realFloorSize;
    private EntityPosition exitPosition;
    private boolean exitIsVisible = false;

    ArrayList<CellMap> list = new ArrayList<>();

    public Minimap(Position[][] level, EntityPosition exitPosition) {
        this.exitPosition = exitPosition;
        spriteBatch = new SpriteBatch();
        realFloorSize = level.length * 5;
        blockSize = minimapSize / level.length;

        Pixmap pixmap = new Pixmap((int)blockSize,(int)blockSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        wallTexture = new Texture(pixmap);

        pixmap = new Pixmap((int)blockSize,(int)blockSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        playerTexture = new Texture(pixmap);

        pixmap = new Pixmap((int)blockSize,(int)blockSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        exitTexture = new Texture(pixmap);

        pixmap.dispose();

        circle = new Circle(0,0,20);

        for (int i = 0; i < level[0].length; i++) {
            for (int j = 0; j < level.length; j++) {
                if (i == 0 || j == 0 || i == level[0].length - 1 || j == level.length - 1) {
                    list.add(new CellMap(((level.length-1)-i) * blockSize, j * blockSize));
                } else if (level[i][j].getContent() == 'a') {
                    list.add(new CellMap(((level.length-1)-i) * blockSize, j * blockSize));
                }
            }
        }
    }

    public void render(float playerX, float playerZ) {

        float exitPosMapX = (Gdx.graphics.getWidth() - minimapSize-50 ) + minimapSize - ((exitPosition.x / realFloorSize) * minimapSize) - (blockSize);
        float exitPosMapY = (Gdx.graphics.getHeight() - minimapSize-50 ) + ((exitPosition.z / realFloorSize) * minimapSize);
        spriteBatch.begin();
        circle.setPosition(
                (Gdx.graphics.getWidth() - minimapSize-50 ) + minimapSize - ((playerX / realFloorSize) * minimapSize) - (blockSize/2f),
                (Gdx.graphics.getHeight() - minimapSize-50 ) + ((playerZ / realFloorSize) * minimapSize) + (blockSize/2f)
        );

        for (CellMap cell : list) {
            if(circle.contains((Gdx.graphics.getWidth() - minimapSize - 50) + cell.x,(Gdx.graphics.getHeight() - minimapSize - 50) + cell.y))
                cell.setVisibile();
            if(cell.isVisibile)
                spriteBatch.draw(wallTexture, (Gdx.graphics.getWidth() - minimapSize - 50) + cell.x, (Gdx.graphics.getHeight() - minimapSize - 50) + cell.y);
        }

        if(circle.contains(exitPosMapX, exitPosMapY))
            exitIsVisible = true;

        if(exitIsVisible)
            spriteBatch.draw(exitTexture, exitPosMapX, exitPosMapY);

        spriteBatch.draw(playerTexture,circle.x-(blockSize/2f),circle.y-(blockSize/2f));
        spriteBatch.end();
    }

    public void dispose() {
        spriteBatch.dispose();
    }

    public void clear(){
        for(CellMap cell : list)
            cell.isVisibile = false;
    }
}

