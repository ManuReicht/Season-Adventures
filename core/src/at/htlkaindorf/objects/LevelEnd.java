package at.htlkaindorf.objects;

import at.htlkaindorf.Game;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;

/**
 * The LevelEnd class is used to create an object at the end of the level for the player in order
 * to finish it.
 * It extends the InteractiveObject class.
 * @author Trummer Nik
 * */
public class LevelEnd extends InteractiveObject {
    /**
     * Stores all frames of the idle animation for the flag.
     * */
    private final Animation<TextureRegion> animation;

    /**
     * Gets all frames for the level end animation from the spritesheet and stores
     * it in animation. It also sets the right filterBit of the flag.
     * @param screen playscreen on which the coin should appear
     * @param object the MapObject of the flag
     * @param x x position of the coin
     * @param y y position of the coin
     * @since 1.0
     *  */
    public LevelEnd(PlayScreen screen, MapObject object, float x, float y){
        super(screen, object ,x, y);
        fixture.setUserData(this);
        setCategoryFilter(Game.getInstance().LEVEL_END_BIT);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("flag animation"), i * 60, 0, 60, 60));
        animation = new Animation(0.15f, frames);
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
        TextureRegion region = animation.getKeyFrame(stateTime, true);
        setRegion(region);
    }
}