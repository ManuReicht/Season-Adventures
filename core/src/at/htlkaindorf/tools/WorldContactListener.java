package at.htlkaindorf.tools;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.entities.collectables.Collectable;
import at.htlkaindorf.entities.enemies.Enemy;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {

    private final static short NOTHING_BIT = Game.getInstance().NOTHING_BIT;
    private final static short TERRAIN_BIT = Game.getInstance().TERRAIN_BIT;
    private final static short PLAYER_BIT = Game.getInstance().PLAYER_BIT;
    private final static short ENEMY_BIT = Game.getInstance().ENEMY_BIT;
    private final static short ENEMY_HEAD_BIT = Game.getInstance().ENEMY_HEAD_BIT;
    private final static short PLAYER_HEAD_BIT = Game.getInstance().PLAYER_HEAD_BIT;
    private final static short LEVEL_END_BIT = Game.getInstance().LEVEL_END_BIT;
    private final static short COLLECTABLE_BIT = Game.getInstance().COLLECTABLE_BIT;

    private Fixture fixA;
    private Fixture fixB;

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
        } else if (cDef == (COLLECTABLE_BIT | PLAYER_BIT)) { // An item collides with the player
            collectItem();
            Game.getInstance().getCurrentPlayScreen().getHud().addScore(100);
            System.out.println("Collected");
        }
    }

    private void jumpOnEnemy() {
        if (fixA.getFilterData().categoryBits == Game.getInstance().ENEMY_HEAD_BIT)
            ((Enemy) fixA.getUserData()).hitOnHead((Player) fixB.getUserData());
        else
            ((Enemy) fixB.getUserData()).hitOnHead((Player) fixA.getUserData());
    }

    private void reverseEnemyVelocity(boolean x, boolean y) {
        if (fixA.getFilterData().categoryBits == Game.getInstance().ENEMY_BIT)
            ((Enemy) fixA.getUserData()).reverseVelocity(x, y);
        else
            ((Enemy) fixB.getUserData()).reverseVelocity(x, y);
    }

    private void collectItem() {
        if (fixA.getFilterData().categoryBits == Game.getInstance().COLLECTABLE_BIT)
            ((Collectable) fixA.getUserData()).collect((Player) fixB.getUserData());
        else
            ((Collectable) fixB.getUserData()).collect((Player) fixA.getUserData());
    }

    private void takeDamage() {
        if (fixA.getFilterData().categoryBits == Game.getInstance().PLAYER_BIT)
            ((Player) fixA.getUserData()).hit((Enemy) fixB.getUserData());
        else
            ((Player) fixB.getUserData()).hit((Enemy) fixA.getUserData());
    }

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
