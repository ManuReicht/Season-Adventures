package at.htlkaindorf.tools;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.collectables.Coin;
import at.htlkaindorf.entities.collectables.Collectable;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.entities.enemies.Walker;
import at.htlkaindorf.entities.factories.*;
import at.htlkaindorf.objects.InteractiveObject;
import at.htlkaindorf.objects.LevelEnd;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;


public class WorldCreator {
    private Array<Walker> walkers;
    private Array<Coin> coins;
    private Array<LevelEnd> levelEnds;

    public WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create terrain
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
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
        Game.getInstance().setCurrentSeason();
        SeasonFactory seasonFactory = null;
        switch (Game.getInstance().getCurrentSeason()) {
            case SPRING:
                seasonFactory = new SpringFactory();
                break;
            case SUMMER:
                seasonFactory = new SummerFactory();
                break;
            case AUTUMN:
                seasonFactory = new AutumnFactory();
                break;
            case WINTER:
                seasonFactory = new WinterFactory();
                break;
        }

        walkers = new Array<Walker>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            walkers.add((Walker) seasonFactory.createWalker(screen, rect.getX() / Game.getInstance().getPPM(),
                    rect.getY() / Game.getInstance().getPPM()));
        }

        //create coins
        coins = new Array<Coin>();
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            coins.add(new Coin(screen, rect.getX() / Game.getInstance().getPPM(),
                    rect.getY() / Game.getInstance().getPPM()));
        }

        //create level end
        levelEnds = new Array<LevelEnd>();
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            levelEnds.add(new LevelEnd(screen, object, rect.getX() / Game.getInstance().getPPM(),
                    rect.getY() / Game.getInstance().getPPM()));
            System.out.println("LEVEL END");
        }
    }


    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(walkers);
        return enemies;
    }

    public Array<Collectable> getCollectables(){
        Array<Collectable> collectables = new Array<Collectable>();
        collectables.addAll(coins);
        return collectables;
    }

    public Array<InteractiveObject> getInteractiveObjects() {
        Array<InteractiveObject> objects = new Array<>();
        objects.addAll(levelEnds);
        return objects;
    }
}