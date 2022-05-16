package at.htlkaindorf.entities.collectables;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Collectable extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Collectable(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        toDestroy = false;
        destroyed = false;

        setPosition(x, y);
        setBounds(getX(), getY(), 16 / Game.getInstance().getPPM(), 16 / Game.getInstance().getPPM());
        defineItem();
    }

    public abstract void defineItem();
    public abstract void collect(Player mario);

    public void update(float dt){
        System.out.println("Update");
        if(toDestroy && !destroyed){
            System.out.println("Destroy");
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy(){
        toDestroy = true;
    }
}
