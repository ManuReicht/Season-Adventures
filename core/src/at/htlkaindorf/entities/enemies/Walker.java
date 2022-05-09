package at.htlkaindorf.entities.enemies;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import java.text.DecimalFormat;

public class Walker extends Enemy{
    private float stateTime;
    private Animation<TextureRegion> walk;
    private Animation<TextureRegion> die;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    float angle;

    private double oldPosition = b2body.getPosition().x;
    private double newPosition = oldPosition + 100;
    private boolean runningLeft;


    public Walker(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();
        for(int i = 1; i < 16; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("angry_pig_walk"), i * 36, 0, 36, 30));
        walk = new Animation(0.07f, frames);

        frames.clear();
        for(int i = 1; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("angry_pig_hit_one"), i * 36, 0, 36, 30));
        die = new Animation(0.07f, frames);

        stateTime = 0;
        setBounds(getX(), getY(), 16 / Game.getInstance().getPPM(), 16 / Game.getInstance().getPPM());
        setToDestroy = false;
        destroyed = false;
        angle = 0;
        runningLeft = true;
    }

    public void update(float dt){
        stateTime += dt;
        if (reverseTimeout > 0) {
            reverseTimeout--;
        }
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(die.getKeyFrame(stateTime, true));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

            TextureRegion region = walk.getKeyFrame(stateTime, true);
            if ((b2body.getLinearVelocity().x < 0 || runningLeft) && region.isFlipX()){
                region.flip(true, false);
                runningLeft = true;
                System.out.println("Flip left");
            } else if((b2body.getLinearVelocity().x > 0 || !runningLeft) && !region.isFlipX()){
                region.flip(true, false);
                runningLeft = false;
                System.out.println("Flip right");
            }
            setRegion(region);
        }

        oldPosition = newPosition;
        newPosition = b2body.getPosition().x;

        DecimalFormat df = new DecimalFormat("#.###");
        oldPosition = Double.valueOf(df.format(oldPosition).replace(",","." ));
        newPosition = Double.valueOf(df.format(newPosition).replace(",","." ));

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
                Game.getInstance().getPLAYER_BIT());

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
