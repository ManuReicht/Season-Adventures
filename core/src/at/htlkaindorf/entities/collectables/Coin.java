package at.htlkaindorf.entities.collectables;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Coin extends Collectable{
    public Coin(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("player_jump"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Game.getInstance().getPPM());
        fdef.filter.categoryBits = Game.getInstance().getCOLLECTABLE_BIT();
        fdef.filter.maskBits = (short) Game.getInstance().getPLAYER_BIT();

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void collect(Player mario) {
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }
}
