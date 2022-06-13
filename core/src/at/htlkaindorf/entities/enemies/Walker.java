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

import java.text.DecimalFormat;

/**
 * The walker class is used to create walking enemies in a level.
 * It extends the enemy class.
 * @author Trummer Nik
 * */
public class Walker extends Enemy {
    /**
     * Used to set the right frame for the animaition.
     * */
    private float stateTime;
    /**
     * Stores all frames of the walking animation for the walker.
     * */
    private final Animation<TextureRegion> walk;
    /**
     * Stores all frames of the die animation for the walker.
     * */
    private final Animation<TextureRegion> die;
    /**
     * Turns true if the collectable is collected and should be destroyed.
     * */
    private boolean setToDestroy;
    /**
     * Turns true if the collectable is defeated.
     * */
    private boolean destroyed;

    /**
     * The Position of the walker in the frame before the current frame.
     * */
    private double oldPosition = body.getPosition().x;
    /**
     * The Position of the walker in the current frame.
     * */
    private double newPosition = oldPosition + 100;
    /**
     * Turns true if the walker is moving in the negative x direction.
     * */
    private boolean runningLeft;


    /**
     * Sets the bounds of the walker.
     * @param screen playscreen on which the walker should appear
     * @param x x position of the coin
     * @param y y position of the coin
     * @param walk frames of the walking animation
     * @param die frames of the die animation
     * @since 1.0
     *  */
    public Walker(PlayScreen screen, float x, float y, Animation walk, Animation die) {
        super(screen, x, y);

        this.walk = walk;
        this.die = walk;

        stateTime = 0;
        setBounds(getX(), getY(), 16 / Game.getInstance().getPPM(), 16 / Game.getInstance().getPPM());
        setToDestroy = false;
        destroyed = false;
        runningLeft = true;
    }

    /**
     * The update method of the walker is called every frame.
     * It does collision detection and animates the walker.
     * @param dt delta time
     * @since 1.0
     *  */
    public void update(float dt) {
        stateTime += dt;

        if (reverseTimeout > 0) {
            reverseTimeout--;
        }

        if (setToDestroy && !destroyed) {
            destroy();
        } else if (!destroyed) {
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);

            setAnimationFrame();
        }

        oldPosition = newPosition;
        newPosition = body.getPosition().x;

        DecimalFormat df = new DecimalFormat("#.###");
        oldPosition = Double.parseDouble(df.format(oldPosition).replace(",", "."));
        newPosition = Double.parseDouble(df.format(newPosition).replace(",", "."));

        if (oldPosition == newPosition) {
            reverseVelocity(true, false);
            newPosition += 100;
        }

    }

    /**
     * Sets the right frame of the walking animation.
     * It also flips the frame if the walking direction changes.
     * @since 1.0
     *  */
    public void setAnimationFrame() {
        TextureRegion region = walk.getKeyFrame(stateTime, true);
        if ((body.getLinearVelocity().x < 0 || runningLeft) && region.isFlipX()) {
            region.flip(true, false);
            runningLeft = true;
        } else if ((body.getLinearVelocity().x > 0 || !runningLeft) && !region.isFlipX()) {
            region.flip(true, false);
            runningLeft = false;
        }
        setRegion(region);
    }

    /**
     * Called if the walker is dead.
     * @since 1.0
     *  */
    public void destroy() {
        world.destroyBody(body);
        destroyed = true;
        setRegion(die.getKeyFrame(stateTime, true));
        stateTime = 0;
    }

    /**
     * Defines the Walker:
     * Sets position for the frames and the type of the body.
     * Creates the hitbox and the head area of the walker.
     * @since 1.0
     * */
    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Game.getInstance().getPPM());
        fdef.filter.categoryBits = Game.getInstance().ENEMY_BIT;
        fdef.filter.maskBits = (short) (Game.getInstance().TERRAIN_BIT |
                Game.getInstance().ENEMY_BIT |
                Game.getInstance().PLAYER_BIT);

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        //Head
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / Game.getInstance().getPPM());
        vertice[1] = new Vector2(5, 8).scl(1 / Game.getInstance().getPPM());
        vertice[2] = new Vector2(-3, 3).scl(1 / Game.getInstance().getPPM());
        vertice[3] = new Vector2(3, 3).scl(1 / Game.getInstance().getPPM());
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = Game.getInstance().ENEMY_HEAD_BIT;
        body.createFixture(fdef).setUserData(this);

    }

    /**
     * Draws the walker if it is not destroyed.
     * @param batch The Sprite batch which contains all Sprites
     * @since 1.0
     *  */
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 1) {
            super.draw(batch);
        }
    }


    /**
     * Called if the player hits the head of the walker.
     * Turns setToDestroy true.
     * @param player the player which hit the enemy
     * @since 1.0
     *  */
    @Override
    public void hitOnHead(Player player) {
        setToDestroy = true;
    }

    /**
     * Called if the walker hits another enemy.
     * It calles the reverseVelocity function.
     * @param enemy the enemy which collided with the walker
     * @since 1.0
     *  */
    @Override
    public void hitByEnemy(Enemy enemy) {
        reverseVelocity(true, false);
    }
}
