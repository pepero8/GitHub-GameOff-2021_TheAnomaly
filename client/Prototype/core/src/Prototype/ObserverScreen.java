package Prototype;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.MsgCodes;

public class ObserverScreen implements Screen {
	private Prototype game;

	private Stage stage;
	private FitViewport viewport;
	private OrthographicCamera playerCamera;

	private int curPlayer;
	
	public ObserverScreen(Prototype prototype) {
		this.game = prototype;

		playerCamera = new OrthographicCamera();
		viewport = new FitViewport(Prototype.VP_WIDTH, Prototype.VP_HEIGHT, playerCamera);

		stage = new Stage(viewport);
		//stage.addActor(game.world);
		stage.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch(keycode) {
					case Keys.TAB:
						// do {
						// 	curPlayer = (curPlayer + 1) % game.world.players.length;
						// } while(game.world.players[curPlayer].accessState("get", '0') == MsgCodes.Game.EXIT_STATE);
							
						return true;
					default:
						return false;
				}
			}
		});
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		stage.addActor(game.world);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		if (!game.start) {
			game.disconnect();
		}
		// TODO Auto-generated method stub
		ScreenUtils.clear(0, 0, 0, 0); // clears the background(black)
		
		stage.act(delta);
		char playerState = game.world.players[curPlayer].accessState("get", '0');
		if (playerState == MsgCodes.Game.EXIT_STATE) {
			curPlayer = (curPlayer + 1) % game.world.players.length;
			return;
		}
		playerCamera.position.set(game.world.players[curPlayer].getX() + Prototype.CHAR_WIDTH/2, game.world.players[curPlayer].getY() + Prototype.CHAR_HEIGHT/2, 0);
		playerCamera.update(); // update the camera
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		Gdx.app.log("TestScreen", "resized to: " + width + ", " + height);
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
