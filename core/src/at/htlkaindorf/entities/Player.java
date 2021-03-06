package at.htlkaindorf.entities;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.text.DecimalFormat;

/**
 * The player class is used to create the player.
 * It extends the sprite class.
 * @author Trummer Nik
 * */
public class Player extends Sprite {
    /**
     * Defines the max. jump height of the player.
     * */
    public static final int MAX_JUMP_HEIGHT = 35;
    /**
     * The states of the player. These states are used to control which actions can be performed in which state.
     * */
    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD};

    /**
     * The state of the player in the current frame.
     * */
    private State currentState;
    /**
     * The state of the player the frame before.
     * */
    private State previousState;

    /**
     * The body of the player with position, texture, ... .
     * */
    private Body body;

    /**
     * Stores all frames of the running animation for the player.
     * */
    private final Animation run;
    /**
     * Stores all frames of the idle animation for the player.
     * */
    private final Animation idle;
    /**
     * Stores all frames of the jumping animation for the player.
     * */
    private final TextureRegion jump;

    /**
     * Used to set the right frame for the animation.
     * */
    private float stateTimer;
    /**
     * Turns true if the player is moving in the positive x direction.
     * */
    private boolean runningRight;
    /**
     * Stores the y position of the frame before the player started a jump.
     * It is used to make sure that the player does not jump over the max. jump height.
     * */
    private float yBeforeJump;
    /**
     * If the jump button is pressed, the player gains height.
     * Turns false if the max. jump height is reached.
     * */
    private boolean gainHeight = true;

    /**
     * The position of the player the frame before.
     * */
    private double oldPosition;
    /**
     * The position of the player in the current frame.
     * */
    private double newPosition;

    /**
     * Playscreen on which the player should appear.
     * */
    private final PlayScreen screen;
    /**
     * World in which the player should appear.
     * */
    private final World world;

    /**
     * Gets all frames for the coin animation from the spritesheet and stores
     * it in animation.
     * @param screen playscreen on which the player should appear
     * @since 1.0
     *  */
    public Player(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 12; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("player run"), i * 32, 0, 32, 32));
        }
        run = new Animation(0.07f, frames);

        frames.clear();
        for(int i = 1; i < 11; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("player idle"), i * 32, 0, 32, 32));
        }
        idle = new Animation(0.07f, frames);

        jump = new TextureRegion(screen.getAtlas().findRegion("player jump"), 0, 0, 32, 32);

        definePlayer();

        setBounds(0, 0, 16 / Game.getInstance().getPPM(), 16 / Game.getInstance().getPPM());

        oldPosition = body.getPosition().y;
        newPosition =  oldPosition + 100;
    }

    /**
     * Sets the right sprite every frame and checks if the player is onCeiling and if
     * true, the player stops gaining height.
     * @param dt delta time
     * @since 1.0
     *  */
    public void update(float dt){
        setRegion(getFrame(dt));
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

        oldPosition = newPosition;
        newPosition = body.getPosition().y;

        DecimalFormat df = new DecimalFormat("#.###");
        oldPosition = Double.parseDouble(df.format(oldPosition).replace(",","." ));
        newPosition = Double.parseDouble(df.format(newPosition).replace(",","." ));

        if(onCeiling()) {
            gainHeight = false;
            newPosition += 100;
        }
    }

    /**
     * Determines the right sprite based on the state of the player and flips it if nescessary.
     * @param dt delta time
     * @return the right TextureRegion for the animation
     * @since 1.0
     *  */
    public TextureRegion getFrame(float dt){
        TextureRegion region;

        switch(currentState){
            case JUMPING:
                region = jump;
                break;
            case FALLING:
            case RUNNING:
                region = (TextureRegion) run.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default:
                region = (TextureRegion) idle.getKeyFrame(stateTimer, true);
                break;
        }

        flipSprite(region);

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;

    }

    /**
     * Determines if the frame should be fliped and flipes it if nescessary.
     * @param region the texture which should be fliped
     * @since 1.0
     *  */
    public void flipSprite(TextureRegion region) {
        if((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        } else if((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }
    }

    /**
     * Determines if the player is allowed to jump based on the states and if true, an impulse will be
     * applied on the player.
     * @since 1.0
     *  */
    public void jump(){
        // no double jumps, only allow jumping if player is standing or running
        if(currentState == State.STANDING || currentState == State.RUNNING){
            yBeforeJump = body.getPosition().y;
            body.applyLinearImpulse(new Vector2(0, 1f), body.getWorldCenter(), true);
        }
    }

    /**
     * Applies a linear impulse to the player if the user holds the jump button and the max. jump height
     * is not reached.
     * @since 1.0
     *  */
    public void gainHeight() {
        if ((body.getPosition().y) < (MAX_JUMP_HEIGHT / Game.getInstance().getPPM() + yBeforeJump) && gainHeight) {
            body.applyLinearImpulse(new Vector2(0, 0.3f), body.getWorldCenter(), true);
        } else if (currentState.equals(State.STANDING)){
            gainHeight = false;
        } else {
            gainHeight = false;
        }
    }

    /**
     * Applies a linear impulse to the player to move it in the x direction.
     * @since 1.0
     *  */
    public void moveRight(){
        body.applyLinearImpulse(new Vector2(0.1f, 0), body.getWorldCenter(), true);
    }

    /**
     * Applies a linear impulse to the player to move it in the negative x direction.
     * @since 1.0
     *  */
    public void moveLeft(){
        body.applyLinearImpulse(new Vector2(-0.1f, 0), body.getWorldCenter(), true);
    }

    /**
     * Applies a linear impulse to the player to move it in the negative x direction.
     * @param enemy the enemy which hit the player
     * @since 1.0
     *  */
    public void hit(Enemy enemy){
        currentState = State.DEAD;
    }

    /**
     * Defines the Player:
     * Sets position for the frames and the type of the body
     * @since 1.0
     * */
    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(50 / Game.getInstance().getPPM(), 32 / Game.getInstance().getPPM());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / Game.getInstance().getPPM(), 6 / Game.getInstance().getPPM());

        fdef.filter.categoryBits = Game.getInstance().PLAYER_BIT;
        fdef.filter.maskBits = (short) (Game.getInstance().TERRAIN_BIT |
                Game.getInstance().ENEMY_BIT |
                Game.getInstance().ENEMY_HEAD_BIT |
                Game.getInstance().LEVEL_END_BIT);

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    /**
     * Checks if the player is on ceiling.
     * @return true if the player  is on ceiling
     * @since 1.0
     * */
    public boolean onCeiling() {
        if (oldPosition == newPosition && gainHeight && yBeforeJump < body.getPosition().y
                && body.getLinearVelocity().y == 0 && previousState.equals(State.JUMPING)
                && currentState.equals(State.JUMPING)) {
            return true;
        }

        return false;
    }

    /**
     * Draws the sprite of the player.
     * @param batch the sprite batch with the sprites
     * @since 1.0
     * */
    public void draw(Batch batch){
        super.draw(batch);
    }

    /**
     * Determines the current state of the player based on his movement.
     * @return the current state of the player
     * @since 1.0
     * */
    public State getState(){

        if(body.getPosition().y < 0) {
            return State.DEAD;
        }

        else if(body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        }

            //if negative in Y-Axis player is falling
        else if(body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if(body.getLinearVelocity().x != 0) {
            gainHeight = true;
            return State.RUNNING;
        } else {
            gainHeight = true;
            return State.STANDING;
        }
    }

    public void printState(){
        //System.out.println(currentState);
        //System.out.println(b2body.getPosition().y);
    }

    /**
     * Updates the current state and set the previous state.
     * @since 1.0
     * */
    public void updateCurrentState(){
        previousState = currentState;
        currentState = getState();
    }

    public State getCurrentState() {
        return currentState;
    }

    public State getPreviousState() {
        return previousState;
    }

    public World getWorld() {
        return world;
    }

    public Body getBody() {
        return body;
    }

    public PlayScreen getScreen() {
        return screen;
    }

    public boolean isGainHeight() {
        return gainHeight;
    }

    public void setGainHeight(boolean gainHeight) {
        this.gainHeight = gainHeight;
    }
}
