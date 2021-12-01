package Prototype.desktop;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import Prototype.Prototype;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		DisplayMode primaryMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		//config.setFullscreenMode(primaryMode);
		config.setTitle("The Anomaly");
		config.setWindowedMode(600, 600);
		new Lwjgl3Application(new Prototype(), config);
	}
}
