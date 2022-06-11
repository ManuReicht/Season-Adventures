package at.htlkaindorf.entities.collectables;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * The collectable class is the parent class of all collectables.
 * It extends the sprite class.
 * @author Trummer Nik
 * */
public abstract class Collectable extends Sprite {
    /**
     * Playscreen on which the collectable should appear.
     * */
    protected PlayScreen screen;
    /**
     * World in which the collectable should appear.
     * */
    protected World world;
    /**
     * Turns true if the collectable is collected and should be destroyed.
     * */
    protected boolean toDestroy;
    /**
     * Turns true if the collectable is destroyed.
     * */
    protected boolean destroyed;
    /**
     * The body of the collectable with position, texture, ... .
     * */
    protected Body body;
    /**
     * Used to set the right frame for the animaition.
     * */
    protected float stateTime;

    /**
     * Sets the position and the bounds of the collectable and calls
     * the define item method.
     * @param screen playscreen on which the collectable should appear
     * @param x x position of the collectable
     * @param y y position of the collectable
     * @since 1.0
     *  */
    public Collectable(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        toDestroy = false;
        destroyed = false;

        setPosition(x, y);
        setBounds(getX(), getY(), 8 / Game.getInstance().getPPM(), 8 / Game.getInstance().getPPM());
        defineItem();
        stateTime = 0;
    }

    public abstract void defineItem();
    public abstract void collect(Player mario);

    /**
     * Checks every frame if the collectable should be destroyed and if this is true,
     * it destroyes it.
     * @param dt delta time
     * @since 1.0
     *  */
    public void update(float dt){
        stateTime += dt;
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
            stateTime = 0;
            screen.getHud().addScore(100);
        }
    }

    /**
     * Draws the collectable if it is not destroyed.
     * @param batch The Sprite batch which contains all Sprites
     * @since 1.0
     *  */
    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    /**
     * Called if the collectable is collected.
     * @since 1.0
     *  */
    public void destroy(){
        toDestroy = true;
    }

    public Body getB2body() {
        return body;
    }
}
