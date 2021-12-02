package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.MsgCodes;

public class IntroScreen implements Screen {
	private Prototype game;

	private Stage stage;
	private FitViewport viewport;
	private OrthographicCamera camera;

	private ShapeRenderer shapeRenderer;

	private Image matchInfo;
	private Image background;
	private Actor robot;
	private Label robotName;
	private Label player1Name;
	private Label player2Name;
	private Label player3Name;
	private Label player4Name;

	private float curtainY;

	private float elapsed;

	private Matrix4 matchInfoMatrix;

	private boolean ready;

	private boolean playRobotMalfunction = true;
	private boolean playRobotVoice = true;
	private boolean playAlarm = true;
	private boolean playRobotMove = true;

	IntroScreen(Prototype prototype) {
		game = prototype;

		camera = new OrthographicCamera();
		viewport = new FitViewport(176, 128, camera);
		stage = new Stage(viewport);

		matchInfoMatrix = camera.combined.cpy();
		matchInfoMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / (viewport.getWorldWidth()/viewport.getWorldHeight()));

		shapeRenderer = new ShapeRenderer();

		matchInfo = new Image(game.assets.introMatchInfoTexture) {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(matchInfoMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		matchInfo.setPosition(Gdx.graphics.getWidth()/2-matchInfo.getWidth()/2, Gdx.graphics.getHeight()/2-matchInfo.getHeight()/2);

		player1Name = new Label("player1", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(matchInfoMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};

		String playerName = game.world.player1.getName();
		player1Name.setText(playerName);
		player1Name.setFontScale(0.5f);
		player1Name.setWidth(64);
		player1Name.setWrap(true);
		player1Name.setPosition(matchInfo.getX()+22, matchInfo.getY()+70-player1Name.getHeight());

		player2Name = new Label("player2", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(matchInfoMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		playerName = game.world.player2.getName();
		player2Name.setText(playerName);
		player2Name.setFontScale(0.5f);
		player2Name.setWidth(64);
		player2Name.setWrap(true);
		player2Name.setPosition(player1Name.getX() + 64+5, matchInfo.getY() + 70 - player2Name.getHeight());

		player3Name = new Label("player3", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(matchInfoMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		playerName = game.world.player3.getName();
		player3Name.setText(playerName);
		player3Name.setFontScale(0.5f);
		player3Name.setWidth(64);
		player3Name.setWrap(true);
		player3Name.setPosition(player2Name.getX() + 64+5, matchInfo.getY() + 70 - player3Name.getHeight());

		player4Name = new Label("player4", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(matchInfoMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		playerName = game.world.player4.getName();
		player4Name.setText(playerName);
		player4Name.setFontScale(0.5f);
		player4Name.setWidth(64);
		player4Name.setWrap(true);
		player4Name.setPosition(player3Name.getX() + 64+5, matchInfo.getY() + 70 - player4Name.getHeight());

		robotName = new Label("robot", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(matchInfoMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		playerName = game.world.robot.getName();
		robotName.setText(playerName);
		robotName.setFontScale(0.5f);
		robotName.setWidth(64);
		robotName.setWrap(true);
		robotName.setPosition(matchInfo.getX()+492-22-64, matchInfo.getY() + 70 - robotName.getHeight());
		
		background = new Image(game.assets.introBackgroundTexture);
		background.setPosition(0, 0);

		robot = new Actor() {
			private TextureRegion curFrame;
			private float stateTime = 0;

			@Override
			public void act(float delta) {
				stateTime += delta;
				curFrame = game.assets.introRobotAnimation.getKeyFrame(stateTime);
				if (stateTime >= 11) {
					setX(getX() - 50 * delta);
				}
			}

			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.draw(curFrame, getX(), getY());
			}
		};
		robot.setBounds(64+32-16, 32+32-16, 32, 32);

		stage.addActor(background);
		stage.addActor(robot);
		stage.addActor(matchInfo);
		stage.addActor(player1Name);
		stage.addActor(player2Name);
		stage.addActor(player3Name);
		stage.addActor(player4Name);
		stage.addActor(robotName);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		elapsed = 0;
		curtainY = 0;
		game.assets.introBackground.play(0.3f);
	}

	@Override
	public void render(float delta) {
		elapsed += delta;

		if (elapsed >= 3) {
			matchInfo.setVisible(false);
			player1Name.setVisible(false);
			player2Name.setVisible(false);
			player3Name.setVisible(false);
			player4Name.setVisible(false);
			robotName.setVisible(false);
		}
		if (elapsed >= 5 && playRobotMalfunction) {
			playRobotMalfunction = false;
			game.assets.introRobotMalfunction.play();
		}
		if (elapsed >= 7 && playRobotVoice) {
			playRobotVoice = false;
			game.assets.introRobotVoice.play();
		}
		if (elapsed >= 10 && playAlarm) {
			playAlarm = false;
			game.assets.introAlarm.play(0.2f);
		}
		if (elapsed >= 11 && playRobotMove) {
			playRobotMove = false;
			game.world.robot.getSounds()[0].play();
		}
		if (elapsed >= 13) {
			if (!ready) {
				game.client.send(MsgCodes.MESSAGECODE, MsgCodes.Client.READY);
				ready = true;
			}
			if (game.gameStart) {
				game.setScreen(new TestScreen(game));
			}
		}

		ScreenUtils.clear(Color.BLACK);

		camera.position.set(64+32, 32+32, 0);
		camera.update();

		stage.act();
		game.assets.baseTexture.setRegion(0, 0, (int)viewport.getWorldWidth(), (int)viewport.getWorldHeight());
		stage.getBatch().begin();
		stage.getBatch().draw(game.assets.baseTexture, 0, 0);
		stage.getBatch().end();
		stage.draw();

		if (curtainY <= viewport.getWorldHeight()) {
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.BLACK);
				shapeRenderer.rect(0, curtainY, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			shapeRenderer.end();

			curtainY += viewport.getWorldHeight()*2 * delta;
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		Gdx.app.log("IntroScreen", "resized to: " + width + ", " + height);
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
		game.assets.introBackground.stop();
		dispose();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		shapeRenderer.dispose();
		Gdx.app.log("introScreen", "disposed");
	}
	
}
