package at.htlkaindorf.tools;

import at.htlkaindorf.Game;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by brentaureli on 8/28/15.
 */
public class B2WorldCreator {

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Game.getInstance().getPPM(),
                              (rect.getY() + rect.getHeight() / 2) / Game.getInstance().getPPM());

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Game.getInstance().getPPM(),
                          rect.getHeight() / 2 / Game.getInstance().getPPM());
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //create pipe bodies/fixtures
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Game.getInstance().getPPM(),
                              (rect.getY() + rect.getHeight() / 2) / Game.getInstance().getPPM());

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Game.getInstance().getPPM(),
                           rect.getHeight() / 2 / Game.getInstance().getPPM());
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }
}
