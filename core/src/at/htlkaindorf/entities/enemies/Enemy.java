package at.htlkaindorf.entities.enemies;

import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The Enemy class is the parent class of all enemies.
 * It extends the sprite class.
 * @author Trummer Nik
 * */
public abstract class Enemy extends Sprite {
    /**
     * World in which the collectable should appear.
     * */
    protected World world;
    /**
     * Playscreen on which the collectable should appear.
     * */
    protected PlayScreen screen;
    /**
     * The body of the collectable with position, texture, ... .
     * */
    protected Body body;
    /**
     * A vector which defines the movement speed of the enemy.
     * */
    protected Vector2 velocity;
    /**
     * Makes sure that the enemy does not get stuck in
     * reversing his movement.
     * */
    protected int reverseTimeout = 0;

    /**
     * Sets the position and the bounds of the enemie and calls
     * the define enemie method.
     * @param screen playscreen on which the enemie should appear
     * @param x x position of the enemie
     * @param y y position of the enemie
     * @since 1.0
     *  */
    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(-1, -2);
        body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead(Player player);
    public abstract void hitByEnemy(Enemy enemy);

    /**
     * Sets the position and the bounds of the enemie and calls
     * the define enemie method.
     * @param x if true, the movement in the x direction is reversed
     * @param y if true, the movement in the y direction is reversed
     * @since 1.0
     *  */
    public void reverseVelocity(boolean x, boolean y){
        if (reverseTimeout == 0) {
            if(x) {
                velocity.x = -velocity.x;
            }
            if(y) {
                velocity.y = -velocity.y;
            }
            reverseTimeout = 30;
        }
    }

    public World getWorld() {
        return world;
    }

    public PlayScreen getScreen() {
        return screen;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}
