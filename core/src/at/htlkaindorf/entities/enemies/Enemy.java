package at.htlkaindorf.entities.enemies;

import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    protected Body b2body;
    protected Vector2 velocity;

    protected int reverseTimeout = 0;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(-1, -2);
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead(Player player);
    public abstract void hitByEnemy(Enemy enemy);

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

    public Body getB2body() {
        return b2body;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
}
