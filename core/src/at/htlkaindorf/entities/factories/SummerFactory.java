package at.htlkaindorf.entities.factories;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.entities.enemies.Walker;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class SummerFactory implements SeasonFactory {
    @Override
    public Enemy createWalker(PlayScreen screen, float x, float y) {
        Array<TextureRegion> frames;
        frames = new Array<TextureRegion>();
        for (int i = 1; i < 16; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("angry_pig_walk"), i * 36, 0, 36, 30));
        Animation<TextureRegion> walk = new Animation(0.07f, frames);

        frames.clear();
        for (int i = 1; i < 5; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("angry_pig_hit_one"), i * 36, 0, 36, 30));
        Animation<TextureRegion> die = new Animation(0.07f, frames);

        return new Walker(screen, x, y, walk, die);
    }
}
