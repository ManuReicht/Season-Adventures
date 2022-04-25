package at.htlkaindorf;

import at.htlkaindorf.screens.MainMenuScreen;
import at.htlkaindorf.screens.PlayScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends com.badlogic.gdx.Game {
	private final int V_WIDTH = 400;
	private final int V_HEIGHT = 208;
	private final float PPM = 100;

	private final short NOTHING_BIT = 0;
	private final short TERRAIN_BIT = 1;
	private final short PLAYER_BIT = 2;
	//private final short BRICK_BIT = 4;
	//private final short COIN_BIT = 8;
	//private final short DESTROYED_BIT = 16;
	private final short OBJECT_BIT = 32;
	private final short ENEMY_BIT = 64;
	private final short ENEMY_HEAD_BIT = 128;
	//private final short ITEM_BIT = 256;
	private final short MARIO_HEAD_BIT = 512;

	private static Game instance;
	private SpriteBatch batch;

	private AssetManager manager;

	private Game() {}

	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();

		manager.finishLoading();
		//setScreen(new PlayScreen());
		setScreen(new MainMenuScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
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

	public int getV_WIDTH() {
		return V_WIDTH;
	}
	public int getV_HEIGHT() {
		return V_HEIGHT;
	}
	public float getPPM() { return PPM; }

	public void reloadGame(){
		setScreen(new PlayScreen());
	}

	public short getNOTHING_BIT() {
		return NOTHING_BIT;
	}

	public short getTERRAIN_BIT() {
		return TERRAIN_BIT;
	}

	public short getMARIO_BIT() {
		return PLAYER_BIT;
	}

	/*public short getBRICK_BIT() {
		return BRICK_BIT;
	}*/

	/*public short getCOIN_BIT() {
		return COIN_BIT;
	}*/

	/*public short getDESTROYED_BIT() {
		return DESTROYED_BIT;
	}*/

	public short getOBJECT_BIT() {
		return OBJECT_BIT;
	}

	public short getENEMY_BIT() {
		return ENEMY_BIT;
	}

	public short getENEMY_HEAD_BIT() {
		return ENEMY_HEAD_BIT;
	}

	/*public short getITEM_BIT() {
		return ITEM_BIT;
	}*/

	public short getPLAYER_HEAD_BIT() {
		return MARIO_HEAD_BIT;
	}

}
