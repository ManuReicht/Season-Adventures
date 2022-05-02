package at.htlkaindorf.screens;

import at.htlkaindorf.Game;
import at.htlkaindorf.entities.Player;
import at.htlkaindorf.entities.enemies.Enemy;
import at.htlkaindorf.tools.B2WorldCreator;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen implements Screen{
    //Reference to our Game, used to set Screens
    private TextureAtlas atlas;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Player player;

    public PlayScreen(){
        //atlas = new TextureAtlas("Mario_and_Enemies.pack");

        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(Game.getInstance().getV_WIDTH() / Game.getInstance().getPPM(),
                                   Game.getInstance().getV_HEIGHT() / Game.getInstance().getPPM(), gamecam);

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("maps/test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1  / Game.getInstance().getPPM());

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set((gamePort.getWorldWidth() / 2), gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        //create mario in our game world
        player = new Player(this);

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {
    }

    public void handleInput(float dt){
        player.printState();
        //control our player using immediate impulses
        try {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    player.jump();
                } else if (player.getCurrentState().equals(Player.State.JUMPING)){
                    player.gainHeight();
                }
            }
        } catch (IndexOutOfBoundsException iooe) {

        } catch (NullPointerException npe) {

        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getB2body().getLinearVelocity().x <= 2)
            player.moveRight();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getB2body().getLinearVelocity().x >= -2)
            player.moveLeft();
        player.updateCurrentState();
    }

    public void update(float dt){

        //handle user input first
        handleInput(dt);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);
        player.update(dt);

        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            //if (enemy.getX() < player.getX() + 224 / Game.getInstance().getPPM()) {
            enemy.getB2body().setActive(true);
            //}
        }

        //attach our gamecam to our players.x coordinate
        if (player.getB2body().getPosition().x > 2) {
            gamecam.position.x = player.getB2body().getPosition().x;
        }

        //update our gamecam with correct coordinates after changes
        gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);

        //if player is dead, restart the game
        if(player.getCurrentState() == Player.State.DEAD){
            Game.getInstance().reloadGame();
        }

    }


    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);
        Game.getInstance().getBatch().setProjectionMatrix(gamecam.combined);

        //Game.getInstance().getBatch().begin();
        //player.draw(Game.getInstance().getBatch());
        //Game.getInstance().getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
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
        //dispose of all our opened resources
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}