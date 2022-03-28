package at.htlkaindorf.entities;

import at.htlkaindorf.Game;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        defineMario();

        //set initial values for marios location, width and height. And initial frame as marioStand.
        setBounds(0, 0, 16 / Game.getInstance().getPPM(), 16 / Game.getInstance().getPPM());
        //setRegion(marioStand);
    }

    public void update(float dt){

    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Game.getInstance().getPPM(), 32 / Game.getInstance().getPPM());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Game.getInstance().getPPM());

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
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
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
