package at.htlkaindorf;

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
		setScreen(new PlayScreen());
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
}
