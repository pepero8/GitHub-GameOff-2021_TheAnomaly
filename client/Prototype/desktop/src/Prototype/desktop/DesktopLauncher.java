package Prototype.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import Prototype.Prototype;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("The Anomaly");
		config.setWindowedMode(900, 900);
		config.setResizable(false);
		new Lwjgl3Application(new Prototype(), config);
	}
}
