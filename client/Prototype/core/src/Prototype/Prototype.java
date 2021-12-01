package Prototype;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import net.Client;

public class Prototype extends Game {
	/**
	 * HOW TO ADD A PLAYER
	 * - change NUM_PLAYERS
	 * 
	 * World
	 * - add new player variable
	 * - add initialization of player variable in constructor
	 * - add player variable to players[] in constructor
	 * 
	 * Client
	 * - increase PACKET_SIZE
	 * - add code block for handling player input
	 */
	public static final int NUM_PLAYERS = 5; //number of players in each session
	public static final int PLAYER_ROBOT_NUM = 0;
	public static final int PLAYER1_NUM = 1;
	public static final int PLAYER2_NUM = 2;
	public static final int PLAYER3_NUM = 3;
	public static final int PLAYER4_NUM = 4;
	public static final int MAP_WIDTH = 3840; // map width
	public static final int MAP_HEIGHT = 2160; // map height
	public static int VP_WIDTH = 600; // viewport width. equals to monitor's width
	public static int VP_HEIGHT = 600; // viewport height. equals to monitor's height
	public static int VP_HEIGHT_ROBOT = 600; // viewport height. equals to monitor's height
	public static int VP_WIDTH_ROBOT = 600; // viewport width. equals to monitor's width
	public static int VP_HEIGHT_SURVIVOR = 400; // viewport height. equals to monitor's height
	public static int VP_WIDTH_SURVIVOR = 400; // viewport width. equals to monitor's width
	public static final int CHAR_WIDTH = 32; // all character's width
	public static final int CHAR_HEIGHT = 32; // all character's height
	public static final int ATTACK_RANGE = 15; // robot's attack range
	public static final int PROJECTILE_WIDTH = 25;
	public static final int PROJECTILE_HEIGHT = 25;
	public static final int PROJECTILE_DISTANCE = 300;

	public int playerNum = -1; //initialized by client
	public String playerName = "player";

	//TestScreen testScreen;
	//SessionWaitScreen waitScreen;
	//ObserverScreen observerScreen;
	//ResultScreen resultScreen;
	//IntroScreen introScreen;
	//MainMenuScreen mainMenuScreen;

	public World world;
	public Assets assets;

	Client client;

	public volatile boolean sessionStart; //true: session started, false: session terminated
	public volatile boolean gameStart;
	public volatile char gameEndCode; //used at game end screen

	@Override
	public void create() {
		//world = new World(this);

		assets = new Assets();

		//testScreen = new TestScreen(this);
		//waitScreen = new SessionWaitScreen(this);
		//observerScreen = new ObserverScreen(this);
		//resultScreen = new ResultScreen(this);
		//introScreen = new IntroScreen(this);
		//mainMenuScreen = new MainMenuScreen(this);
		//this.setScreen(testScreen);
		//this.setScreen(waitScreen);
		//this.setScreen(resultScreen);
		//this.setScreen(introScreen);
		//this.setScreen(mainMenuScreen);
		this.setScreen(new MainMenuScreen(this));
	}

	public void init() {
		assets.dispose();
		playerNum = -1;
		playerName = "player";
		sessionStart = false;
		gameStart = false;
		gameEndCode = '0';
		assets = new Assets();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		//testScreen.dispose();
		//waitScreen.dispose();
		//observerScreen.dispose();
		//resultScreen.dispose();
		//introScreen.dispose();
		//mainMenuScreen.dispose();
		if (world != null)
			world.dispose();
		assets.dispose();

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
		if (client != null) {
			client.dispose();
			client = null;
		}
		//dispose(); //temp
	}
}
