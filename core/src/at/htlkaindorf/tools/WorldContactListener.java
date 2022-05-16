package at.htlkaindorf.tools;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.entities.collectables.Collectable;
import at.htlkaindorf.entities.enemies.Enemy;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by brentaureli on 9/4/15.
 */
public class WorldContactListener implements ContactListener {

    private final static short NOTHING_BIT = Game.getInstance().getNOTHING_BIT();
    private final static short TERRAIN_BIT = Game.getInstance().getTERRAIN_BIT();
    private final static short PLAYER_BIT = Game.getInstance().getPLAYER_BIT();
    //private final static short BRICK_BIT = Game.getInstance().getBRICK_BIT();
    //private final static short COIN_BIT = Game.getInstance().getCOIN_BIT();
    //private final static short DESTROYED_BIT = Game.getInstance().getDESTROYED_BIT();
    //private final static short OBJECT_BIT = Game.getInstance().getOBJECT_BIT();
    private final static short ENEMY_BIT = Game.getInstance().getENEMY_BIT();
    private final static short ENEMY_HEAD_BIT = Game.getInstance().getENEMY_HEAD_BIT();
    //private final static short ITEM_BIT = Game.getInstance().getITEM_BIT();
    private final static short PLAYER_HEAD_BIT = Game.getInstance().getPLAYER_HEAD_BIT();
    private final static short LEVEL_END_BIT = Game.getInstance().getLEVEL_END_BIT();
    private final static short COLLECTABLE_BIT = Game.getInstance().getCOLLECTABLE_BIT();

    private Fixture fixA;
    private Fixture fixB;

    @Override
    public void beginContact(Contact contact) {
        fixA = contact.getFixtureA();
        fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        //if (cDef == (PLAYER_HEAD_BIT | BRICK_BIT) || cDef == (PLAYER_HEAD_BIT | COIN_BIT)) { // Player hits destroyable Block with head
        //destroyBlock();
        //}
        if (cDef == (ENEMY_HEAD_BIT | PLAYER_BIT)) { // Player collides with the head of an enemy
            jumpOnEnemy();
            Game.getInstance().getCurrentPlayScreen().getHud().addScore(50);
        }
        //else if (cDef == (ENEMY_BIT | OBJECT_BIT)) { // Enemy collides with an object
        //  reverseEnemyVelocity(true, false);
        //}
        else if (cDef == (PLAYER_BIT | ENEMY_BIT)) { // Player collides with an enemy
            takeDamage();
            System.out.println("PlayerHitEnemy");
        } else if (cDef == (ENEMY_BIT | ENEMY_BIT)) { // Two enemys collide with each other
            hitByOtherEnemy();
        } else if (cDef == (PLAYER_BIT | LEVEL_END_BIT)) {
            Game.getInstance().loadNextMap();
        }
        //else if (cDef == (ITEM_BIT | OBJECT_BIT)) { // A moving item collides with an object
        //reverseItemVelocity(true, false);
        //}
        else if (cDef == (COLLECTABLE_BIT | PLAYER_BIT)) { // An item collides with the player
            collectItem();
            Game.getInstance().getCurrentPlayScreen().getHud().addScore(100);
            System.out.println("Collected");
        }
    }

    /*private void destroyBlock() {
        if (fixA.getFilterData().categoryBits == Plattformer.getInstance().getPLAYER_HEAD_BIT())
            ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
        else
            ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
    }*/

    private void jumpOnEnemy() {
        if (fixA.getFilterData().categoryBits == Game.getInstance().getENEMY_HEAD_BIT())
            ((Enemy) fixA.getUserData()).hitOnHead((Player) fixB.getUserData());
        else
            ((Enemy) fixB.getUserData()).hitOnHead((Player) fixA.getUserData());
    }

    private void reverseEnemyVelocity(boolean x, boolean y) {
        if (fixA.getFilterData().categoryBits == Game.getInstance().getENEMY_BIT())
            ((Enemy) fixA.getUserData()).reverseVelocity(x, y);
        else
            ((Enemy) fixB.getUserData()).reverseVelocity(x, y);
    }

    /*private void reverseItemVelocity(boolean x, boolean y) {
        if (fixA.getFilterData().categoryBits == Game.getInstance().getENEMY_BIT())
            ((Item) fixA.getUserData()).reverseVelocity(x, y);
        else
            ((Item) fixB.getUserData()).reverseVelocity(x, y);
    }*/

    private void collectItem() {
        if (fixA.getFilterData().categoryBits == Game.getInstance().getCOLLECTABLE_BIT())
            ((Collectable) fixA.getUserData()).collect((Player) fixB.getUserData());
        else
            ((Collectable) fixB.getUserData()).collect((Player) fixA.getUserData());
    }

    /*private void setToDestroy() {
        if (fixA.getFilterData().categoryBits == Game.getInstance().getFIREBALL_BIT())
            ((FireBall) fixA.getUserData()).setToDestroy();
        else
            ((FireBall) fixB.getUserData()).setToDestroy();
    }*/

    private void takeDamage() {
        if (fixA.getFilterData().categoryBits == Game.getInstance().getPLAYER_BIT())
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
