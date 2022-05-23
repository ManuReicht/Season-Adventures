package at.htlkaindorf.objects;

import at.htlkaindorf.Game;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;

public class LevelEnd extends InteractiveObject {
    private final Animation<TextureRegion> animation;

    public LevelEnd(PlayScreen screen, MapObject object, float x, float y){
        super(screen, object ,x, y);
        fixture.setUserData(this);
        setCategoryFilter(Game.getInstance().LEVEL_END_BIT);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("flag animation"), i * 60, 0, 60, 60));
        animation = new Animation(0.15f, frames);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        TextureRegion region = animation.getKeyFrame(stateTime, true);
        setRegion(region);
    }
}