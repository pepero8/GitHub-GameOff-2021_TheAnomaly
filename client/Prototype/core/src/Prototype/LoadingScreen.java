package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LoadingScreen implements Screen {

	final Prototype game;

	private Stage stage;
	private OrthographicCamera camera;
	private FitViewport viewport;

	public LoadingScreen(final Prototype game) {
		this.game = game;

		camera = new OrthographicCamera();
		viewport = new FitViewport(Prototype.VP_WIDTH, Prototype.VP_HEIGHT, camera);
		stage = new Stage(viewport);
	}

	@Override
	public void show() {
		game.connect();
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.2588f, 0.2588f, 0.9059f, 0);

		stage.act();
		stage.draw();

		if (game.start) {
			Gdx.app.log("LoadingScreen", "game starting...");
			game.setScreen(game.testScreen);
		}
	}

	@Override
	public void resize(int width, int height) {
		
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
		stage.dispose();
		Gdx.app.log("LoadingScreen", "disposed");
	}
}