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

public class Player extends Sprite {
    public static final int MAX_JUMP_HEIGHT = 35;
    public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD};
	
    private State currentState;
    private State previousState;

    private Animation run;
    private Animation idle;
    private TextureRegion jump;

    private float stateTimer;
    private boolean runningRight;
    private float yBeforeJump;
    private boolean gainHeight = true;

    private World world;
    private Body b2body;

    private double oldPosition;
    private double newPosition;

    private PlayScreen screen;

    public Player(PlayScreen screen){
        //initialize default values
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

        //define player in Box2d
        definePlayer();

        //set initial values for players location, width and height. And initial frame as playerStand.
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
        oldPosition = Double.valueOf(df.format(oldPosition).replace(",","." ));
        newPosition = Double.valueOf(df.format(newPosition).replace(",","." ));

        if(onCeiling()) {
            gainHeight = false;
            newPosition += 100;
        }
    }

    public TextureRegion getFrame(float dt){
        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch(currentState){
            /*case DEAD:
                region = marioDead;
                break;*/
            /*case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;
                }
                break;*/
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

        //if mario is running left and the texture isnt facing left... flip it.
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        //if mario is running right and the texture isnt facing right... flip it.
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;

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

        fdef.filter.categoryBits = Game.getInstance().getPLAYER_BIT();
        fdef.filter.maskBits = (short) (Game.getInstance().getTERRAIN_BIT() |
                //Game.getInstance().getCOIN_BIT() |
                //Game.getInstance().getBRICK_BIT() |
                Game.getInstance().getENEMY_BIT() |
                //Game.getInstance().getOBJECT_BIT() |
                Game.getInstance().getENEMY_HEAD_BIT() |
                Game.getInstance().getLEVEL_END_BIT());
                //Game.getInstance().getITEM_BIT());

        fdef.shape = shape;
        
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
