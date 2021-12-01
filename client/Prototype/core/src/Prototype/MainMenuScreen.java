package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen implements Screen {
	final Prototype game;

	private Stage stage;
	private OrthographicCamera camera;
	private FitViewport viewport;

	private ShapeRenderer shapeRenderer;

	private Image mainMenuBackground;
	private Label title;
	private TextButton playButton;
	private TextButton okButton;
	private TextButton cancelButton;
	private Window inputPrompt;
	private TextField inputField;
	private Window sessionWaitWindow;
	private Label sessionWaitLabel;
	private TextButton cancelWaitButton;

	private float curtainY;

	public MainMenuScreen(final Prototype game) {
		this.game = game;
		curtainY = Prototype.VP_HEIGHT;

		camera = new OrthographicCamera();
		viewport = new FitViewport(600, 600, camera);
		stage = new Stage(viewport);

		shapeRenderer = new ShapeRenderer();

		mainMenuBackground = new Image(game.assets.mainMenuBackgroundTexture);
		mainMenuBackground.setPosition(0, 0);

		title = new Label("The Anomaly", game.assets.skin, "title");
		title.setAlignment(Align.center);
		title.setWidth(600);
		title.setWrap(true);
		title.setPosition(Gdx.graphics.getWidth()/2-title.getWidth()/2, 472-title.getHeight());

		playButton = new TextButton("Play", game.assets.skin, "default");
		playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.assets.click.play();
				inputPrompt.setVisible(true);
			}
		});
		playButton.setWidth(100);
		playButton.setPosition(Gdx.graphics.getWidth()/2-playButton.getWidth()/2, title.getHeight() + 100);

		inputPrompt = new Window("Enter your name", game.assets.skin, "default");
		inputPrompt.setSize(568, 100);
		inputPrompt.setPosition(Gdx.graphics.getWidth() / 2 - inputPrompt.getWidth() / 2, 0);
		inputPrompt.getTitleLabel().setWrap(true);
		inputPrompt.setVisible(false);

		inputField = new TextField("", game.assets.skin, "default");
		inputField.setSize(250, 80);
		inputField.setMaxLength(12);

		okButton = new TextButton("OK", game.assets.skin, "default");
		okButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.assets.click.play();
				String name = inputField.getText();
				if (name.length() != 0)
					game.playerName = name;
				//game.setScreen(game.waitScreen);
				game.world = new World(game);
				game.connect();
				inputPrompt.setVisible(false);
				sessionWaitWindow.setVisible(true);
			}
		});

		cancelButton = new TextButton("Cancel", game.assets.skin, "default");
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.assets.click.play();
				inputPrompt.setVisible(false);
			}
		});
		//inputPrompt.add().setActor(inputField);
		inputPrompt.add(inputField).width(250);
		inputPrompt.add(okButton).width(60).padLeft(10);
		inputPrompt.add(cancelButton).width(150).padLeft(10);
		//inputPrompt.addActor(inputField);

		sessionWaitWindow = new Window("", game.assets.skin, "default");
		sessionWaitWindow.setSize(568, 100);
		sessionWaitWindow.setPosition(Gdx.graphics.getWidth() / 2 - inputPrompt.getWidth() / 2, 0);
		sessionWaitWindow.setVisible(false);

		sessionWaitLabel = new Label("searching for players.", game.assets.skin, "default");
		RepeatAction loop = new RepeatAction();
		loop.setCount(RepeatAction.FOREVER);
		loop.setAction(new Action() {
			float stateTime = 0;

			@Override
			public boolean act(float delta) {
				stateTime = (stateTime + delta) % 3f;
				if (stateTime < 1)
					sessionWaitLabel.setText("searching for players.");
				else if (stateTime < 2)
					sessionWaitLabel.setText("searching for players..");
				else if (stateTime < 3)
					sessionWaitLabel.setText("searching for players...");

				return true;
			}
		});
		sessionWaitLabel.addAction(loop);
		sessionWaitLabel.setAlignment(Align.center);
		sessionWaitLabel.setFontScale(0.7f);
		sessionWaitLabel.setWrap(true);

		cancelWaitButton = new TextButton("Cancel", game.assets.skin, "default");
		cancelWaitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.assets.click.play();
				game.disconnect();
				sessionWaitWindow.setVisible(false);
			}
		});

		sessionWaitWindow.add(sessionWaitLabel).expandX();
		sessionWaitWindow.add(cancelWaitButton).width(150);
		
		stage.addActor(mainMenuBackground);
		stage.addActor(title);
		stage.addActor(playButton);
		stage.addActor(inputPrompt);
		stage.addActor(sessionWaitWindow);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		game.assets.mainMenuMusic.play();
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		stage.act();
		game.assets.baseTexture.setRegion(0, 0, 568, 472);
		stage.getBatch().begin();
		stage.getBatch().draw(game.assets.baseTexture, 16, 64);
		stage.getBatch().end();
		stage.draw();

		if (game.sessionStart) {
			sessionWaitWindow.setVisible(false);
			if (curtainY >= 0) {
				shapeRenderer.setProjectionMatrix(camera.combined);
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(Color.BLACK);
				shapeRenderer.rect(0, curtainY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				shapeRenderer.end();

				curtainY -= Prototype.VP_HEIGHT*2 * delta;

				return;
			}
			Gdx.app.log("MainMenuScreen", "session starting...");
			// game.setScreen(game.testScreen);
			//game.introScreen.init();
			//game.setScreen(game.introScreen);
			game.setScreen(new IntroScreen(game));
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		Gdx.app.log("MainMenuScreen", "resized to: " + width + ", " + height);
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
		game.assets.mainMenuMusic.stop();
		dispose();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		Gdx.app.log("MainMenuScreen", "disposed");
	}
	
}
