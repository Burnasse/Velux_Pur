package com.mygdx.game.gameGeneration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.EntityMonster;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.Entity.PlayerFactory;
import com.mygdx.game.Entity.instances.Entity;
import com.mygdx.game.Entity.instances.EntityInstance;
import com.mygdx.game.Entity.instances.EntityInstancePlayer;
import com.mygdx.game.Entity.utils.EntityPosition;
import com.mygdx.game.FloorGeneration.FloorData;
import com.mygdx.game.FloorGeneration.FloorFactory;
import com.mygdx.game.FrustumCulling;
import com.mygdx.game.IA.Gunner;
import com.mygdx.game.IA.Projectile;
import com.mygdx.game.Trigger;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.controller.PrefKeys;
import com.mygdx.game.physics.CallbackFlags;
import com.mygdx.game.physics.DynamicWorld;
import com.mygdx.game.ui.HealthBar;
import com.mygdx.game.ui.Minimap;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mygdx.game.physics.CallbackFlags.ENNEMY_FLAG;
import static com.mygdx.game.physics.CallbackFlags.TRIGGER_FLAG;

/**
 * The type Generate level.
 */
public class GenerateLevel {

    private final boolean DEBUG_MODE;
    private DebugDrawer debugDrawer;

    private Assets assets;

    private ModelBatch modelBatch;
    private Environment environment;
    private PerspectiveCamera cam;
    private FrustumCulling frustum;
    private FrustumCulling tempFrustum;

    private CameraInputController camController;
    private DynamicWorld world;
    private FloorData floorData;
    private EntityPlayer player;
    private PlayerController playerController;
    private MyContactListener contactListener;
    private AnimationController animationController;

    public ArrayMap<Integer, EntityInstancePlayer> players;

    private Minimap minimap;
    private HealthBar healthBar;
    private PointLight followLight;
    private int playerUserValue = 6666;

    private Trigger exitTrigger;

    private Stage stage;
    private Label interactLabel;

    private Array<EntityInstance> temp;
    /**
     * - toutes les Entity Instances a mettre en jeux doivent etre misent la dedans.
     * - les index des objets correpsondent a UserValue du body de l'objet
     * - pour trouvé l'entity monster correspondant au constact, il faut faire UserValue-firstEnnemyUserValue
     */
    private ArrayList<EntityInstance> instances;
    private boolean playerPov = true;
    private int clock;
    int firstEnnemyUserValue;
    private int toDelete = -1;
    private Vector3 swordPlayerPos = new Vector3();

    private volatile boolean onLoad = false;

    /**
     * The type My contact listener is called when there is a collision.
     */
    public class MyContactListener extends ContactListener {
        @Override
        public boolean onContactAdded(int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                      int index1, boolean match1) {

            if (match0) {
                /**
                 * check si la colision est faite avec un ennemy, si oui, fait le necessaire
                 * */
                if (player.isAttacking && player.cdColisionWeaponEnnemy >= 125 && ((userValue1 >= firstEnnemyUserValue && userValue1 <= firstEnnemyUserValue + floorData.entityMonsters.size()) || (userValue0 >= firstEnnemyUserValue && userValue0 <= firstEnnemyUserValue + floorData.entityMonsters.size()))) {
                    player.cdColisionWeaponEnnemy = player.cdAttack;
                    System.out.println("contact 0");
                    System.out.println("entite num :" + (userValue1 - firstEnnemyUserValue));
                    System.out.println(floorData.entityMonsters.get(userValue1 - firstEnnemyUserValue).getHealth());
                    floorData.entityMonsters.get(userValue1 - firstEnnemyUserValue).damage(player.getWeapon());
                    System.out.println(floorData.entityMonsters.get(userValue1 - firstEnnemyUserValue).getHealth());
                    if (floorData.entityMonsters.get(userValue1 - firstEnnemyUserValue).getHealth() <= 0) {
                        toDelete = userValue1 - firstEnnemyUserValue;
                    }
                }
                if (userValue1 == exitTrigger.getUserValue()) {
                    interactLabel.setVisible(true);
                }
                if (player.getCharacteristics().getHealth() > 0 && player.cdDammagesTaken == 0 && ((userValue0 >= firstEnnemyUserValue && userValue0 <= firstEnnemyUserValue + floorData.entityMonsters.size()) || (userValue1 >= firstEnnemyUserValue && userValue1 <= firstEnnemyUserValue + floorData.entityMonsters.size())) && (userValue1 == 6666 || userValue0 == 6666)) {
                    player.getsAttacked(floorData.entityMonsters.get(userValue1 - firstEnnemyUserValue).getCharacteristics().getAttackDamage());
                    player.cdDammagesTaken = 60;
                }
                System.out.println("contact0");
                System.out.println("uservalue0 = " + userValue0);
                System.out.println("uservalue1 = " + userValue1);
            }

            if (match1) {
                if (userValue0 == exitTrigger.getUserValue()) {
                    interactLabel.setVisible(true);
                }
                if (userValue1 == 3000 && player.cdDammagesTaken == 0 && player.getCharacteristics().getHealth() > 0) {
                    Gunner ennemy = (Gunner) floorData.entityMonsters.get(0).getBehavior();
                    System.out.println(ennemy.getProjectileDamage());
                    player.getsAttacked(ennemy.getProjectileDamage());
                    player.cdDammagesTaken = 60;
                }

                System.out.println("contact1");
                System.out.println("uservalue0 = " + userValue0);
                System.out.println("uservalue1 = " + userValue1);
            }
            return true;
        }

        @Override
        public void onContactEnded(int userValue0, boolean match0, int userValue1, boolean match1) {
            if (match1) {
                if (userValue0 == exitTrigger.getUserValue()) {
                    interactLabel.setVisible(false);
                }
            }
        }
    }

    public GenerateLevel(Assets assets, boolean DEBUG_MODE) {
        this.DEBUG_MODE = DEBUG_MODE;
        this.assets = assets;
    }

    /**
     * Create level with floorData as argument
     * Mainly used in multiplayer
     */
    public void create(FloorData floorData, ArrayMap<Integer, EntityInstancePlayer> players){
        this.floorData = floorData;
        this.players = players;
        create();
    }

    /**
     * Create.
     */
    public void create() {
        if (DEBUG_MODE) {
            debugDrawer = new DebugDrawer();
            world = new DynamicWorld(debugDrawer);
            debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        } else
            world = new DynamicWorld();

        contactListener = new MyContactListener();
        temp = new Array<>();
        instances = new ArrayList<>();
        DefaultShader.Config config = new DefaultShader.Config();
        config.numDirectionalLights = 2;
        config.numPointLights = 1;
        modelBatch = new ModelBatch(new DefaultShaderProvider(config));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, new Color(0.1f, 0.1f, 0.1f, 0.1f)));
        environment.add(new DirectionalLight().set(new Color(0.1f, 0.1f, 0.1f, 0.1f), 0, -10f, 0));

        followLight = new PointLight().set(new Color(1, 0.6f, 0.4f, 1f), new Vector3(0, 0, 0), 5f);
        environment.add(followLight);

        healthBar = new HealthBar();

        if(floorData == null) floorData = FloorFactory.create("Mixed", 20, 4, 3, 7, assets);

        minimap = floorData.minimap;

        player = PlayerFactory.create(floorData.playerSpawnPosition, assets);
        player.getEntity().getBody().setUserValue(playerUserValue);
        animationController = new AnimationController(player.getEntity());
        animationController.animate("idle", -1, 1.0f, null, 0.2f);
        player.getEntity().getBody().setContactCallbackFlag(CallbackFlags.PLAYER_FLAG);
        player.getEntity().getBody().setContactCallbackFilter(TRIGGER_FLAG | ENNEMY_FLAG);
        player.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);
        world.getDynamicsWorld().addCollisionObject(player.getEntity().getGhostObject(), (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter, (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        world.getDynamicsWorld().addAction(player.getEntity().getController());


        /**
         * ajoute arme dans le jeu
         */
        /*player.equipWeapon(CreatedItems.getBow());
        player.getWeapon().getEntity().transform.setToRotation(new Vector3(0,1,0),180);
        player.getWeapon().getEntity().transform.scale(0.3f,0.3f,0.3f);*/
        player.getWeapon().getEntity().getBody().setUserValue(1001);
        player.getWeapon().getEntity().getBody().setCollisionFlags(player.getWeapon().getEntity().getBody().getCollisionFlags() | 55);
        player.getWeapon().getEntity().getBody().setContactCallbackFlag(CallbackFlags.WEAPON_FLAG);
        player.getWeapon().getEntity().getBody().setContactCallbackFilter(CallbackFlags.ENNEMY_FLAG);
        player.getWeapon().getEntity().getBody().setActivationState(Collision.DISABLE_SIMULATION);
        world.addRigidBody(player.getWeapon().getEntity().getBody());

        cam = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(player.getEntity().transform.getValues()[12], 15, player.getEntity().transform.getValues()[14] - 1);
        cam.lookAt(player.getEntity().transform.getValues()[12], player.getEntity().transform.getValues()[13], player.getEntity().transform.getValues()[14] + 1);
        cam.near = 3f;
        cam.far = 300f;
        cam.update();

        initFloorObjects();

        stage = new Stage();
        interactLabel = new Label("Press F to interact", assets.manager.get(Assets.menuSkin));
        interactLabel.setVisible(false);
        interactLabel.getStyle().fontColor.set(Color.WHITE);
        interactLabel.setFontScale(1.1f);

        interactLabel.setPosition(Gdx.graphics.getWidth() / 2 - interactLabel.getWidth() / 2, interactLabel.getHeight() * 10);
        stage.addActor(interactLabel);
        stage.act();

        camController = new CameraInputController(cam);

        animationController = new AnimationController(player.getEntity());
        animationController.animate("idle", -1, 1.0f, null, 0.2f);
        playerController = new PlayerController(player, animationController, cam);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(camController);
        inputMultiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void camFollowPlayer() {
        cam.position.set(player.getEntity().transform.getValues()[12], cam.position.y, player.getEntity().transform.getValues()[14] - 1);
    }

    private void deleteDeadEntity() {
        floorData.objectsInstances.get(toDelete).getBody().setCollisionFlags(55);
        floorData.objectsInstances.get(toDelete).move(new EntityPosition(-50, -50, -50));
        toDelete = -1;
    }

    private void swordAnimation() {
        if (player.cdAttack <= 30) {
            player.cdAttack++;
            player.getWeapon().getEntity().getBody().setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            player.cdColisionWeaponEnnemy++;
        }
        if (player.cdAttack < 125 && player.cdAttack > 30) {
            player.getWeapon().getEntity().getBody().setCollisionFlags(btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            player.cdAttack++;
            player.cdColisionWeaponEnnemy++;
        }

        if (player.cdAttack == 125) {
            player.getWeapon().getEntity().getBody().setCollisionFlags(55);
            player.isAttacking = false;
        }

        if (player.cdAttack == 125) {
            swordPlayerPos = new Vector3(0.75f, -0.15f, 0.15f);
        }

        float newOrientation = player.getEntity().transform.getRotation(new Quaternion()).getAngleAround(new Vector3(0, 1, 0)) + 235;

        Vector3 direction = new Vector3();

        direction.x = -(float) Math.sin(newOrientation);
        direction.z = (float) Math.tan(newOrientation);

        Vector3 swordPos = player.getEntity().transform.getTranslation(new Vector3());

        player.getWeapon().getEntity().transform.set(swordPos, new Quaternion(new Vector3(0, 1, 0), newOrientation));


        if (player.cdAttack == 2) {
            swordPlayerPos = new Vector3(1.3f, 0.2f, 0.35f);

        }
        if (player.cdAttack > 2 && player.cdAttack < 25) {
            swordPlayerPos.x -= 0.0;
            swordPlayerPos.y += 0.0;
            swordPlayerPos.z -= 0.04;

        }

        player.getWeapon().getEntity().getBody().setWorldTransform(player.getEntity().transform);
        
    }

    /**
     * Render.
     */
    public void render() {
        if (onLoad)
            return;

        temp.clear();
        for (EntityMonster foe : floorData.entityMonsters) {
            foe.getBehavior().update(Gdx.graphics.getDeltaTime());
            if (foe.getBehavior() instanceof Gunner)
                ((Gunner) foe.getBehavior()).projectiles().removeAll(((Gunner) foe.getBehavior()).getDoneProjectiles(), true);
            for (Projectile projectile :
                    ((Gunner) foe.getBehavior()).getProjectilesShot()) {
                if (!projectile.isDone())
                    temp.add(projectile.getInstance());
            }

        }

        if (toDelete != -1) {
            deleteDeadEntity();
            toDelete = -1;
        }

        if (player.cdDammagesTaken > 0) {
            player.cdDammagesTaken--;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        animationController.update(Gdx.graphics.getDeltaTime());
        world.getDynamicsWorld().stepSimulation(Gdx.graphics.getDeltaTime(), 5, 1f / 60f);

        animationController.update(Gdx.graphics.getDeltaTime());

        swordAnimation();

        camFollowPlayer();
        cam.update();

        for (EntityMonster monster : floorData.entityMonsters)
            monster.getAnimationController().update(Gdx.graphics.getDeltaTime());

        followLight.position.x = player.getEntity().transform.getValues()[12];
        followLight.position.y = player.getEntity().transform.getValues()[13] + 1;
        followLight.position.z = player.getEntity().transform.getValues()[14];

        modelBatch.begin(cam);
        frustum.render();
        tempFrustum.render();
        modelBatch.render(exitTrigger.getEntity());
        modelBatch.render(player.getEntity(), environment);
        modelBatch.render(player.getEntityWeapon(), environment);
        if(players != null) modelBatch.render(players.values(),environment);

        modelBatch.end();

        stage.act();
        stage.draw();

        minimap.render(player.getEntity().transform.getValues()[12], player.getEntity().transform.getValues()[14]);
        healthBar.render(100, player.getCharacteristics().getHealth());

        if (DEBUG_MODE) {
            debugDrawer.begin(cam);
            world.getDynamicsWorld().debugDrawWorld();
            debugDrawer.end();
        }

        player.getEntity().getGhostObject().getWorldTransform(player.getEntity().transform);

        if (interactLabel.isVisible() && Gdx.input.isKeyPressed(PrefKeys.Interact)) {
            onLoad = true;
            interactLabel.setVisible(false);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    goToNextLevel();
                }
            });

        }
    }

    public void resize(int width, int height) {
        cam.viewportWidth = width;
        cam.viewportHeight = height;
    }

    /**
     * Dispose.
     */
    public void dispose() {
        disposeFloorObject();

        instances.clear();
        temp.clear();
        floorData.objectsInstances.clear();
        minimap.dispose();
        contactListener.dispose();
        modelBatch.dispose();
        minimap.dispose();
        healthBar.dispose();
        exitTrigger.dispose();
    }

    private void disposeFloorObject() {
        for (EntityInstance obj : floorData.objectsInstances) {
            world.getDynamicsWorld().removeRigidBody(obj.getBody());
            obj.dispose();
        }

        for (EntityInstance obj : temp) {
            world.getDynamicsWorld().removeRigidBody(obj.getBody());
            obj.dispose();
        }
        world.dispose();
    }

    public void goToNextLevel() {
        onLoad = true;

        player.getEntity().getController().setGravity(Vector3.Zero);
        disposeFloorObject();
        debugDrawer = new DebugDrawer();
        world = new DynamicWorld(debugDrawer);
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);

        world.getDynamicsWorld().addCollisionObject(player.getEntity().getGhostObject(), (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter, (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
        world.getDynamicsWorld().addAction(player.getEntity().getController());

        world.addRigidBody(player.getEntityWeapon().getBody());

        instances.clear();
        temp.clear();
        floorData.objectsInstances.clear();
        minimap.dispose();

        floorData = FloorFactory.create("Mixed", 50, 4, 3, 7, assets);
        minimap = floorData.minimap;
        minimap.clear();
        player.getEntity().transform.set(floorData.playerSpawnPosition, new Quaternion());
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);

        exitTrigger.dispose();
        initFloorObjects();

        player.getEntity().getController().setGravity(new Vector3(0, -10, 0));
        onLoad = false;
    }

    private void initFloorObjects() {
        Model exitModel = new ModelBuilder().createBox(5,7,5, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position
                | VertexAttributes.Usage.Normal);
        exitTrigger = new Trigger(exitModel, 2.5f, 4, 2.5f, floorData.exitPositon);
        exitTrigger.addInWorld(world.dynamicsWorld);

        /**
         * ajoute le sol et murs
         */
        for (EntityInstance obj : floorData.objectsInstances) {
            obj.getBody().setUserValue(instances.size());
            obj.getBody().setCollisionFlags(obj.getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            obj.getBody().setContactCallbackFlag(CallbackFlags.GROUND_FLAG);
            obj.getBody().setContactCallbackFilter(0);
            world.addRigidBody(obj.getBody());
            instances.add(obj);
        }
        firstEnnemyUserValue = instances.size();

        /**
         * ajoute les mobs
         */
        for (EntityMonster monster : floorData.entityMonsters) {
            monster.getEntity().getBody().setUserValue(instances.size());
            monster.getEntity().getBody().setCollisionFlags(monster.getEntity().getBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
            monster.getEntity().getBody().setContactCallbackFlag(CallbackFlags.ENNEMY_FLAG);
            monster.getEntity().getBody().setContactCallbackFilter(CallbackFlags.WEAPON_FLAG);
            monster.getEntity().getBody().setActivationState(Collision.DISABLE_DEACTIVATION);
            world.addRigidBody(monster.getEntity().getBody());
            world.getDynamicsWorld().addCollisionObject(monster.getEntity().getBody(), (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter, (short) btBroadphaseProxy.CollisionFilterGroups.AllFilter);
            monster.getBehavior().Surroundings(player, world);
            instances.add(monster.getEntity());

        }

        Array<EntityInstance> instances = new Array<>();
        for (EntityInstance instance : floorData.objectsInstances)
            instances.add(instance);

        frustum = new FrustumCulling(instances, environment, cam, modelBatch);
        tempFrustum = new FrustumCulling(temp, environment, cam, modelBatch);
    }

    public EntityPlayer getPlayer() {
        return player;

    }
}
