package at.htlkaindorf.entities.enemies;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

public class Walker extends Enemy{
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    float angle;

    private double oldPosition = b2body.getPosition().x;
    private double newPosition = oldPosition + 100;


    public Walker(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        //frames = new Array<TextureRegion>();
        //for(int i = 0; i < 2; i++)
          //  frames.add(new TextureRegion(screen.getAtlas().findRegion("walker0"), i * 16, 0, 16, 16));
        //walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / Game.getInstance().getPPM(), 16 / Game.getInstance().getPPM());
        setToDestroy = false;
        destroyed = false;
        angle = 0;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            //setRegion(new TextureRegion(screen.getAtlas().findRegion("walker0"), 32, 0, 16, 16));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            //setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }

        oldPosition = newPosition;
        newPosition = b2body.getPosition().x;

        if(oldPosition == newPosition) {
            reverseVelocity(true, false);
            newPosition += 100;
            //System.out.println("Reverse");
        }

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Game.getInstance().getPPM());
        fdef.filter.categoryBits = Game.getInstance().getENEMY_BIT();
        fdef.filter.maskBits = (short) (Game.getInstance().getTERRAIN_BIT() |
                //Game.getInstance().getCOIN_BIT() |
                //Game.getInstance().getBRICK_BIT() |
                Game.getInstance().getENEMY_BIT() |
                Game.getInstance().getOBJECT_BIT() |
                Game.getInstance().getMARIO_BIT());

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / Game.getInstance().getPPM());
        vertice[1] = new Vector2(5, 8).scl(1 / Game.getInstance().getPPM());
        vertice[2] = new Vector2(-3, 3).scl(1 / Game.getInstance().getPPM());
        vertice[3] = new Vector2(3, 3).scl(1 / Game.getInstance().getPPM());
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Game.getInstance().getENEMY_HEAD_BIT();
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }



    @Override
    public void hitOnHead(Player player) {
        setToDestroy = true;
        //Game.getInstance().getManager().get("audio/sounds/stomp.wav", Sound.class).play();
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        reverseVelocity(true, false);
    }
}
