package be.catvert.marioshooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import be.catvert.marioshooter.Game;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.FileReader;

public class DesktopLauncher {
	private static int screenWidth = 1280, screenHeight = 720;
	private static boolean vsync = true, fullscreen = false;

	public static void main (String[] arg) {
		loadConfig();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = screenWidth;
		config.height = screenHeight;
		config.vSyncEnabled = vsync;
		config.fullscreen = fullscreen;
		config.resizable = false;

		config.title = "Mario Shooter";

		new LwjglApplication(new Game(), config);
	}
	/**
	 * Permet le chargement du fichier de configuration du jeu, config.json
	 */
	private static void loadConfig() {
		try {
			JsonValue root = new JsonReader().parse(new FileReader("config.json"));

			screenWidth = root.getInt("width");
			screenHeight = root.getInt("height");
			vsync = root.getBoolean("vsync");
			fullscreen = root.getBoolean("fullscreen");
		} catch(Exception e) {
			System.err.println("Erreur lors du chargement de la configuration du jeu ! Erreur : " + e.getMessage());
		}
	}
}
