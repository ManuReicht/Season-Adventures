package at.htlkaindorf.objects;

import at.htlkaindorf.Game;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

/**
 * The InteravtiveObject class is the parent class of all object the player can interact with.
 * It extends the sprite class.
 * @author Trummer Nik
 * */
public abstract class InteractiveObject  extends Sprite {
    /**
     * World in which the object should appear.
     * */
    protected World world;
    /**
     * The hit box of the object. Other object can collide with it.
     * */
    protected Rectangle bounds;
    /**
     * The body of the object with position, texture, ... .
     * */
    protected Body body;
    /**
     * Used to set the right frame for the animaition.
     * */
    protected float stateTime;
    /**
     * Stores the filter bit of the object.
     * */
    protected Fixture fixture;

    /**
     * Sets the position and the bounds of the object.
     * @param screen playscreen on which the object should appear
     * @param x x position of the object
     * @param y y position of the object
     * @since 1.0
     *  */
    public InteractiveObject(PlayScreen screen, MapObject object, float x, float y) {
        this.world = screen.getWorld();
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

    /**
     * Updates the stateTime every frame.
     * @param dt delta time
     * @since 1.0
     *  */
    public void update(float dt){
        stateTime += dt;
    }

    /**
     * Draws the sprite of the object.
     * @param batch the sprite batch with the sprites
     * @since 1.0
     * */
    public void draw(Batch batch){
        super.draw(batch);
    }

    /**
     * Sets the category filter of the object. It is used to detect collision with the object.
     * @param filterBit the filterBit for the object
     * @since 1.0
     * */
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
