package at.htlkaindorf.entities.collectables;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;

public class Coin extends Collectable{
    private final Animation<TextureRegion> animation;

    public Coin(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("coin"), i * 13, 0, 13, 13));
        }

        animation = new Animation(0.15f, frames);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX() + 4 / Game.getInstance().getPPM(), getY() + 4 / Game.getInstance().getPPM());
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);
    }

    @Override
    public void collect(Player mario) {
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (!destroyed) {
            TextureRegion region = animation.getKeyFrame(stateTime, true);
            setRegion(region);
        }
    }
}
