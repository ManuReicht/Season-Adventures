package at.htlkaindorf.entities;

import at.htlkaindorf.Game;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite {
    public static final int MAX_JUMP_HEIGHT = 25;
    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD};
	
    private State currentState;
    private State previousState;

    private float yBeforeJump;
    private boolean gainHeight = true;

    private World world;
    private Body b2body;

    private PlayScreen screen;

    public Player(PlayScreen screen){
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //define player in Box2d
        definePlayer();

        //set initial values for players location, width and height. And initial frame as playerStand.
        setBounds(0, 0, 16 / Game.getInstance().getPPM(), 16 / Game.getInstance().getPPM());
    }

    public void update(float dt){

    }

    public void jump(){
        // no double jumps, only allow jumping if player is standing of running
        if(currentState == State.STANDING || currentState == State.RUNNING){
        //if ( currentState != State.JUMPING && previousState != State.JUMPING ) { //old line
            yBeforeJump = b2body.getPosition().y;
            b2body.applyLinearImpulse(new Vector2(0, 1f), b2body.getWorldCenter(), true);
        }
    }

    public void gainHeight() {
        if ((b2body.getPosition().y) < (MAX_JUMP_HEIGHT / Game.getInstance().getPPM() + yBeforeJump) && gainHeight) {
            b2body.applyLinearImpulse(new Vector2(0, 0.5f), b2body.getWorldCenter(), true);
        } else if (currentState.equals(State.STANDING)){
            gainHeight = false;
        } else {
            System.out.println("hjlkögfs ");
            gainHeight = false;
        }
    }

    public void moveRight(){
        b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
    }

    public void moveLeft(){
        b2body.applyLinearImpulse(new Vector2(-0.1f, 0), b2body.getWorldCenter(), true);
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(200 / Game.getInstance().getPPM(), 32 / Game.getInstance().getPPM());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / Game.getInstance().getPPM(), 6 / Game.getInstance().getPPM());
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Game.getInstance().getPPM(),
                        6 / Game.getInstance().getPPM()),
                new Vector2(2 / Game.getInstance().getPPM(),
                        6 / Game.getInstance().getPPM()));

        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        super.draw(batch);
    }

    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis ~ Nik
        //if player is going positive in Y-Axis he is jumping, no double jumps ~ Manu

        //if player is dead
        if(b2body.getPosition().y < 0)
            return State.DEAD;
        else if(b2body.getLinearVelocity().y > 0)
            return State.JUMPING;
            //if negative in Y-Axis player is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if player is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0) {
            gainHeight = true;
            return State.RUNNING;
            //if none of these return then he must be standing
        }
        else {
            gainHeight = true;
            return State.STANDING;
        }
    }

    public void printState(){
        System.out.println(currentState);
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
