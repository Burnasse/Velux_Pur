package com.mygdx.game.classesatrier.FloorLayout.RoomTypes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.game.classesatrier.Entity.EntityPlayer;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnRoom extends Room {
    EntityPlayer player;

    public SpawnRoom(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);

        int XPosition = ThreadLocalRandom.current().nextInt(x1, x2);
        int ZPosition =ThreadLocalRandom.current().nextInt(y1, y2); //Z position because z is the depth in libgdx

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "box";
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.BLUE)))
                .box(0.3f, 0.3f, 0.3f);
        Model model = modelBuilder.end();

        Bullet.init();
        btBoxShape btBoxShape = new btBoxShape(new Vector3(0.3f,0.3f,0.3f));

        player = new EntityPlayer("Velux", model, btBoxShape, XPosition, 1,ZPosition );
    }

    public SpawnRoom(int x1, int y1, int x2, int y2, EntityPlayer player){
        super(x1, y1, x2, y2);
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
