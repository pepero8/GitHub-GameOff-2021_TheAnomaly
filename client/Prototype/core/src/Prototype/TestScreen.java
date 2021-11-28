package Prototype;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.MsgCodes;

public class TestScreen implements Screen {
	private Prototype game;

	private ShapeRenderer shapeRenderer; // temporary renderer to indicate boundary of the map
	private BitmapFont font; //is used to show camera's position
	private SpriteBatch batch; //is used to draw camera's position font
	private Stage stage;
	private FitViewport viewport;
	private OrthographicCamera playerCamera; // camera following the player

	//private Image remainTimeBox;

	//ui components
	private Label remainTimeLabel;
	private Label objectivesLabel;
	private Label objective1Label;
	private Label objective2Label;
	private Label messageLabel;
	private Image player1State;
	private Image player2State;
	private Image player3State;
	private Image player4State;

	private float[][] playerPositions;

	private float elapsed = 0;

	//constructor
	public TestScreen(Prototype prototype) {
		game = prototype;

		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		batch = new SpriteBatch();

		//create camera & viewport & stage
		playerCamera = new OrthographicCamera();
		viewport = new FitViewport(Prototype.VP_WIDTH, Prototype.VP_HEIGHT, playerCamera);

		//register actors to stage
		stage = new Stage(viewport);
		//stage.addActor(game.world);
		//is changing the order of adding actors also changes the act/drawing order?
		//stage.addActor(game.world.mainArea);
		// stage.addActor(game.world.area1);
		// stage.addActor(game.world.area2);
		// stage.addActor(game.world.area3);
		//stage.addActor(game.world.robot);
		//stage.addActor(game.world.player1);
		// stage.addActor(game.world.player2);
		// stage.addActor(game.world.player3);
		// stage.addActor(game.world.player4);
		//stage.setKeyboardFocus(game.world.robot); //sets keyboard focus on game.world.robot -> should change to stage?
		//stage.addListener(new UserInputListener(game.client));
		stage.addListener(new InputListener() {
			//Vector3 touchPoint = new Vector3();
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				//Gdx.app.log("TestScreen", "mouse clicked: " + button);
				//if player is robot and left mouse button is clicked
				if (game.playerNum == Prototype.PLAYER_ROBOT_NUM && button == 0)
					game.client.sendInput(MsgCodes.Game.ATTACK, MsgCodes.Game.KEY_DOWN);
				// if player is robot and right mouse button is clicked
				if (game.playerNum == Prototype.PLAYER_ROBOT_NUM && button == 1) {
					//touchPoint.set(x, y, 0);
					//Gdx.app.log("before fuck", "(" + touchPoint.x + ", " + touchPoint.y + ")");
					//playerCamera.unproject(touchPoint);
					//Gdx.app.log("fuck", "(" + touchPoint.x + ", " + touchPoint.y + ")");
					game.client.sendInput(MsgCodes.Game.GRAB, x, y, MsgCodes.Game.KEY_DOWN);
				}
				return true;
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
				case Keys.W:
					game.client.sendInput(MsgCodes.Game.UP, MsgCodes.Game.KEY_DOWN);
					return true;
				case Keys.A:
					game.client.sendInput(MsgCodes.Game.LEFT, MsgCodes.Game.KEY_DOWN);
					return true;
				case Keys.S:
					game.client.sendInput(MsgCodes.Game.DOWN, MsgCodes.Game.KEY_DOWN);
					return true;
				case Keys.D:
					game.client.sendInput(MsgCodes.Game.RIGHT, MsgCodes.Game.KEY_DOWN);
					return true;
				case Keys.E:
					Interactable obj = game.world.players[game.playerNum].getNearbyObject();
					if (obj != null) {
						game.client.sendInput(MsgCodes.Game.INTERACT, game.world.players[game.playerNum].getCurrentArea().getNum(), obj.getNum(), MsgCodes.Game.KEY_DOWN);
						return true;
					}
					else
						return false;
				case Keys.SHIFT_LEFT:
				case Keys.SHIFT_RIGHT:
					//robot
					if (game.playerNum == Prototype.PLAYER_ROBOT_NUM) {
						game.client.sendInput(MsgCodes.Game.RUSH, MsgCodes.Game.KEY_DOWN);
					}
					//player
					else
						game.client.sendInput(MsgCodes.Game.DODGE, MsgCodes.Game.KEY_DOWN);
					return true;
				default:
					return false;
				}
			}

			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				switch (keycode) {
				case Keys.W:
					game.client.sendInput(MsgCodes.Game.UP, MsgCodes.Game.KEY_UP);
					return true;
				case Keys.A:
					game.client.sendInput(MsgCodes.Game.LEFT, MsgCodes.Game.KEY_UP);
					return true;
				case Keys.S:
					game.client.sendInput(MsgCodes.Game.DOWN, MsgCodes.Game.KEY_UP);
					return true;
				case Keys.D:
					game.client.sendInput(MsgCodes.Game.RIGHT, MsgCodes.Game.KEY_UP);
					return true;
				case Keys.E:
					//if (game.world.players[game.playerNum].accessState("get", '0') == MsgCodes.Game.INTERACT_STATE) {
						game.client.sendInput(MsgCodes.Game.INTERACT, MsgCodes.Game.KEY_UP);
						return true;
					//}
					//return false;
				default:
					return false;
				}
			}
		});

		//remainTimeBox = new Image(game.world.assets.remainTimeBoxTexture);
		remainTimeLabel = new Label("0:00", game.world.assets.skin, "default");
		remainTimeLabel.setAlignment(Align.center);

		objectivesLabel = new Label("Objectives", game.world.assets.skin, "bold");
		//objectivesLabel.setAlignment(Align.topLeft);
		objective1Label = new Label("Find a card key", game.world.assets.skin, "default");
		//objective1Label.setAlignment(Align.topLeft);
		objective2Label = new Label("Escape the facility", game.world.assets.skin, "default");
		//objective2Label.setAlignment(Align.topLeft);

		objectivesLabel.setFontScale(0.5f, 0.5f);
		objective1Label.setFontScale(0.5f, 0.5f);
		objective2Label.setFontScale(0.5f, 0.5f);

		objectivesLabel.setHeight(objectivesLabel.getHeight() * 0.5f);
		objective1Label.setHeight(objective1Label.getHeight() * 0.5f);
		objective2Label.setHeight(objective2Label.getHeight() * 0.5f);

		messageLabel = new Label("", game.world.assets.skin, "default");
		messageLabel.setFontScale(0.5f, 0.5f);
		messageLabel.setAlignment(Align.bottomLeft);

		///portraitNormal = new Image(game.world.assets.portraitNormalTexture);
		player1State = new Image(game.world.assets.portraitNormalDrawable);
		player2State = new Image(game.world.assets.portraitNormalDrawable);
		player3State = new Image(game.world.assets.portraitNormalDrawable);
		player4State = new Image(game.world.assets.portraitNormalDrawable);

		playerPositions = new float[Prototype.NUM_PLAYERS][2];
	}

	@Override
	public void show() {
		game.world.assets.introBackground.loop(0.2f);
		//game.world.assets.introBackground.play(0.2f);
		stage.addActor(game.world);
		//stage.addActor(remainTimeBox);
		stage.addActor(remainTimeLabel);
		stage.addActor(objectivesLabel);
		stage.addActor(objective1Label);
		stage.addActor(objective2Label);
		stage.addActor(messageLabel);
		stage.addActor(player1State);
		stage.addActor(player2State);
		stage.addActor(player3State);
		stage.addActor(player4State);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 0); // clears the background(black)
		
		stage.act(delta);

		float[] robotPosition = game.world.robot.accessPosition("get", 0, 0); // [x, y]
		float[] player1Position = game.world.player1.accessPosition("get", 0, 0); // [x, y]
		float[] player2Position = game.world.player2.accessPosition("get", 0, 0); // [x, y]

		// System.out.println("robot: " + Arrays.toString(robotPosition));
		// System.out.println("player1: " + Arrays.toString(player1Position));

		//used for rendering robot's projectile
		float[] projectilePos = game.world.robot.accessProjectilePos("get", 0, 0);
		
		char robotState = game.world.robot.accessState("get", '0');
		char player1State = game.world.player1.accessState("get", '0');
		char player2State = game.world.player2.accessState("get", '0');

		String robotStateStr = codeToString(robotState);
		String player1StateStr = codeToString(player1State);
		String player2StateStr = codeToString(player2State);

		boolean player1HasKey = game.world.player1.accessHasKey("get", false);
		boolean player2HasKey = game.world.player2.accessHasKey("get", false);
		
		char robotDirection = game.world.robot.accessDirection("get", '0');
		char player1Direction = game.world.player1.accessDirection("get", '0');
		char player2Direction = game.world.player1.accessDirection("get", '0');

		String robotDirectionStr = codeToString(robotDirection);
		String player1DirectionStr = codeToString(player1Direction);
		String player2DirectionStr = codeToString(player2Direction);

		String[] nearbyObjects = new String[Prototype.NUM_PLAYERS];
		// String player1NearbyObj;
		// String player2NearbyObj;
		for (int i = 1; i < Prototype.NUM_PLAYERS; i++) {
			Interactable obj = game.world.players[i].getNearbyObject();
			if (obj == null)
				nearbyObjects[i] = "null";
				// player1NearbyObj = "null";
			else
				nearbyObjects[i] = ((Actor)obj).getName();
				// player1NearbyObj = ((Actor)game.world.player1.getNearbyObject()).getName();
		}

		playerPositions[0] = robotPosition;
		playerPositions[1] = player1Position;
		playerPositions[2] = player2Position;

		//update robot's attack bound rectangle's position
		//game.world.robot.updateAttackBound(robotPosition[0], robotPosition[1]);

		//checkHit(); //check if robot hits any player. It doesn't use robotPosition and playerPosition so it might not be accurate

		// sets the camera's position equal to player's position
		//playerCamera.position.set(robotPosition[0] + Prototype.CHAR_WIDTH/2, robotPosition[1] + Prototype.CHAR_HEIGHT/2, 0);
		
		//if player survived
		if (game.world.players[game.playerNum].accessState("get", '0') == MsgCodes.Game.EXIT_STATE) {
			//play animation
			// if (elapsed < 2)
			// 	elapsed += delta;
			// else {
				//change screen
				game.observerScreen.init();
				game.setScreen(game.observerScreen);
				//updateCamera(nextPlayer);
		}
		// if player is killed
		else if (game.world.players[game.playerNum].accessState("get", '0') == MsgCodes.Game.DEAD_STATE) {
			//play animation
			if (elapsed < 2)
				elapsed += delta;
			else {
			// change screen
				game.observerScreen.init();
				game.setScreen(game.observerScreen);
			// updateCamera(nextPlayer);
			}
		}
		playerCamera.position.set(playerPositions[game.playerNum][0] + Prototype.CHAR_WIDTH/2, playerPositions[game.playerNum][1] + Prototype.CHAR_HEIGHT/2, 0);
		playerCamera.update(); // update the camera

		adjustSounds();

		// // if player's nearby object is interactable
		// Interactable nearbyObj = game.world.players[game.playerNum].getNearbyObject();
		// if (nearbyObj != null && !nearbyObj.interacted() && !nearbyObj.isInteracting()) {
		// 	// shapeRenderer.begin(ShapeType.Filled);
		// 	// shapeRenderer.setColor(1, 1, 1, 1); // white
		// 	// // shapeRenderer.scale(2f, 2f, 1f);
		// 	// shapeRenderer.rect(((Actor) nearbyObj).getX() - 10, ((Actor) nearbyObj).getY() - 10, ((Actor) nearbyObj).getWidth() + 20, ((Actor) nearbyObj).getHeight() + 20);
		// 	// // shapeRenderer.scale(1f, 1f, 1f);
		// 	// shapeRenderer.end();
		// 	nearbyObj.setNearBy(true);
		// }

		//set ui
		//remainTime box
		//remainTimeBox.setPosition(playerCamera.position.x - remainTimeBox.getWidth() / 2, playerCamera.position.y + Prototype.VP_HEIGHT/2 - remainTimeBox.getHeight()/2);
		//remainTime label
		remainTimeLabel.setPosition(playerCamera.position.x - remainTimeLabel.getWidth()/2, playerCamera.position.y + Prototype.VP_HEIGHT/2 - remainTimeLabel.getHeight());
		//System.out.println(remainTimeLabel.getWidth() + ", " + remainTimeLabel.getHeight());
		int remainTimeMin = (int)((float)game.world.remainTime / 60000f);
		//System.out.println("remaining min: " + remainTimeMin);
		int remainTimeSec = ((int)((float)game.world.remainTime / 1000f) % 60);
		CharSequence remainTimeSecStr = String.valueOf(remainTimeSec);
		if (remainTimeSec < 10)
			remainTimeSecStr = "0" + remainTimeSecStr;
		if (remainTimeMin == 0)
			remainTimeLabel.setColor(Color.RED);
		remainTimeLabel.setText(String.valueOf(remainTimeMin) + ":" + remainTimeSecStr);
		//System.out.println(remainTimeLabel.getWidth() + ", " + remainTimeLabel.getHeight());

		//"objectives" label
		objectivesLabel.setPosition(playerCamera.position.x - Prototype.VP_WIDTH/2 + 10, playerCamera.position.y + Prototype.VP_HEIGHT/2 - objectivesLabel.getHeight());
		//objective1 label
		objective1Label.setPosition(objectivesLabel.getX(), objectivesLabel.getY() - objective1Label.getHeight());
		// objective2 label
		objective2Label.setPosition(objectivesLabel.getX(), objectivesLabel.getY() - objective2Label.getHeight()*2);
		
		//portraits & message label
		this.player1State.setPosition(playerCamera.position.x - Prototype.VP_WIDTH/2 + 16, playerCamera.position.y - Prototype.VP_HEIGHT/2 + 16);
		this.player2State.setPosition(this.player1State.getX() + this.player1State.getWidth(), this.player1State.getY());
		this.player3State.setPosition(this.player2State.getX() + this.player2State.getWidth(), this.player1State.getY());
		this.player4State.setPosition(this.player3State.getX() + this.player3State.getWidth(), this.player1State.getY());
		messageLabel.setPosition(playerCamera.position.x, this.player1State.getY());
		for (int i = 1; i != Prototype.NUM_PLAYERS; i++) {
			//update portrait image
			Player player = game.world.players[i];
			char state = player.accessState("get", '0');
			if (player.accessHasKey("get", false)) {
				player.setPortraitDrawable(game.world.assets.portraitKeyDrawable);
			}
			else if (state == MsgCodes.Game.DEAD_STATE) {
				player.setPortraitDrawable(game.world.assets.portraitKilledDrawable);
			}
			else
				player.setPortraitDrawable(game.world.assets.portraitNormalDrawable);
		}
		//update message text
		Player player = game.world.players[game.playerNum];
		char state = player.accessState("get", '0');
		if (player.getNearbyObject() instanceof BoxObject) {
			if (state == MsgCodes.Game.INTERACT_STATE)
				messageLabel.setText("searching...");
			else if (state == MsgCodes.Game.INTERACT_SUCCESS_STATE)
				messageLabel.setText("you found a card key");
			else if (state == MsgCodes.Game.INTERACT_FAILED_STATE) {
				messageLabel.setText("no item found");
			}
		}
		else if (state == MsgCodes.Game.NORMAL_STATE_MOVING)
			messageLabel.setText("");

		this.player1State.setDrawable(game.world.player1.getPortraitDrawable());
		this.player2State.setDrawable(game.world.player2.getPortraitDrawable());
		// this.player3State.setDrawable(game.world.player3.getPortraitDrawable());
		// this.player4State.setDrawable(game.world.player4.getPortraitDrawable());
		//draw world
		stage.draw();

		// shapeRenderer.setProjectionMatrix(playerCamera.combined);

		//render area1's position boundary
		//shapeRenderer.setProjectionMatrix(playerCamera.combined);
		//shapeRenderer.begin(ShapeType.Line);
		//shapeRenderer.setColor(0, 0, 1, 0); // blue
		//	shapeRenderer.rect(game.world.area1.getX(), game.world.area1.getY(), game.world.area1.getWidth(), game.world.area1.getHeight());
		//shapeRenderer.end();
		// render actual area1
		//shapeRenderer.begin(ShapeType.Line);
		//shapeRenderer.setColor(1, 1, 1, 0); // white
		//	shapeRenderer.rect(game.world.mainArea.getX(), game.world.mainArea.getY(), game.world.mainArea.getWidth(), game.world.mainArea.getHeight());
		//	shapeRenderer.rect(game.world.mainArea.getX()+256, game.world.mainArea.getY()+256, 632, 824);
		//shapeRenderer.end();
		//render objects in area1
		// shapeRenderer.begin(ShapeType.Filled);
		// 	for (Interactable obj : game.world.mainArea.getObjects()) {
		// 		if (obj != null && !(obj instanceof CardKeyObject)) {
		// 			if (obj.isInteracting())
		// 				shapeRenderer.setColor(0.8f, 1, 1, 0);
		// 			else if (obj.interacted())
		// 				shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0);
		// 			else
		// 				shapeRenderer.setColor(1, 1, 1, 0); //white
		// 			shapeRenderer.rect(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
		// 		}
		// 	}
		// shapeRenderer.end();

		// render world.area2's position boundary
		//shapeRenderer.setProjectionMatrix(playerCamera.combined);
		//shapeRenderer.begin(ShapeType.Line);
		//shapeRenderer.setColor(0, 0, 1, 0); // blue
		//shapeRenderer.rect(game.world.area2.getX(), game.world.area2.getY(), game.world.area2.getWidth(), game.world.area2.getHeight());
		//shapeRenderer.end();
		// render actual world.area2
		//shapeRenderer.setProjectionMatrix(playerCamera.combined);
		//shapeRenderer.begin(ShapeType.Line);
		//shapeRenderer.setColor(1, 1, 1, 0); // white
		//shapeRenderer.rect(game.world.area2.getX()+Prototype.CHAR_WIDTH, game.world.area2.getY(), game.world.area2.getWidth()-Prototype.CHAR_WIDTH, game.world.area2.getHeight() + Prototype.CHAR_HEIGHT);
		//shapeRenderer.end();

		// render area3's position boundary
		// shapeRenderer.setProjectionMatrix(playerCamera.combined);
		//shapeRenderer.begin(ShapeType.Line);
		//shapeRenderer.setColor(0, 0, 1, 0); // blue
		//shapeRenderer.rect(game.world.area3.getX(), game.world.area3.getY(), game.world.area3.getWidth(), game.world.area3.getHeight());
		//shapeRenderer.end();
		// render actual area3
		// shapeRenderer.setProjectionMatrix(playerCamera.combined);
		//shapeRenderer.begin(ShapeType.Line);
		//shapeRenderer.setColor(1, 1, 1, 0); // white
		//shapeRenderer.rect(game.world.area3.getX(), game.world.area3.getY(), game.world.area3.getWidth()+Prototype.CHAR_WIDTH, game.world.area3.getHeight()+Prototype.CHAR_HEIGHT);
		//shapeRenderer.end();

		//render robot
		// shapeRenderer.begin(ShapeType.Filled);
		// shapeRenderer.setColor(1, 0, 0, 0); //red
		// 	shapeRenderer.rect(robotPosition[0], robotPosition[1], Prototype.CHAR_WIDTH, Prototype.CHAR_HEIGHT);
		// shapeRenderer.end();
		
		shapeRenderer.setProjectionMatrix(playerCamera.combined);
		// render robot's attack rectangle
		if (game.playerNum == Prototype.PLAYER_ROBOT_NUM) {
			shapeRenderer.begin(ShapeType.Line);
			if (robotState == MsgCodes.Game.ATTACK_STATE || robotState == MsgCodes.Game.ATTACK_GRABBING_STATE)
				shapeRenderer.setColor(1, 1, 0, 0); //yellow
			else
				shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0); // grey
			shapeRenderer.rect(game.world.robot.attackBound.x, game.world.robot.attackBound.y,
							   game.world.robot.attackBound.width, game.world.robot.attackBound.height);
			shapeRenderer.end();
			//render robot's grab range circle
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0); //grey
				shapeRenderer.circle(robotPosition[0] + Prototype.CHAR_WIDTH/2, robotPosition[1] + Prototype.CHAR_HEIGHT/2, Prototype.PROJECTILE_DISTANCE);
			shapeRenderer.end();
		}
			//if robot's current state is grabbing, render robot's projectile -> move to Player
			if (robotState == MsgCodes.Game.GRABBING_STATE || robotState == MsgCodes.Game.ATTACK_GRABBING_STATE) {
				//Gdx.app.log("Projectile", "(" + projectilePos[0] + ", " + projectilePos[1] + ")");
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(1, 1, 1, 0); //white
					shapeRenderer.rect(projectilePos[0] - Prototype.PROJECTILE_WIDTH/2, projectilePos[1] - Prototype.PROJECTILE_HEIGHT/2, Prototype.PROJECTILE_WIDTH, Prototype.PROJECTILE_HEIGHT);
					shapeRenderer.line(robotPosition[0] + Prototype.CHAR_WIDTH/2, robotPosition[1] + Prototype.CHAR_HEIGHT/2, projectilePos[0], projectilePos[1]);
				shapeRenderer.end();
			}

		//render player1
		// shapeRenderer.begin(ShapeType.Filled);
		// if (player1State == MsgCodes.Game.DEAD_STATE)
		// 	shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0); //grey
		// else if (player1State == MsgCodes.Game.DRAGGED_STATE)
		// 	shapeRenderer.setColor(1, 0.8f, 0, 0); //yellow
		// else
		// 	shapeRenderer.setColor(0, 1, 0, 0); //green
		// 	shapeRenderer.rect(player1Position[0], player1Position[1], Prototype.CHAR_WIDTH, Prototype.CHAR_HEIGHT);
		// shapeRenderer.end();

		// //render game.world.player2
		// shapeRenderer.begin(ShapeType.Filled);
		// if (world.player2.isKilled()) shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0); //grey
		// else shapeRenderer.setColor(0, 1, 0, 0); //green
		// 	shapeRenderer.rect(world.player2.getX(), world.player2.getY(), Prototype.CHAR_WIDTH, Prototype.CHAR_HEIGHT);
		// shapeRenderer.end();

		// //render game.world.player3
		// shapeRenderer.begin(ShapeType.Filled);
		// if (world.player3.isKilled()) shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0); //grey
		// else shapeRenderer.setColor(0, 1, 0, 0); //green
		// 	shapeRenderer.rect(world.player3.getX(), world.player3.getY(), Prototype.CHAR_WIDTH, Prototype.CHAR_HEIGHT);
		// shapeRenderer.end();

		// //render game.world.player4
		// shapeRenderer.begin(ShapeType.Filled);
		// if (world.player4.isKilled()) shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0); //grey
		// else shapeRenderer.setColor(0, 1, 0, 0); //green
		// 	shapeRenderer.rect(world.player4.getX(), world.player4.getY(), Prototype.CHAR_WIDTH, Prototype.CHAR_HEIGHT);
		// shapeRenderer.end();
		
		//render card key
		// if (game.world.cardKey.getX() != -1f) {
		// 	//Gdx.app.log("TestScreen", "rendering card key: (" + game.world.cardKey.getX() + ", " + game.world.cardKey.getY() + ")");
		// 	// shapeRenderer.begin(ShapeType.Filled);
		// 	// shapeRenderer.setColor(1, 1, 1, 0);
		// 	// shapeRenderer.rect(game.world.cardKey.getX(), game.world.cardKey.getY(), game.world.cardKey.getWidth(), game.world.cardKey.getHeight());
		// 	// shapeRenderer.end();
		// 	game.world.cardKey.draw(batch, 0);
		// }
		
		//show camera's position & players' info & remaining time
		batch.setProjectionMatrix(playerCamera.combined);
		batch.begin();
			font.draw(batch, "camera pos: (" + playerCamera.position.x + ", " + playerCamera.position.y + ")", playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y - playerCamera.viewportHeight/2 + 11);
			//robot's info
			font.draw(batch, "robot's pos: (" + robotPosition[0] + ", " + robotPosition[1] + ")", playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2);
			font.draw(batch, "robot's current area: " + game.world.robot.getCurrentArea().getName(), playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 12);
			font.draw(batch, "robot's state: " + robotStateStr, playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 24);
			font.draw(batch, "robot's direction: " + robotDirectionStr, playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 36);
			
			//player1's info
			font.draw(batch, "player1's pos: (" + player1Position[0] + ", " + player1Position[1] + ")", playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 48);
			font.draw(batch, "player1's current area: " + game.world.player1.getCurrentArea().getName(), playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 60);
			font.draw(batch, "player1's state: " + player1StateStr, playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 72);
			font.draw(batch, "player1's direction: " + player1DirectionStr, playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 84);
			font.draw(batch, "player1's nearby object: " + nearbyObjects[Prototype.PLAYER1_NUM], playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 96);
			font.draw(batch, "player1's key possession: " + player1HasKey, playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 108);

			//player2's info
			font.draw(batch, "player2's pos: (" + player2Position[0] + ", " + player2Position[1] + ")", playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 120);
			font.draw(batch, "player2's current area: " + game.world.player2.getCurrentArea().getName(), playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 132);
			font.draw(batch, "player2's state: " + player2StateStr, playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 144);
			font.draw(batch, "player2's direction: " + player2DirectionStr, playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 156);
			font.draw(batch, "player2's nearby object: " + nearbyObjects[Prototype.PLAYER2_NUM], playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 168);
			font.draw(batch, "player2's key possession: " + player2HasKey, playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 180);
		
			//remaining time
			font.draw(batch, "remaining time: " + game.world.remainTime + "ms", playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 192);
		batch.end();

		// if player's nearby object is interactable
		Interactable nearbyObj = game.world.players[game.playerNum].getNearbyObject();
		if (nearbyObj != null && !nearbyObj.interacted()) {
			stage.getBatch().begin();
			stage.getBatch().draw(game.world.assets.interactableMark,
								  ((Actor)nearbyObj).getX() + ((Actor) nearbyObj).getWidth()/2 - 16,
								  ((Actor)nearbyObj).getY() + ((Actor) nearbyObj).getHeight());
			stage.getBatch().end();
			// Gdx.gl.glEnable(GL30.GL_BLEND);
			// Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
			// shapeRenderer.begin(ShapeType.Filled);
			// shapeRenderer.setColor(1, 1, 0, 0.5f);
			// // shapeRenderer.scale(2f, 2f, 1f);
			// shapeRenderer.rect(((Actor) nearbyObj).getX(), ((Actor)nearbyObj).getY(), ((Actor) nearbyObj).getWidth(), ((Actor)nearbyObj).getHeight());
			// // shapeRenderer.scale(1f, 1f, 1f);
			// shapeRenderer.end();
			// Gdx.gl.glDisable(GL30.GL_BLEND);
			//nearbyObj.setNearBy(true);
		}

		// //draw ui
		// remainTimeLabel.setPosition(playerCamera.position.x + Prototype.VP_WIDTH/2 - remainTimeLabel.getWidth()/2, playerCamera.position.y + Prototype.VP_HEIGHT - remainTimeLabel.getHeight());
		// int remainTimeMin = (int)((float)game.world.remainTime / 60000f);
		// int remainTimeSec = (int)((float)game.world.remainTime / 1000f);
		// remainTimeLabel.setText(String.valueOf(remainTimeMin) + ":" + String.valueOf(remainTimeSec));

		if (!game.sessionStart) {
			if (elapsed < 2) {
				elapsed += delta;
			}
			else {
				game.disconnect();
				game.resultScreen.init();
				game.setScreen(game.resultScreen);
			}
		}
	}

	private void adjustSounds() {
		float[] position;
		double distance;
		float volume;

		for (Player character : game.world.players) {
			position = character.accessPosition("get", 0, 0);
			distance = Math.sqrt(Math.pow(position[0] - playerCamera.position.x, 2) +
					   Math.pow(position[1] - playerCamera.position.y, 2));
			volume = (float)(1 - distance / Assets.AUDIBILITY_RANGE);
			character.setSoundVolume((volume < 0 ? 0 : volume));
		}
	}

	/**
	 * Checks robot hits any player.
	 * If the game.world.robot hit a player, that player's kill() is invoked and removed from the activePlayers array
	 */
	//private void checkHit() {
		// for (ArrayIterator<Player> iterator = game.world.activePlayers.iterator(); iterator.hasNext();) {
		// 	Player player = iterator.next();
		// 	if (player.isContact(game.world.robot)) {
		// 		player.kill();
		// 		iterator.remove();
		// 	}
		// }
	//}

	//temporary utility method
	private String codeToString(char code) {
		switch(code) {
			// case MsgCodes.Game.NORMAL_STATE:
			// 	return "normal";
			case MsgCodes.Game.NORMAL_STATE_STANDING:
				return "standing";
			case MsgCodes.Game.NORMAL_STATE_MOVING:
				return "moving";
			case MsgCodes.Game.DODGE_STATE:
				return "dodging";
			case MsgCodes.Game.ATTACK_STATE:
				return "attacking";
			case MsgCodes.Game.GRABBING_STATE:
				return "grabbing";
			case MsgCodes.Game.ATTACK_GRABBING_STATE:
				return "grabbing";
			case MsgCodes.Game.RUSH_STATE:
				return "rushing";
			case MsgCodes.Game.DRAGGED_STATE:
				return "dragged";
			case MsgCodes.Game.DEAD_STATE:
				return "dead";
			case MsgCodes.Game.INTERACT_STATE:
				return "interacting";
			case MsgCodes.Game.INTERACT_SUCCESS_STATE:
				return "interact success";
			case MsgCodes.Game.INTERACT_FAILED_STATE:
				return "interact failed";
			case MsgCodes.Game.EXIT_STATE:
				return "escaped";
			
			case MsgCodes.Game.DIRECTION_NORTH:
				return "north";
			case MsgCodes.Game.DIRECTION_NORTH_EAST:
				return "north-east";
			case MsgCodes.Game.DIRECTION_EAST:
				return "east";
			case MsgCodes.Game.DIRECTION_SOUTH_EAST:
				return "south-east";
			case MsgCodes.Game.DIRECTION_SOUTH:
				return "south";
			case MsgCodes.Game.DIRECTION_SOUTH_WEST:
				return "south-west";
			case MsgCodes.Game.DIRECTION_WEST:
				return "west";
			case MsgCodes.Game.DIRECTION_NORTH_WEST:
				return "north-west";

			default:
				return null;
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log("TestScreen", "resized to: " + width + ", " + height);
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		game.world.assets.introBackground.stop();
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		stage.dispose();
		batch.dispose();
		font.dispose();
		//world.dispose();
		Gdx.app.log("TestScreen", "disposed");
	}
	
}
