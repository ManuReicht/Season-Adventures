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

/**
 * Used to create all hitboxes and objects (Enemies, Collectables, LevelEnds) in a level.
 * @author Trummer Nik
 * */
public class WorldCreator {
    private Array<Walker> walkers;
    private Array<Coin> coins;
    private Array<LevelEnd> levelEnds;
    private PlayScreen screen;


    /**
     * Gets the current season and creates
     * the terrain, all enemies, collectables and levelEnds.
     * @author Trummer Nik
     * */
    public WorldCreator(PlayScreen screen) {
        this.screen = screen;
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        Game.getInstance().setCurrentSeason();
        SeasonFactory seasonFactory = getFactory();

        createTerrain(world, map);
        createEnemies(seasonFactory, map);
        createCollectables(seasonFactory, map);
        createLevelEnds(seasonFactory, map);
    }
    /**
     * Gets the current season from the game and sets the right factory for it.
     * @return the right factory for the season of the current level
     * @since 1.0
     *  */
    private SeasonFactory getFactory() {
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

        return seasonFactory;
    }

    /**
     * Creates the collision hitboxes based on the areas in the tiled map.
     * @param world the world which contains the current level
     * @param map the map which contains the areas where the hitboxes should be created
     * @since 1.0
     *  */
    public void createTerrain(World world, TiledMap map) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

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
    }

    /**
     * Creates enemies based on the information of the tiled map.
     * @param seasonFactory the season factory to create the right enemies for the current season
     * @param map the map which contains the positions where the enemies should be created
     * @since 1.0
     *  */
    private void createEnemies(SeasonFactory seasonFactory, TiledMap map) {
        walkers = new Array<Walker>();

        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            walkers.add((Walker) seasonFactory.createWalker(screen, rect.getX() / Game.getInstance().getPPM(),
                    rect.getY() / Game.getInstance().getPPM()));
        }
    }

    /**
     * Creates collectables based on the information of the tiled map.
     * @param seasonFactory the season factory to create the right collectables for the current season
     * @param map the map which contains the positions where the collectables should be created
     * @since 1.0
     *  */
    private void createCollectables(SeasonFactory seasonFactory, TiledMap map) {
        coins = new Array<Coin>();

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            coins.add((Coin) seasonFactory.createCoin(screen, rect.getX() / Game.getInstance().getPPM(),
                    rect.getY() / Game.getInstance().getPPM()));
        }
    }

    /**
     * Creates level ends based on the information of the tiled map.
     * @param seasonFactory the season factory to create the right level ends for the current season
     * @param map the map which contains the positions where the level ends should be created
     * @since 1.0
     *  */
    private void createLevelEnds(SeasonFactory seasonFactory, TiledMap map) {
        levelEnds = new Array<LevelEnd>();

        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            levelEnds.add(new LevelEnd(screen, object, rect.getX() / Game.getInstance().getPPM(),
                    rect.getY() / Game.getInstance().getPPM()));
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