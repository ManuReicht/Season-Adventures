package at.htlkaindorf.objects;

import at.htlkaindorf.Game;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public abstract class InteractiveObject  extends Sprite {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected PlayScreen screen;
    protected MapObject object;
    protected float stateTime;
    protected Fixture fixture;

    public InteractiveObject(PlayScreen screen, MapObject object, float x, float y) {
        this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Game.getInstance().getPPM(),
                (bounds.getY() + bounds.getHeight() / 2) / Game.getInstance().getPPM());

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / Game.getInstance().getPPM(),
                bounds.getHeight() / 2 / Game.getInstance().getPPM());
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

        setPosition(x - 0.05f, y - 0.02f);
        setBounds(getX(), getY(), 40 / Game.getInstance().getPPM(), 40 / Game.getInstance().getPPM());

        stateTime = 0;

    }

    public void update(float dt){
        stateTime += dt;
    }

    public void draw(Batch batch){
        super.draw(batch);
    }

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
