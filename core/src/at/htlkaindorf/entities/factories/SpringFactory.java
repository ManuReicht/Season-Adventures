package at.htlkaindorf.entities.factories;

import at.htlkaindorf.entities.collectables.Coin;
import at.htlkaindorf.entities.collectables.Collectable;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.entities.enemies.Walker;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * The SpringFactory class is used to create objects in the spring theme.
 * It implements the SeasonFactory interface.
 * @author Reicht Manuel
 * */
public class SpringFactory implements SeasonFactory {
    @Override
    public Enemy createWalker(PlayScreen screen, float x, float y) {
        Array<TextureRegion> frames;
        frames = new Array<TextureRegion>();
        for (int i = 1; i < 14; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("trunk run"), i * 44, 0, 44, 32));
        Animation<TextureRegion> walk = new Animation(0.07f, frames);

        frames.clear();
        for (int i = 1; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("trunk hit"), i * 64, 0, 64, 32));
        Animation<TextureRegion> die = new Animation(0.07f, frames);

        return new Walker(screen, x, y, walk, die);
    }

    @Override
    public Collectable createCoin(PlayScreen screen, float x, float y) {
        Animation<TextureRegion> animation;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("spring coin"), i * 13, 0, 13, 13));
        }

        animation = new Animation(0.15f, frames);
        return new Coin(screen, x,y, animation);
    }
}