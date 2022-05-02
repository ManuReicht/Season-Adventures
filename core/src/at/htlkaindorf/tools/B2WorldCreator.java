package at.htlkaindorf.tools;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.entities.enemies.Walker;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;


public class B2WorldCreator {
    private Array<Walker> walkers;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create terrain
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
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

        //create enemies
        walkers = new Array<Walker>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            walkers.add(new Walker(screen, rect.getX() / Game.getInstance().getPPM(),
                    rect.getY() / Game.getInstance().getPPM()));
        }

        //create level end
        for(MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
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



    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(walkers);
        return enemies;
    }
}
