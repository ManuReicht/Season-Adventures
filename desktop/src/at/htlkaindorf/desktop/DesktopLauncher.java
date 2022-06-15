package at.htlkaindorf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import at.htlkaindorf.Game;

/**
 * Contains the main class of the game. It creates an application of the game.
 * @author Trummer Nik
 * */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(Game.getInstance(), config);
	}
}
