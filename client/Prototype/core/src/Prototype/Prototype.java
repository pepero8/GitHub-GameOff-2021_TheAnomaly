package Prototype;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import net.Client;

public class Prototype extends Game {
	public static final int NUM_PLAYERS = 2; //number of players in each session
	public static final int MAP_WIDTH = 3840; // map width
	public static final int MAP_HEIGHT = 2160; // map height
	public static int VP_WIDTH; // viewport width. equals to monitor's width
	public static int VP_HEIGHT; // viewport height. equals to monitor's height
	public static final int CHAR_WIDTH = 64; // all character's width
	public static final int CHAR_HEIGHT = 128; // all character's height
	public static final int ATTACK_RANGE = 25; // all character's height

	public int playerNum = -1; //initialized by client

	TestScreen testScreen;
	LoadingScreen loadingScreen;

	public World world;

	Client client;

	public volatile boolean start;

	@Override
	public void create() {
		VP_WIDTH = Gdx.graphics.getWidth();
		VP_HEIGHT = Gdx.graphics.getHeight();

		world = new World();

		testScreen = new TestScreen(this);
		loadingScreen = new LoadingScreen(this);

		//this.setScreen(testScreen);
		this.setScreen(loadingScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		testScreen.dispose();
		loadingScreen.dispose();
		world.dispose();
		if (client != null) {
			client.dispose();
		}
		Gdx.app.log("Prototype", "disposed");
	}

	public void connect() {
		client = new Client(this);
		client.start();
	}

	public void disconnect() {
		// if (client != null) {
		// 	client.dispose();
		// 	client = null;
		// }
		dispose(); //temp
	}
}
