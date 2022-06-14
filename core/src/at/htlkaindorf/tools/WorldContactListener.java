package at.htlkaindorf.tools;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.entities.collectables.Collectable;
import at.htlkaindorf.entities.enemies.Enemy;
import com.badlogic.gdx.physics.box2d.*;

/**
 * The WorldContactListener is used to create a contact listener for the game.
 * It is responsible for all collision detections.
 * @author Trummer Nik
 * */
public class WorldContactListener implements ContactListener {

    /**
     * Stores the bit (a power of two) of the player. It is used to determine if the player is part of a collision.
     * */
    private final static short PLAYER_BIT = Game.getInstance().PLAYER_BIT;
    /**
     * Stores the bit (a power of two) of an enemy. It is used to determine if an enemy is part of a collision.
     * */
    private final static short ENEMY_BIT = Game.getInstance().ENEMY_BIT;
    /**
     * Stores the bit (a power of two) of an enemy head. It is used to determine if a player jumps on an enemy.
     * */
    private final static short ENEMY_HEAD_BIT = Game.getInstance().ENEMY_HEAD_BIT;
    /**
     * Stores the bit (a power of two) of the level end. It is used to determine if the player reaches the end of the level.
     * */
    private final static short LEVEL_END_BIT = Game.getInstance().LEVEL_END_BIT;

    /**
     * One of the two fixtures (hitboxes) which are part of a collision.
     * */
    private Fixture fixA;
    /**
     * One of the two fixtures (hitboxes) which are part of a collision.
     * */
    private Fixture fixB;

    /**
     * Called if two fixtures collide with each other.
     * It calls a function based on the two fixtures.
     * @param contact stores the two fixtures with are involved in the collsion
     * */
    @Override
    public void beginContact(Contact contact) {
        fixA = contact.getFixtureA();
        fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (cDef == (ENEMY_HEAD_BIT | PLAYER_BIT)) { // Player collides with the head of an enemy
            jumpOnEnemy();
            Game.getInstance().getCurrentPlayScreen().getHud().addScore(50);
        } else if (cDef == (PLAYER_BIT | ENEMY_BIT)) { // Player collides with an enemy
            takeDamage();
            System.out.println("PlayerHitEnemy");
        } else if (cDef == (ENEMY_BIT | ENEMY_BIT)) { // Two enemys collide with each other
            hitByOtherEnemy();
        } else if (cDef == (PLAYER_BIT | LEVEL_END_BIT)) {
            Game.getInstance().loadNextMap();
        }
    }

    /**
     * Called if the player jumps on an enemy and calles the hitOnHead function of the enemy.
     * */
    private void jumpOnEnemy() {
        if (fixA.getFilterData().categoryBits == Game.getInstance().ENEMY_HEAD_BIT)
            ((Enemy) fixA.getUserData()).hitOnHead((Player) fixB.getUserData());
        else
            ((Enemy) fixB.getUserData()).hitOnHead((Player) fixA.getUserData());
    }

    /**
     * Called if the player gets hit by an enemy and calles the hit function of the player.
     * */
    private void takeDamage() {
        if (fixA.getFilterData().categoryBits == Game.getInstance().PLAYER_BIT)
            ((Player) fixA.getUserData()).hit((Enemy) fixB.getUserData());
        else
            ((Player) fixB.getUserData()).hit((Enemy) fixA.getUserData());
    }

    /**
     * Called if an enemy hits another one and calles the hitByEnemy funtion of both enemies.
     * */
    private void hitByOtherEnemy() {
        ((Enemy) fixA.getUserData()).hitByEnemy((Enemy) fixB.getUserData());
        ((Enemy) fixB.getUserData()).hitByEnemy((Enemy) fixA.getUserData());
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
