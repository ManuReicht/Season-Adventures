package at.htlkaindorf.entities;

import at.htlkaindorf.Game;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING};
    private State currentState;
    private State previousState;

    private World world;
    private Body b2body;

    private PlayScreen screen;

    public Player(PlayScreen screen){
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //define mario in Box2d
        definePlayer();

        //set initial values for marios location, width and height. And initial frame as marioStand.
        setBounds(0, 0, 16 / Game.getInstance().getPPM(), 16 / Game.getInstance().getPPM());
        //setRegion(marioStand);
    }

    public void update(float dt){

    }

    public void jump(){
        // no double jumps, only allow jumping if mario is standing of running
        if(currentState == State.STANDING || currentState == State.RUNNING){
        //if ( currentState != State.JUMPING && previousState != State.JUMPING ) { //old line
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
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
        bdef.position.set(32 / Game.getInstance().getPPM(), 32 / Game.getInstance().getPPM());
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
        //if mario is going positive in Y-Axis he is jumping, no double jumps ~ Manu
        //if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) // old line
        if(b2body.getLinearVelocity().y > 0)
            return State.JUMPING;
            //if negative in Y-Axis mario is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if mario is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
            //if none of these return then he must be standing
        else
            return State.STANDING;
    }

    public void printState(){
        System.out.println(currentState);
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
}
