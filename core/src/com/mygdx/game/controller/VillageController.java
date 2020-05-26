package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Assets;
import com.mygdx.game.Entity.EntityPlayer;
import com.mygdx.game.gameGeneration.GenerateVillage;

/**
 * The type Village controller handle keyboard and gamepad input.
 */
public class VillageController implements InputProcessor, ControllerListener {

    private EntityPlayer player;
    private AnimationController animation;
    private Assets assets;
    private Vector3 walkDirection = new Vector3();
    private float speed = 0;
    private Sound footStepSound;
    private Sound jumpSound;

    /**
     * This thread is used in script method
     */
    private Thread scriptThread;

    /**
     * If the player is turned left.
     */
    private boolean lookLeft = true;

    /**
     * If a script wait a trigger to finish.
     */
    public boolean waitTrigger = false;

    /**
     * If the player sprint
     */
    private boolean isSprint = false;

    /**
     * If the controller is enable.
     * It's used for disable the gamepad during the script.
     */
    private boolean controllerEnable = true;

    /**
     * If the player can change layout.
     * Used to move up and down when a trigger is triggered
     */
    public boolean canChangeLayout = false;

    /**
     * If the player can move up.
     * Used to specify if a trigger permit to move up or down
     */
    public boolean canChangeUp = false;

    private GenerateVillage village;

    public String currentInteractTriggerName = "";

    /**
     * The User value.
     */
    public int userValue = -1;

    /**
     * Instantiates a new Village controller.
     *
     * @param player    the player
     * @param animation the animation
     */
    public VillageController(GenerateVillage village, EntityPlayer player, AnimationController animation,Assets assets) {
        this.village = village;
        this.player = player;
        this.animation = animation;
        this.assets = assets;
        Controllers.clearListeners();
        Controllers.addListener(this);
        footStepSound = assets.manager.get(Assets.stepSound);
        jumpSound = assets.manager.get(Assets.jumpSound);
    }

    @Override
    public boolean keyDown(int keycode) {

        speed = 0;

        if (Gdx.input.isKeyPressed(PrefKeys.LEFT_ARR) || Gdx.input.isKeyPressed(PrefKeys.Left)) {
            moveLeft(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT));
            loadFootstepSound();
        }

        if (Gdx.input.isKeyPressed(PrefKeys.RIGHT_ARR) || Gdx.input.isKeyPressed(PrefKeys.Right)) {
            moveRight(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT));
            loadFootstepSound();
        }

        if ((Gdx.input.isKeyPressed(PrefKeys.UP_ARR) || Gdx.input.isKeyPressed(PrefKeys.Up)) && player.getEntity().getController().onGround()
                && canChangeLayout && canChangeUp) {
            Script_ChangeLayout(false, true, 3);
        }

        if ((Gdx.input.isKeyPressed(PrefKeys.DOWN_ARR) || Gdx.input.isKeyPressed(PrefKeys.Down)) && player.getEntity().getController().onGround()
                && canChangeLayout && !canChangeUp) {
            Script_ChangeLayout(false, false, 3);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.F) && !currentInteractTriggerName.isEmpty()){
            village.showDialog(currentInteractTriggerName);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.getEntity().getController().onGround()) {
            jump();
            footStepSound.stop();
            jumpSound.play(0.5f);
        }

        setMovement(speed);

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!player.getEntity().getController().onGround())
            return false;
        if (!Gdx.input.isKeyPressed(PrefKeys.LEFT_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Left)
                && !Gdx.input.isKeyPressed(PrefKeys.RIGHT_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Right)) {
            walkDirection.set(0, 0, 0);
            animation.animate("idle", -1, 1.0f, null, 0.2f);
            setMovement(0);
            footStepSound.stop();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (buttonCode == Xbox.A && player.getEntity().getController().onGround()) {
            jump();
        }

        if (buttonCode == Xbox.B) {
            isSprint = true;
        }

        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {

        if (buttonCode == Xbox.B)
            isSprint = false;
        return true;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (!controllerEnable)
            return false;
        if (axisCode == Xbox.L_STICK_HORIZONTAL_AXIS) {
            if (value == -1)
                moveLeft(isSprint);
            else if (value == 1)
                moveRight(isSprint);
            else
                animation.animate("idle", -1, 1.0f, null, 0.2f);
            setMovement(speed);
        }
        if (axisCode == Xbox.L_STICK_VERTICAL_AXIS && player.getEntity().getController().onGround()
                && canChangeLayout) {
            if (value == -1 && canChangeUp) {
                Script_ChangeLayout(true, true, 3);
            } else if (value == 1 && !canChangeUp)
                Script_ChangeLayout(true, false, 3);
        }

        return true;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }

    /**
     * Set movement
     */
    private void setMovement(float speed) {
        walkDirection.scl(speed * Gdx.graphics.getDeltaTime());
        player.getEntity().getController().setWalkDirection(walkDirection);
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(this);
        controllerEnable = true;
    }

    /**
     * This method is used for move up and down in the village.
     * A thread is used for wait when the player is in the right place.
     *
     * @param isGamepad if the method is call by the gamepad
     * @param moveUp    if the player move up
     * @param zTarget   the destination
     */
    private void Script_ChangeLayout(final boolean isGamepad, final boolean moveUp, final float zTarget) {
        Gdx.input.setInputProcessor(null);
        controllerEnable = false;
        if (moveUp && lookLeft) {
            player.getEntity().transform.rotate(new Vector3(0, -1, 0), 90);
            walkDirection.set(0, 0, 1);
            loadFootstepSound();
        } else if (moveUp) {
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
            walkDirection.set(0, 0, 1);
            loadFootstepSound();
        } else if (lookLeft) {
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
            walkDirection.set(0, 0, -1);
            loadFootstepSound();
        } else {
            player.getEntity().transform.rotate(new Vector3(0, -1, 0), 90);
            walkDirection.set(0, 0, -1);
            loadFootstepSound();
        }
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        player.getEntity().getController().setGravity(new Vector3());
        animation.animate("walk", -1, 1.0f, null, 0.2f);
        speed = 1.5f;

        if (isGamepad)
            setMovement(speed);
        scriptThread = new Thread(new Runnable() {
            @Override
            public void run() {
                float zPos = player.getEntity().transform.getValues()[14];

                synchronized (scriptThread) {
                    if (moveUp) {
                        while (player.getEntity().transform.getValues()[14] < zPos + zTarget) {
                            try {
                                scriptThread.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else
                        while (player.getEntity().transform.getValues()[14] > zPos - zTarget) {
                            try {
                                scriptThread.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                }
                waitTrigger = false;
                userValue = -1;

                if (moveUp && lookLeft)
                    player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
                else if (moveUp)
                    player.getEntity().transform.rotate(new Vector3(0, -1, 0), 90);
                else if (lookLeft)
                    player.getEntity().transform.rotate(new Vector3(0, -1, 0), 90);
                else
                    player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);

                player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
                player.getEntity().getController().setGravity(new Vector3(0, -10, 0));
                walkDirection.set(0, 0, 0);
                setMovement(0);
                animation.animate("idle", -1, 1.0f, null, 0.2f);
                setInputProcessor();
            }
        });
        waitTrigger = true;
        scriptThread.start();
    }

    /**
     * Notify trigger.
     */
    public void notifyTrigger() {
        synchronized (scriptThread) {
            if (scriptThread.isAlive())
                scriptThread.notify();
        }
    }

    /**
     * Sets can change layout.
     * Used by the trigger.
     *
     * @param userValue   the user value
     * @param canChangeUp the can change up
     */
    public void setCanChangeLayout(int userValue, boolean canChangeUp) {
        this.canChangeUp = canChangeUp;
        canChangeLayout = true;
        this.userValue = userValue;
    }

    private void moveLeft(boolean sprint) {
        if (!lookLeft)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);

        lookLeft = true;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }
    }

    private void moveRight(boolean sprint) {
        if (lookLeft)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);

        lookLeft = false;
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(-1, 0, 0);

        if (sprint || isSprint) {
            animation.animate("running", -1, 1.0f, null, 0.2f);
            speed = 3f;
        } else {
            animation.animate("walk", -1, 1.0f, null, 0.2f);
            speed = 1.5f;
        }
    }

    private void jump() {
        player.getEntity().getController().jump(new Vector3(0, 3, 0));
        animation.animate("jump", 1, 1.0f, new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc anim) {
                if (speed == 3)
                    animation.animate("running", -1, 1.0f, null, 0.2f);
                else if (speed == 1.5f)
                    animation.animate("walk", -1, 1.0f, null, 0.2f);
                else
                    animation.animate("idle", -1, 1.0f, null, 0.2f);
            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {
            }
        }, 0.2f);
    }
    private void loadFootstepSound(){
        long soundID = footStepSound.play(0.2f);
        footStepSound.setLooping(soundID,true);
    }

    public void setInteractTrigger(String name){
        currentInteractTriggerName = name;
    }

}



