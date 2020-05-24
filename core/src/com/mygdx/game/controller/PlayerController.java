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

import java.util.Random;

/**
 * The type Player controller.
 */
public class PlayerController implements InputProcessor, ControllerListener {

    private EntityPlayer player;
    private AnimationController animation;
    private Vector3 walkDirection = new Vector3();
    private boolean playerPov;
    private float speed = 0;

    /**
     * If the player is turned left.
     */
    private boolean lookLeft = true;

    /**
     * If the player is turned left.
     */
    private boolean lookRight = false;

    /**
     * If the player is turned left.
     */
    private boolean lookUp = false;

    /**
     * If the player is turned left.
     */
    private boolean lookDown = false;


    /**
     * Instantiates a new Player controller.
     *
     * @param player the player
     */
    public PlayerController(EntityPlayer player,AnimationController animationController) {
        this.player = player;
        this.animation = animationController;
    }
    public boolean keyDown(int keycode) {

        speed = 0;

        if (Gdx.input.isKeyPressed(PrefKeys.LEFT_ARR) || Gdx.input.isKeyPressed(PrefKeys.Left)) {
            moveLeft();
        }

        if (Gdx.input.isKeyPressed(PrefKeys.RIGHT_ARR) || Gdx.input.isKeyPressed(PrefKeys.Right)) {
            moveRight();
        }

        if ((Gdx.input.isKeyPressed(PrefKeys.UP_ARR) || Gdx.input.isKeyPressed(PrefKeys.Up))) {
            moveUp();
        }

        if ((Gdx.input.isKeyPressed(PrefKeys.DOWN_ARR) || Gdx.input.isKeyPressed(PrefKeys.Down))) {
            moveDown();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.X) && player.getEntity().getController().onGround()) {
            dodge();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.H) && player.getEntity().getController().onGround()) {
            Random random = new Random();
            dance(random.nextInt(5));
        }
        setMovement(speed);

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (!player.getEntity().getController().onGround())
            return false;
        if (!Gdx.input.isKeyPressed(PrefKeys.LEFT_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Left)
                && !Gdx.input.isKeyPressed(PrefKeys.RIGHT_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Right)
                && !Gdx.input.isKeyPressed(PrefKeys.DOWN_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Down)
                && !Gdx.input.isKeyPressed(PrefKeys.UP_ARR) && !Gdx.input.isKeyPressed(PrefKeys.Up)) {
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
        player.attack();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == PrefKeys.LeftClick)
            System.out.println("AHAHAHAHAH");
        if (button == Input.Buttons.RIGHT)
            System.out.println("OUHOUHOUHOUH");
        return true;
    }


    private void moveLeft() {
        if (lookLeft)
            lookLeft = true;
            animationRunLeft();


        if (lookRight)
            lookLeft = true;
            lookRight = false;
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);
            animationRunLeft();

        if (lookUp)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -90);
            lookLeft = true;
            lookUp = false;
            animationRunLeft();


        if (lookDown)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
            lookLeft = true;
            lookDown= false;
            animationRunLeft();


    }


    private void moveRight() {
        if (lookLeft)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);
            lookLeft = false;
            lookRight = true;
            animationRunRight();

        if (lookRight)
            lookLeft = false;
            lookRight = true;
            animationRunRight();

        if (lookUp)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -90);
            lookLeft = false;
            lookUp = false;
            lookRight = true;
            animationRunRight();


        if (lookDown)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
            lookLeft = false;
            lookDown= false;
            lookRight = true;
            animationRunRight();

    }


    private void moveUp() {
        if (lookLeft)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -90);
            lookLeft = false;
            lookUp = true;
            animationRunUp();

        if (lookRight)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
            lookLeft = false;
            lookRight = false;
            lookUp = true;
            animationRunUp();

        if (lookUp)
            lookLeft = false;
            lookUp = true;
            animationRunUp();

        if (lookDown)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 180);
            lookLeft = false;
            lookDown= false;
            lookUp = true;
            animationRunUp();

    }

    private void moveDown() {
        if (lookLeft)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), 90);
            lookLeft = false;
            lookDown= true;
            animationRunDown();

        if (lookRight)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -90);
            lookRight = false;
            lookDown=true;
            animationRunDown();

        if (lookUp)
            player.getEntity().transform.rotate(new Vector3(0, 1, 0), -180);
            lookUp = false;
            lookDown = true;
            animationRunDown();

        if (lookDown)
            lookDown= true;
            animationRunDown();
        }



    private void dodge() {
        if(lookRight){
            player.getEntity().getController().jump(new Vector3(-8, 0, 0));
            dodgeAnimate();
        }

        if(lookUp){
            player.getEntity().getController().jump(new Vector3(0, 0, 8));
            dodgeAnimate();
        }

        if(lookDown){
            player.getEntity().getController().jump(new Vector3(0, 0, -8));
            dodgeAnimate();
        }

        if(lookLeft){
            player.getEntity().getController().jump(new Vector3(8, 0, 0));
            dodgeAnimate();
        }
    }
    private void dance(int numRand){
        if(numRand == 0)animation.animate("dance", -1, 1.0f, null, 0.2f);
        else if(numRand == 1)animation.animate("chicken", -1, 1.0f, null, 0.2f);
        else if(numRand == 2)animation.animate("macarena", -1, 1.0f, null, 0.2f);
        else if(numRand == 3)animation.animate("shuffle", -1, 1.0f, null, 0.2f);
        else if(numRand == 4)animation.animate("thriller", -1, 1.0f, null, 0.2f);


    }

    private void dodgeAnimate() {
        animation.animate("dodge", 1, 1.0f, new AnimationController.AnimationListener() {
            @Override
            public void onEnd(AnimationController.AnimationDesc anim) {
                if (speed == 3)
                    animation.animate("running", -1, 1.0f, null, 0.2f);
                else
                    animation.animate("idle", -1, 1.0f, null, 0.2f);

                setMovement(speed);
            }

            @Override
            public void onLoop(AnimationController.AnimationDesc animation) {
            }
        }, 0.2f);
    }

    private void animationRunLeft(){
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(1, 0, 0);
        animation.animate("running", -1, 1.0f, null, 0.2f);
        speed = 3f;

    }

    private void animationRunRight(){
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(-1, 0, 0);
        animation.animate("running", -1, 1.0f, null, 0.2f);
        speed = 3f;

    }
    private void animationRunUp(){
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(0, 0, 1);
        animation.animate("running", -1, 1.0f, null, 0.2f);
        speed = 3f;

    }
    private void animationRunDown(){
        player.getEntity().getGhostObject().setWorldTransform(player.getEntity().transform);
        walkDirection.add(0, 0, -1);
        animation.animate("running", -1, 1.0f, null, 0.2f);
        speed = 3f;

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

}
