package at.htlkaindorf.entities.factories;

import at.htlkaindorf.entities.collectables.Collectable;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.screens.PlayScreen;

/**
 * The SeasonFactory interface is used to create factories for every season.
 * It contains the functions for every object of the factories.
 * @author Reicht Manuel
 * */
public interface SeasonFactory {
    /**
     * This method is used to create a new enemy. It is overwritten by the factories for the different seasons.
     * Based on the season, the function gets the right animation sprites and creates a new enemy with it.
     * @param screen playscreen on which the enemy should appear
     * @param x x position of the enemy
     * @param y y position of the enemy
     * @return the new created enemy
     * @since 1.0
     *  */
    Enemy createWalker(PlayScreen screen, float x, float y);
    /**
     * This method is used to create a new coin. It is overwritten by the factories for the different seasons.
     * Based on the season, the function gets the right animation sprites and creates a new coin with it.
     * @param screen playscreen on which the coin should appear
     * @param x x position of the coin
     * @param y y position of the coin
     * @return the new created coin
     * @since 1.0
     *  */
    Collectable createCoin(PlayScreen screen, float x, float y);
}
