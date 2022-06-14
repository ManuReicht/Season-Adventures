package at.htlkaindorf.screens;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.entities.collectables.Collectable;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.objects.InteractiveObject;
import at.htlkaindorf.scenes.Hud;
import at.htlkaindorf.tools.WorldCreator;
import at.htlkaindorf.tools.WorldContactListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The play screen which the user can see. All objects (player, enemies, coins, ...) are placed
 * on the play screen. It also handles all inputs.
 * It only exists once.
 * @author Trummer Nik
 * */
public class PlayScreen implements Screen{
    /**
     * The texture atlas of the game. It contains all sprites of all objects, enemies and the player.
     * It only exists once.
     */
    private final TextureAtlas atlas;

    /**
     * The camera of the playscreen. It follows the player.
     */
    private final OrthographicCamera gamecam;
    /**
     * The viewport of the playscreen.
     */
    private final Viewport gamePort;

    /**
     * The current level which is displayed on the playscreen.
     */
    private final TiledMap map;
    /**
     * The map renderer of the playscreen. It renders all sprites of the level.
     */
    private final OrthogonalTiledMapRenderer renderer;

    /**
     * The world of the playscreen. All collisions, physics and hitboxes are in the world
     * while the graphics are in the playscreen.
     * */
    private final World world;
    /**
     * Only for debugging!
     * It allows to render debug lines.
     * */
    private final Box2DDebugRenderer b2dr;
    /**
     * The creator of the world. It creates all objects which should appear in the world.
     * */
    private final WorldCreator creator;

    /**
     * The hud of the playscreen. It shows values like score, time, world, ... on the playscreen.
     * */
    private final Hud hud;

    /**
     * A player instance which will be created on the playscreen.
     * */
    private final Player player;

    /**
     * Loads the map and the sprite sheet into the game.
     * @param mapName the name of the map which should be loaded in the playscreen
     * @since 1.0
     *  */
    public PlayScreen(String mapName){
        TmxMapLoader maploader = new TmxMapLoader();
        map = maploader.load("maps/" + mapName + ".tmx");

        atlas = new TextureAtlas("sprites/Sprite_Sheet6.pack");
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Game.getInstance().getV_WIDTH() / Game.getInstance().getPPM(),
                                   Game.getInstance().getV_HEIGHT() / Game.getInstance().getPPM(), gamecam);
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        creator = new WorldCreator(this);
        player = new Player(this);
        hud = new Hud(Game.getInstance().getBatch());

        renderer = new OrthogonalTiledMapRenderer(map, 1  / Game.getInstance().getPPM());

        hud.setLevelName(mapName);

        gamecam.position.set((gamePort.getWorldWidth() / 2), gamePort.getWorldHeight() / 2, 0);

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    /**
     * Handles all the inputs of the user and calls the corresponding function to controll the player.
     * @param dt delta time
     * @since 1.0
     *  */
    public void handleInput(float dt){
        player.printState();
        try {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    player.jump();
                } else if (player.getCurrentState().equals(Player.State.JUMPING)){
                    player.gainHeight();
                }
            }
        } catch (IndexOutOfBoundsException | NullPointerException iooe) {

        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getBody().getLinearVelocity().x <= 1.5) {
            player.moveRight();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getBody().getLinearVelocity().x >= -1.5) {
            player.moveLeft();
        }

        player.updateCurrentState();
    }

    /**
     * Checks which actions are occure on the playscreen and calls corresponding functions.
     * @param dt delta time
     * @since 1.0
     *  */
    public void update(float dt){
        if(player.getCurrentState() == Player.State.DEAD){
            Game.getInstance().reloadGame();
        }

        handleInput(dt);

        world.step(1 / 60f, 6, 2);
        player.update(dt);

        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            enemy.getBody().setActive(true);
        }

        float playerX = player.getBody().getPosition().x;
        float playerY = player.getBody().getPosition().y;

        for(Collectable collectable : creator.getCollectables()) {
            float collectableX = collectable.getB2body().getPosition().x;
            float collectableY = collectable.getB2body().getPosition().y;
            if (Math.abs(playerX - collectableX) < 0.1 && Math.abs(playerY - collectableY) < 0.1) {
                collectable.destroy();
            }
            collectable.update(dt);
        }

        for(InteractiveObject object : creator.getInteractiveObjects()) {
            object.update(dt);
        }

        //attach our gamecam to our players.x coordinate
        if (player.getBody().getPosition().x > 2 && player.getBody().getPosition().x < 18.5) {
            gamecam.position.x = player.getBody().getPosition().x;
        }

        System.out.println(player.getBody().getPosition().x);

        gamecam.update();
        renderer.setView(gamecam);
        hud.update(dt);
    }


    /**
     * Renders the sprites of all objects, enemies and the player.
     * @param dt delta time
     * @since 1.0
     *  */
    @Override
    public void render(float dt) {
        update(dt);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
        //b2dr.render(world, gamecam.combined);

        Game.getInstance().getBatch().setProjectionMatrix(gamecam.combined);

        Game.getInstance().getBatch().begin();
        player.draw(Game.getInstance().getBatch());

        for (Enemy enemy : creator.getEnemies()) {
            enemy.draw(Game.getInstance().getBatch());
        }

        for (Collectable collectable : creator.getCollectables()) {
            collectable.draw(Game.getInstance().getBatch());
        }

        for (InteractiveObject object : creator.getInteractiveObjects()) {
            object.draw(Game.getInstance().getBatch());
        }

        Game.getInstance().getBatch().end();
        Game.getInstance().getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
    public Hud getHud(){ return hud; }
}