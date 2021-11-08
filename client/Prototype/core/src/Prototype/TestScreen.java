package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.Client;
import net.MsgCodes;

public class TestScreen implements Screen {
	private Prototype game;

	private ShapeRenderer shapeRenderer; // temporary renderer to indicate boundary of the map
	private BitmapFont font; //is used to show camera's position
	private SpriteBatch batch; //is used to draw camera's position font
	private Stage stage; // map
	private FitViewport viewport;
	private OrthographicCamera playerCamera; // camera that follows the player

	private float[][] playerPositions;

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
		stage.addActor(game.world.robot);
		stage.addActor(game.world.player1);
		// stage.addActor(game.world.player2);
		// stage.addActor(game.world.player3);
		// stage.addActor(game.world.player4);
		//stage.addActor(game.world.area1);
		//stage.addActor(game.world.area2);
		//stage.addActor(game.world.area3);
		//stage.setKeyboardFocus(game.world.robot); //sets keyboard focus on game.world.robot -> should change to stage?
		//stage.addListener(new UserInputListener(game.client));
		stage.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
				case Keys.W:
					game.client.sendInput(MsgCodes.Client.UP, MsgCodes.Client.KEY_DOWN);
					return true;
				case Keys.A:
					game.client.sendInput(MsgCodes.Client.LEFT, MsgCodes.Client.KEY_DOWN);
					return true;
				case Keys.S:
					game.client.sendInput(MsgCodes.Client.DOWN, MsgCodes.Client.KEY_DOWN);
					return true;
				case Keys.D:
					game.client.sendInput(MsgCodes.Client.RIGHT, MsgCodes.Client.KEY_DOWN);
					return true;
				default:
					return false;
				}
			}

			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				switch (keycode) {
				case Keys.W:
					game.client.sendInput(MsgCodes.Client.UP, MsgCodes.Client.KEY_UP);
					return true;
				case Keys.A:
					game.client.sendInput(MsgCodes.Client.LEFT, MsgCodes.Client.KEY_UP);
					return true;
				case Keys.S:
					game.client.sendInput(MsgCodes.Client.DOWN, MsgCodes.Client.KEY_UP);
					return true;
				case Keys.D:
					game.client.sendInput(MsgCodes.Client.RIGHT, MsgCodes.Client.KEY_UP);
					return true;
				default:
					return false;
				}
			}
		});

		playerPositions = new float[Prototype.NUM_PLAYERS][2];
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 0); // clears the background(black)
		
		stage.act();
		stage.draw();

		float[] robotPosition = game.world.robot.accessPosition("get", 0, 0); // [x, y]
		float[] player1Position = game.world.player1.accessPosition("get", 0, 0); // [x, y]

		playerPositions[0] = robotPosition;
		playerPositions[1] = player1Position; 

		//checkHit(); //check if robot hits any player. It doesn't use robotPosition and playerPosition so it might not be accurate

		// sets the camera's position equal to player's position
		//playerCamera.position.set(robotPosition[0] + Prototype.CHAR_WIDTH/2, robotPosition[1] + Prototype.CHAR_HEIGHT/2, 0);
		playerCamera.position.set(playerPositions[game.playerNum][0] + Prototype.CHAR_WIDTH/2, playerPositions[game.playerNum][1] + Prototype.CHAR_HEIGHT/2, 0);
		playerCamera.update(); // update the camera

		shapeRenderer.setProjectionMatrix(playerCamera.combined);

		//render area1's position boundary
		//shapeRenderer.setProjectionMatrix(playerCamera.combined);
		//shapeRenderer.begin(ShapeType.Line);
		//shapeRenderer.setColor(0, 0, 1, 0); // blue
		//	shapeRenderer.rect(game.world.area1.getX(), game.world.area1.getY(), game.world.area1.getWidth(), game.world.area1.getHeight());
		//shapeRenderer.end();
		// render actual world.area1
		//shapeRenderer.setProjectionMatrix(playerCamera.combined);
		//shapeRenderer.begin(ShapeType.Line);
		//shapeRenderer.setColor(1, 1, 1, 0); // white
		//	shapeRenderer.rect(game.world.area1.getX(), game.world.area1.getY(), game.world.area1.getWidth()+Prototype.CHAR_WIDTH, game.world.area1.getHeight()+Prototype.CHAR_HEIGHT);
		//shapeRenderer.end();

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

		//render game.world.robot
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 0); //red
			shapeRenderer.rect(robotPosition[0], robotPosition[1], Prototype.CHAR_WIDTH, Prototype.CHAR_HEIGHT);
		shapeRenderer.end();

		//render game.world.player1
		shapeRenderer.begin(ShapeType.Filled);
		//if (game.world.player1.isKilled()) shapeRenderer.setColor(0.4f, 0.4f, 0.4f, 0); //grey
		//else
			shapeRenderer.setColor(0, 1, 0, 0); //green
			shapeRenderer.rect(player1Position[0], player1Position[1], Prototype.CHAR_WIDTH, Prototype.CHAR_HEIGHT);
		shapeRenderer.end();

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
		
		//show camera's position & players' position & players' current area
		batch.setProjectionMatrix(playerCamera.combined);
		batch.begin();
			font.draw(batch, "camera pos: (" + playerCamera.position.x + ", " + playerCamera.position.y + ")", playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y - playerCamera.viewportHeight/2 + 11);
			//robot's info
			font.draw(batch, "robot's pos: (" + robotPosition[0] + ", " + robotPosition[1] + ")", playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2);
			//font.draw(batch, "robot's current area: " + game.world.robot.getCurrentArea().getName(), playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 12);
			//player1's info
			font.draw(batch, "player1's pos: (" + player1Position[0] + ", " + player1Position[1] + ")", playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 24);
			//font.draw(batch, "player1's current area: " + game.world.player1.getCurrentArea().getName(), playerCamera.position.x - playerCamera.viewportWidth/2, playerCamera.position.y + playerCamera.viewportHeight/2 - 36);
		batch.end();

		if (!game.start) {
			game.disconnect();
		}
	}

	/**
	 * Checks robot hits any player.
	 * If the game.world.robot hit a player, that player's kill() is invoked and removed from the activePlayers array
	 */
	private void checkHit() {
		// for (ArrayIterator<Player> iterator = game.world.activePlayers.iterator(); iterator.hasNext();) {
		// 	Player player = iterator.next();
		// 	if (player.isContact(game.world.robot)) {
		// 		player.kill();
		// 		iterator.remove();
		// 	}
		// }
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
