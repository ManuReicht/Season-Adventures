package at.htlkaindorf.entities.factories;

import at.htlkaindorf.entities.collectables.Coin;
import at.htlkaindorf.entities.collectables.Collectable;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.entities.enemies.Walker;
import at.htlkaindorf.screens.PlayScreen;

/**
 * The SeasonFactory interface is used to create factories for every season.
 * It contains the functions for every object of the factories.
 * @author Reicht Manuel
 * */
public interface SeasonFactory {
    public Enemy createWalker(PlayScreen screen, float x, float y);
    public Collectable createCoin(PlayScreen screen, float x, float y);
}
