package Prototype;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.MsgCodes;

public class ObserverScreen implements Screen {
	private Prototype game;

	private Stage stage;
	private FitViewport viewport;
	private OrthographicCamera playerCamera;

	private Label observingPlayerLabel;
	private Label instructionLabel;

	private int curPlayer;

	private float elapsed;
	
	public ObserverScreen(Prototype prototype) {
		this.game = prototype;

		playerCamera = new OrthographicCamera();
		viewport = new FitViewport(Prototype.VP_WIDTH, Prototype.VP_HEIGHT, playerCamera);

		stage = new Stage(viewport);
		//stage.addActor(game.world); -> testScreen doesn't render the world any more.
		stage.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch(keycode) {
					case Keys.TAB:
						// do {
							curPlayer = (curPlayer + 1) % Prototype.NUM_PLAYERS;
						// } while(game.world.players[curPlayer].accessState("get", '0') == MsgCodes.Game.EXIT_STATE);
							
						return true;
					default:
						return false;
				}
			}
		});

		observingPlayerLabel = new Label("Observing: player1", game.world.assets.skin, "default");
		instructionLabel = new Label("press 'tab' to see next player", game.world.assets.skin, "default");
		instructionLabel.setFontScale(0.5f, 0.5f);
	}

	public void init() { //needed?
		elapsed = 0;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		stage.addActor(game.world);
		stage.addActor(observingPlayerLabel);
		stage.addActor(instructionLabel);
		Gdx.input.setInputProcessor(stage);

		elapsed = 0;
	}

	@Override
	public void render(float delta) {
		if (!game.sessionStart) {
			if (elapsed < 2)
				elapsed += delta;
			else {
				game.disconnect();
				game.resultScreen.init();
				game.setScreen(game.resultScreen);
				return;
			}
		}
		// TODO Auto-generated method stub
		ScreenUtils.clear(0, 0, 0, 0); // clears the background(black)
		
		stage.act(delta);
		char playerState = game.world.players[curPlayer].accessState("get", '0');
		if (playerState == MsgCodes.Game.EXIT_STATE) {
			curPlayer = (curPlayer + 1) % Prototype.NUM_PLAYERS;
			return;
		}
		playerCamera.position.set(game.world.players[curPlayer].getX() + Prototype.CHAR_WIDTH/2, game.world.players[curPlayer].getY() + Prototype.CHAR_HEIGHT/2, 0);
		playerCamera.update(); // update the camera

		observingPlayerLabel.setPosition(playerCamera.position.x - Prototype.VP_WIDTH/2, playerCamera.position.y + Prototype.VP_HEIGHT/2 - observingPlayerLabel.getHeight());
		observingPlayerLabel.setText("Observing: " + game.world.players[curPlayer].getName());

		instructionLabel.setPosition(observingPlayerLabel.getX()+10, playerCamera.position.y - Prototype.VP_HEIGHT / 2);

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		Gdx.app.log("ObservingScreen", "resized to: " + width + ", " + height);
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		//stage.dispose();
		Gdx.app.log("ObserverScreen", "disposed");
	}
	
}
