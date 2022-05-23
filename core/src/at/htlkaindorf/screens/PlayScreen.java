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

public class PlayScreen implements Screen{
    //Reference to our Game, used to set Screens
    private final TextureAtlas atlas;

    //basic playscreen variables
    private final OrthographicCamera gamecam;
    private final Viewport gamePort;

    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final WorldCreator creator;

    private final Hud hud;

    //sprites
    private final Player player;

    public PlayScreen(String mapName){
        TmxMapLoader maploader = new TmxMapLoader();
        map = maploader.load("maps/" + mapName + ".tmx");

        atlas = new TextureAtlas("sprites/Sprite_Sheet3.pack");
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
        Skin skin = new Skin(Gdx.files.internal("skins/pixthulhu/pixthulhu-ui.json"));
        Label timer = new Label("Timer",skin);
    }

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

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.getB2body().getLinearVelocity().x <= 1.5) {
            player.moveRight();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.getB2body().getLinearVelocity().x >= -1.5) {
            player.moveLeft();
        }

        player.updateCurrentState();
    }

    public void update(float dt){
        if(player.getCurrentState() == Player.State.DEAD){
            Game.getInstance().reloadGame();
        }

        handleInput(dt);

        world.step(1 / 60f, 6, 2);
        player.update(dt);

        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            enemy.getB2body().setActive(true);
        }

        float playerX = player.getB2body().getPosition().x;
        float playerY = player.getB2body().getPosition().y;

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
        if (player.getB2body().getPosition().x > 2 && player.getB2body().getPosition().x < 18.5) {
            gamecam.position.x = player.getB2body().getPosition().x;
        }

        System.out.println(player.getB2body().getPosition().x);

        gamecam.update();
        renderer.setView(gamecam);
        hud.update(dt);
    }


    @Override
    public void render(float delta) {
        update(delta);

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