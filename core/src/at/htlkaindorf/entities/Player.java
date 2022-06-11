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
     * Defines the max. jump height of the player.
     * */
    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD};
	
    private State currentState;
    private State previousState;

    private Body b2body;

    private final Animation run;
    private final Animation idle;
    private final TextureRegion jump;

    private float stateTimer;
    private boolean runningRight;
    private float yBeforeJump;
    private boolean gainHeight = true;

    private double oldPosition;
    private double newPosition;

    private final PlayScreen screen;
    private final World world;

    public Player(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 12; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("player_run"), i * 32, 0, 32, 32));
        }
        run = new Animation(0.07f, frames);

        frames.clear();
        for(int i = 1; i < 11; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("player_idle"), i * 32, 0, 32, 32));
        }
        idle = new Animation(0.07f, frames);

        jump = new TextureRegion(screen.getAtlas().findRegion("player_jump"), 0, 0, 32, 32);

        definePlayer();

        setBounds(0, 0, 16 / Game.getInstance().getPPM(), 16 / Game.getInstance().getPPM());

        oldPosition = b2body.getPosition().y;
        newPosition =  oldPosition + 100;
    }

    public void update(float dt){
        setRegion(getFrame(dt));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

        oldPosition = newPosition;
        newPosition = b2body.getPosition().y;

        DecimalFormat df = new DecimalFormat("#.###");
        oldPosition = Double.parseDouble(df.format(oldPosition).replace(",","." ));
        newPosition = Double.parseDouble(df.format(newPosition).replace(",","." ));

        if(onCeiling()) {
            gainHeight = false;
            newPosition += 100;
        }
    }

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

    public void flipSprite(TextureRegion region) {
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        } else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }
    }

    public void jump(){
        // no double jumps, only allow jumping if player is standing or running
        if(currentState == State.STANDING || currentState == State.RUNNING){
            yBeforeJump = b2body.getPosition().y;
            b2body.applyLinearImpulse(new Vector2(0, 1f), b2body.getWorldCenter(), true);
        }
    }

    public void gainHeight() {
        if ((b2body.getPosition().y) < (MAX_JUMP_HEIGHT / Game.getInstance().getPPM() + yBeforeJump) && gainHeight) {
            b2body.applyLinearImpulse(new Vector2(0, 0.3f), b2body.getWorldCenter(), true);
        } else if (currentState.equals(State.STANDING)){
            gainHeight = false;
        } else {
            gainHeight = false;
        }
    }

    public void moveRight(){
        b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
    }

    public void moveLeft(){
        b2body.applyLinearImpulse(new Vector2(-0.1f, 0), b2body.getWorldCenter(), true);
    }

    public void hit(Enemy enemy){
        currentState = State.DEAD;
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(50 / Game.getInstance().getPPM(), 32 / Game.getInstance().getPPM());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / Game.getInstance().getPPM(), 6 / Game.getInstance().getPPM());

        fdef.filter.categoryBits = Game.getInstance().PLAYER_BIT;
        fdef.filter.maskBits = (short) (Game.getInstance().TERRAIN_BIT |
                Game.getInstance().ENEMY_BIT |
                Game.getInstance().ENEMY_HEAD_BIT |
                Game.getInstance().LEVEL_END_BIT);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public boolean onCeiling() {
        if (oldPosition == newPosition && gainHeight && yBeforeJump < b2body.getPosition().y
                && b2body.getLinearVelocity().y == 0 && previousState.equals(State.JUMPING)
                && currentState.equals(State.JUMPING)) {
            return true;
        }

        return false;
    }

    public void draw(Batch batch){
        super.draw(batch);
    }

    public State getState(){

        if(b2body.getPosition().y < 0) {
            return State.DEAD;
        }

        else if(b2body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        }

            //if negative in Y-Axis player is falling
        else if(b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if(b2body.getLinearVelocity().x != 0) {
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

    public Body getB2body() {
        return b2body;
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
