package com.mygdx.game.gameGeneration;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.PlayerFactory;
import com.mygdx.game.Entity.instances.EntityInstancePlayer;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FloorGeneration.FloorData;
import com.mygdx.game.FloorGeneration.FloorFactory;
import com.mygdx.game.network.ClientVelux;
import com.mygdx.game.network.data.DataPlayer;
import com.mygdx.game.network.data.DataPlayerPosition;
import com.mygdx.game.VeluxPurGame;
import com.mygdx.game.network.data.DataPosition;
import com.mygdx.game.physics.CallbackFlags;
import com.mygdx.game.screen.StageManager;

import java.util.HashMap;

/**
 * The type Generate multiplayer.
 */
public class GenerateMultiplayerLevel{

    private final Assets assets;
    private VeluxPurGame manager;
    private ClientVelux client;
    private GenerateLevel level;
    private StageManager screen;
    private FloorData floorData;
    private ArrayMap<Integer, EntityInstancePlayer> players;

    /**
     * Instantiates a new Generate multiplayer.
     *
     * @param screen the screen
     */
    public GenerateMultiplayerLevel(VeluxPurGame manager, StageManager screen, Assets assets) {
        this.screen = screen;
        this.assets = assets;
    }

    /**
     * Init level.
     *
     * @param client the client
     */
    public void initLevel(ClientVelux client) {
        this.client = client;
        players = new ArrayMap<>();


        synchronized (this){
            while(client.getFloor() == null){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            notify();
        }

        System.out.println("AH");

        floorData = FloorFactory.create(client.getFloor(),assets);
        level = new GenerateLevel(screen, assets, false);
        level.create(floorData,players);

    }

    public void render(){
        level.render();
        client.getClient().sendTCP(new DataPlayerPosition(client.getClient().getID(), level.getPlayer().getEntity().transform));
    }

    public void dispose(){
        level.dispose();
    }

    /**
     * Add player.
     *
     * @param data the data
     */
    public void addPlayer(DataPlayer data) {
        EntityPlayer newPlayer = PlayerFactory.create(new EntityPosition(),assets);
        players.put(data.id,newPlayer.getEntity());
    }

    public void move(DataPlayerPosition playerPosition){
        players.get(playerPosition.id).transform.set(playerPosition.matrix4);
    }
    /**
     * Gets manager.
     *
     * @return the manager
     */
    public VeluxPurGame getManager() {
        return manager;
    }

}

