package at.htlkaindorf.entities.collectables;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Collectable extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;
    protected float stateTime;

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

    public void update(float dt){
        stateTime += dt;
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
            stateTime = 0;
            screen.getHud().addScore(100);
        }
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy(){
        toDestroy = true;
    }

    public Body getB2body() {
        return body;
    }
}
