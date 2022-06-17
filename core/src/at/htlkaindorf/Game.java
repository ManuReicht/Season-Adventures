package at.htlkaindorf;

import at.htlkaindorf.screens.MainMenuScreen;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The Game class follows the singleton pattern, so there can only exist one instance of it.
 * It contains things like the season, level and playscreen.
 * It also contains some static variables with global values for the game.
 * @author Trummer Nik
 * */
public class Game extends com.badlogic.gdx.Game {
    /**
     * The virtual width of the game.
     */
    public final int V_WIDTH = 400;
    /**
     * The virtual height of the game.
     */
    public final int V_HEIGHT = 208;
    /**
     * Used to scale all elements in a game down to controll the "size of the game".
     */
    public final float PPM = 100;

    /**
     * Stores the bit (a power of two) of the terrain. It is used to determine if the player collides with the terrain.
     * */
    public final short TERRAIN_BIT = 1;
    /**
     * Stores the bit (a power of two) of the player. It is used to determine if the player is part of a collision.
     * */
    public final short PLAYER_BIT = 2;
    /**
     * Stores the bit (a power of two) of an enemy. It is used to determine if an enemy is part of a collision.
     * */
    public final short ENEMY_BIT = 64;
    /**
     * Stores the bit (a power of two) of an enemy head. It is used to determine if a player jumps on an enemy.
     * */
    public final short ENEMY_HEAD_BIT = 128;
    /**
     * Stores the bit (a power of two) of the level end. It is used to determine if the player reaches the end of the level.
     * */
    public final short LEVEL_END_BIT = 1024;

    /**
     * The instance of the game class.
     * Because the Game class follows the singleton pattern, it is not possible to create another instance.
     * */
    private static Game instance;

    /**
     * The sprite batch is used to draw all sprites of the game. It should only exist once because of its
     * hight resource usage.
     * */
    private SpriteBatch batch;
    /**
     * Used to manage all assets of the game. It should only exist once because of its
     * hight resource usage.
     * */
    private AssetManager manager;

    /**
     * Stores the name of the current level.
     * */
    private String currentLevel;
    /**
     * Stores the current Season. It is an instance of the Season enum.
     * */
    private Season currentSeason;
    public enum Season { SPRING, SUMMER, AUTUMN, WINTER};

    /**
     * The playscreen of the game. It displayes the level which the user can see.
     * */
    private PlayScreen currentPlayScreen = null;

    /**
     * Stores the total number of levels per world. It is used to load the right level when the current
     * one is finished.
     * */
    private int totalNumberOfLevelsInWorld = 1;
    /**
     * Stores the number of existing worlds. It is used to finish the game if the last level is completed.
     * */
    private int totalNumberOfWorlds = 4;

    /**
     * The private constructor of the Game class. It is needed to make sure that no other instance of the
     * game class is created.
     * @since 1.0
     * */
    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     * Called when the game starts. It creates all elements the game needs.
     * @since 1.0
     * */
    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();

        manager.finishLoading();
        setScreen(new MainMenuScreen());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getManager() {
        return manager;
    }

    /**
     * Called to load the next map.
     * @param mapName the name of the map which should be loaded
     * */
    public void loadMap(String mapName) {
        currentLevel = mapName;
        currentPlayScreen = new PlayScreen(mapName);
        setCurrentSeason();
        setScreen(currentPlayScreen);
    }

    /**
     * Called to reload the game. It calls the loadMap function with the current map.
     * @since 1.0
     * */
    public void reloadGame() {
        loadMap(currentLevel);
    }

    /**
     * Finds the next level based on the previous one and calls the loadMap function with it.
     * @since 1.0
     * */
    public void loadNextMap() {
        int season = Integer.parseInt(currentLevel.split("-")[0]);
        int level = Integer.parseInt(currentLevel.split("-")[1]);

        if (level < totalNumberOfLevelsInWorld) {
            level++;
        } else {
            if (season < totalNumberOfWorlds) {
                season++;
            } else {
                season = 1;
                level = 1;
                setScreen(new MainMenuScreen());
                return;
            }
            level = 1;
        }

        loadMap(season + "-" + level);
    }


    public int getV_WIDTH() {
        return V_WIDTH;
    }

    public int getV_HEIGHT() {
        return V_HEIGHT;
    }

    public float getPPM() {
        return PPM;
    }

    public PlayScreen getCurrentPlayScreen() {
        return currentPlayScreen;
    }

    /**
     * Sets the current season based on the current level.
     * @since 1.0
     * */
    public void setCurrentSeason(){
        int season = Integer.parseInt(currentLevel.split("-")[0]);
        switch(season){
            case 1:
                currentSeason = Season.SPRING;
                break;
            case 2:
                currentSeason = Season.SUMMER;
                break;
            case 3:
                currentSeason = Season.AUTUMN;
                break;
            case 4:
                currentSeason = Season.WINTER;
                break;
        }
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }
}