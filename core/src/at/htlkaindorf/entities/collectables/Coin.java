package at.htlkaindorf.entities.collectables;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;

/**
 * The coin class is used to create collectable coins for the player.
 * It extends the collectable class.
 * @author Trummer Nik
 * */
public class Coin extends Collectable{
    /**
     * Stores all frames of the idle animation for the coin.
     * */
    private final Animation<TextureRegion> animation;

    /**
     * Gets all frames for the coin animation from the spritesheet and stores
     * it in animation.
     * @param screen playscreen on which the coin should appear
     * @param x x position of the coin
     * @param y y position of the coin
     * @since 1.0
     *  */
    public Coin(PlayScreen screen, float x, float y, Animation animation) {
        super(screen, x, y);
        this.animation = animation;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("coin"), i * 13, 0, 13, 13));
        }

        animation = new Animation(0.15f, frames);
    }

    /**
     * Defines the Item:
     * Sets position for the frames and the type of the body
     * @since 1.0
     * */
    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + 4 / Game.getInstance().getPPM(), getY() + 4 / Game.getInstance().getPPM());
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);
    }

    /**
     * Calls the destroy function if the player collects the coin.
     * @param player The player who collected the coin.
     * @since 1.0
     *  */
    @Override
    public void collect(Player player) {
        destroy();
    }

    /**
     * Calls the update method of the parrent class and
     * sets the right animation frame.
     * @param dt delta time
     * @since 1.0
     *  */
    @Override
    public void update(float dt) {
        super.update(dt);
        if (!destroyed) {
            TextureRegion region = animation.getKeyFrame(stateTime, true);
            setRegion(region);
        }
    }
}
