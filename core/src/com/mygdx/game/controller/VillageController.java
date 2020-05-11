package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Entity.EntityPlayer;

public class VillageController implements InputProcessor, ControllerListener {

    private EntityPlayer player;
    private AnimationController animation;
    private Vector3 walkDirection = new Vector3();
    private boolean lookLeft = true;
    private float speed = 0;

    private Thread thread;
    public boolean waitTrigger = false;

    public boolean canChangeLayout = false;
    public boolean canChangeUp = false;
    public int userValue = -1;

    public VillageController(EntityPlayer player, AnimationController animation) {
        this.player = player;
        this.animation = animation;
    }

    @Override
    public boolean keyDown(int keycode) {

        speed = 0;

        if (Gdx.input.isKeyPressed(PrefKeys.LEFT_ARR) || Gdx.input.isKeyPressed(PrefKeys.Left)) {
            if (!lookLeft)
                player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);

            lookLeft = true;
            player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
            walkDirection.add(1, 0, 0);

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                animation.animate("running", -1, 1.0f, null, 0.2f);
                speed = 3f;
            } else {
                animation.animate("walk", -1, 1.0f, null, 0.2f);
                speed = 1.5f;
            }
        }

        if (Gdx.input.isKeyPressed(PrefKeys.RIGHT_ARR) || Gdx.input.isKeyPressed(PrefKeys.Right)) {
            if (lookLeft)
                player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);

            lookLeft = false;
            player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
            walkDirection.add(-1, 0, 0);

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                animation.animate("running", -1, 1.0f, null, 0.2f);
                speed = 3f;
            } else {
                animation.animate("walk", -1, 1.0f, null, 0.2f);
                speed = 1.5f;
            }

        }

        if ((Gdx.input.isKeyPressed(PrefKeys.UP_ARR) || Gdx.input.isKeyPressed(PrefKeys.Up)) && player.getEntity().getController().onGround()
                && canChangeLayout && canChangeUp) {
            Script_ChangeLayout(true, 3);
        }

        if ((Gdx.input.isKeyPressed(PrefKeys.DOWN_ARR) || Gdx.input.isKeyPressed(PrefKeys.Down)) && player.getEntity().getController().onGround()
                && canChangeLayout && !canChangeUp) {
            Script_ChangeLayout(false, 3);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && player.getEntity().getController().onGround()) {
            player.getEntity().getController().jump(new Vector3(0, 15, 0));
            animation.animate("jump", 1, 1.0f, new AnimationController.AnimationListener() {
                @Override
                public void onEnd(AnimationController.AnimationDesc anim) {
                    if (speed == 30)
                        animation.animate("running", -1, 1.0f, null, 0.2f);
                    else
                        animation.animate("walk", -1, 1.0f, null, 0.2f);
                }

                @Override
                public void onLoop(AnimationController.AnimationDesc animation) {
                }
            }, 0.2f);
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
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
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
    }

    private void Script_ChangeLayout(final boolean moveUp, final float zTarget) {
        Gdx.input.setInputProcessor(null);

        if (moveUp && lookLeft) {
            player.getEntity().transform.rotate(new Vector3(0, -1, 0), 90);
            walkDirection.set(0, 0, 1);
        }

        if (moveUp && !lookLeft) {
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
            walkDirection.set(0, 0, 1);
        }

        if (!moveUp && lookLeft) {
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
            walkDirection.set(0, 0, -1);
        }

        if (!moveUp && !lookLeft) {
            player.getEntity().transform.rotate(new Vector3(0, -1, 0), 90);
            walkDirection.set(0, 0, -1);
        }
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        player.getEntity().getController().setGravity(new Vector3());
        animation.animate("walk", -1, 1.0f, null, 0.2f);
        speed = 1.5f;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                float zPos = player.getEntity().transform.getValues()[14];

                synchronized (thread){
                    if (moveUp){
                        while (player.getEntity().transform.getValues()[14] < zPos + zTarget){
                            try {
                                thread.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                        while (player.getEntity().transform.getValues()[14] > zPos - zTarget){
                            try {
                                thread.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                }
                waitTrigger = false;
                userValue = -1;

                if (moveUp && lookLeft)
                    player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
                if (moveUp && !lookLeft)
                    player.getEntity().transform.rotate(new Vector3(0, -1, 0), 90);
                if (!moveUp && lookLeft)
                    player.getEntity().transform.rotate(new Vector3(0, -1, 0), 90);
                if (!moveUp && !lookLeft)
                    player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);

                player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
                player.getEntity().getController().setGravity(new Vector3(0, -70, 0));
                walkDirection.set(0, 0, 0);
                setMovement(0);
                animation.animate("idle", -1, 1.0f, null, 0.2f);
                setInputProcessor();
            }
        });
        waitTrigger = true;
        thread.start();
    }

    public void notifyTrigger(){
        synchronized (thread){
            if(thread.isAlive())
                thread.notify();
        }
    }

    public void setCanChangeLayout(int userValue, boolean canChangeUp){
        this.canChangeUp = canChangeUp;
        canChangeLayout = true;
        this.userValue = userValue;
    }


}



