package at.htlkaindorf;

import at.htlkaindorf.screens.MainMenuScreen;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends com.badlogic.gdx.Game {
    public final int V_WIDTH = 400;
    public final int V_HEIGHT = 208;
    public final float PPM = 100;
    public final short NOTHING_BIT = 0;
    public final short TERRAIN_BIT = 1;
    public final short PLAYER_BIT = 2;
    public final short COLLECTABLE_BIT = 32;
    public final short ENEMY_BIT = 64;
    public final short ENEMY_HEAD_BIT = 128;
    public final short PLAYER_HEAD_BIT = 512;
    public final short LEVEL_END_BIT = 1024;

    private static Game instance;
    private SpriteBatch batch;

    private AssetManager manager;

    private String currentLevel;
    private Season currentSeason;
    public enum Season { SPRING, SUMMER, AUTUMN, WINTER};

    private PlayScreen currentPlayScreen = null;

    private int totalNumberOfLevelsInWorld = 1;
    private int totalNumberOfWorlds = 3;

    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

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

    public void loadMap(String mapName) {
        currentLevel = mapName;
        currentPlayScreen = new PlayScreen(mapName);
        setCurrentSeason();
        setScreen(currentPlayScreen);
    }

    public void reloadGame() {
        loadMap(currentLevel);
    }

    public void loadNextMap() {
        if(currentLevel.equals("test")) {
            reloadGame();
            return;
        }

        int season = Integer.parseInt(currentLevel.split("-")[0]);
        int level = Integer.parseInt(currentLevel.split("-")[1]);

        if (level < totalNumberOfLevelsInWorld) {
            level++;
        } else {
            if (season < totalNumberOfWorlds) {
                season++;
            } else {
                season = 1;
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