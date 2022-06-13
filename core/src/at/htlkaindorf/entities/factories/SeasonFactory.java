package at.htlkaindorf.entities.factories;

import at.htlkaindorf.entities.collectables.Coin;
import at.htlkaindorf.entities.collectables.Collectable;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.entities.enemies.Walker;
import at.htlkaindorf.screens.PlayScreen;

public interface SeasonFactory {
    public Enemy createWalker(PlayScreen screen, float x, float y);

    public Collectable createCoin(PlayScreen screen, float x, float y);
}
