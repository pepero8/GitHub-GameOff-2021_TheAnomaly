package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.MsgCodes;

public class ResultScreen implements Screen {
	private Prototype game;
	private char[] playerStates;

	private Stage stage;
	private FitViewport viewport;
	private OrthographicCamera camera;

	private Image resultWindow;
	private Label resultTitle;
	private Label resultMessage;
	private Label player1Name;
	private Label player2Name;
	private Label player3Name;
	private Label player4Name;
	private Label player1State;
	private Label player2State;
	private Label player3State;
	private Label player4State;

	private TextButton okButton;

	private float elapsed;

	private boolean playResultWindow = true;

	ResultScreen(Prototype prototype) {
		this.game = prototype;
		playerStates = new char[Prototype.NUM_PLAYERS - 1];
		elapsed = 0;

		camera = new OrthographicCamera();
		viewport = new FitViewport(Prototype.VP_WIDTH, Prototype.VP_HEIGHT, camera);

		stage = new Stage(viewport);

		resultWindow = new Image(game.assets.resultWindowTexture);
		resultWindow.setPosition(64, 600);
		resultWindow.addAction(Actions.moveTo(64, 64, 1, Interpolation.exp5Out));

		resultTitle = new Label("RESULT", game.assets.skin, "bold");
		resultTitle.setColor(0.2f, 0.24f, 0.34f, 1);
		resultTitle.setPosition(resultWindow.getX()+12, 64+470-resultTitle.getHeight()-12);
		resultTitle.setWidth(470);
		resultTitle.setAlignment(Align.center);
		resultTitle.setWrap(true);

		resultMessage = new Label("You failed to eliminate all the survivors", game.assets.skin, "xp");
		resultMessage.setHeight(resultTitle.getHeight() + 16);
		resultMessage.setPosition(resultTitle.getX(), resultTitle.getY() - resultMessage.getHeight());
		resultMessage.setWidth(resultTitle.getWidth() - 24);
		resultMessage.setAlignment(Align.center);
		resultMessage.setFontScale(0.7f, 0.7f);
		resultMessage.setWrap(true);

		player1Name = new Label("player1", game.assets.skin, "default");
		player2Name = new Label("player2", game.assets.skin, "default");
		player3Name = new Label("player3", game.assets.skin, "default");
		player4Name = new Label("player4", game.assets.skin, "default");

		player1Name.setPosition(resultTitle.getX()+32, resultMessage.getY()-player1Name.getHeight());
		player2Name.setPosition(resultTitle.getX()+32, player1Name.getY()-player2Name.getHeight());
		player3Name.setPosition(resultTitle.getX()+32, player2Name.getY()-player3Name.getHeight());
		player4Name.setPosition(resultTitle.getX()+32, player3Name.getY()-player4Name.getHeight());
		player1Name.setWidth((resultTitle.getWidth() - 64) / 2);
		player2Name.setWidth((resultTitle.getWidth() - 64) / 2);
		player3Name.setWidth((resultTitle.getWidth() - 64) / 2);
		player4Name.setWidth((resultTitle.getWidth() - 64) / 2);
		player1Name.setWrap(true);
		player2Name.setWrap(true);
		player3Name.setWrap(true);
		player4Name.setWrap(true);

		player1State = new Label("ESCAPED", game.assets.skin, "default");
		player2State = new Label("ESCAPED", game.assets.skin, "default");
		player3State = new Label("ESCAPED", game.assets.skin, "default");
		player4State = new Label("ESCAPED", game.assets.skin, "default");

		player1State.setPosition(player1Name.getX() + player1State.getWidth(), player1Name.getY());
		player2State.setPosition(player1Name.getX() + player1State.getWidth(), player2Name.getY());
		player3State.setPosition(player1Name.getX() + player1State.getWidth(), player3Name.getY());
		player4State.setPosition(player1Name.getX() + player1State.getWidth(), player4Name.getY());
		player1State.setWidth((resultTitle.getWidth() - 64) / 2);
		player2State.setWidth((resultTitle.getWidth() - 64) / 2);
		player3State.setWidth((resultTitle.getWidth() - 64) / 2);
		player4State.setWidth((resultTitle.getWidth() - 64) / 2);
		player1State.setWrap(true);
		player2State.setWrap(true);
		player3State.setWrap(true);
		player4State.setWrap(true);
		player1State.setAlignment(Align.right);
		player2State.setAlignment(Align.right);
		player3State.setAlignment(Align.right);
		player4State.setAlignment(Align.right);

		okButton = new TextButton("OK", game.assets.skin, "default");
		okButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//world dispose
				game.world.dispose();
				game.init();
				game.setScreen(new MainMenuScreen(game));
			}
		});
		okButton.setWidth(65);
		okButton.setPosition(resultWindow.getX()+resultWindow.getWidth()/2 - okButton.getWidth()/2, 64 + 16);
		

		stage.addActor(resultWindow);
		stage.addActor(resultTitle);
		stage.addActor(resultMessage);
		stage.addActor(player1Name);
		stage.addActor(player2Name);
		stage.addActor(player3Name);
		stage.addActor(player4Name);
		stage.addActor(player1State);
		stage.addActor(player2State);
		stage.addActor(player3State);
		stage.addActor(player4State);
		stage.addActor(okButton);

		resultTitle.setVisible(false);
		resultMessage.setVisible(false);
		player1Name.setVisible(false);
		player2Name.setVisible(false);
		player3Name.setVisible(false);
		player4Name.setVisible(false);
		player1State.setVisible(false);
		player2State.setVisible(false);
		player3State.setVisible(false);
		player4State.setVisible(false);
		okButton.setVisible(false);

		init();
	}

	public void init() {
		for (int i = 1; i != Prototype.NUM_PLAYERS; i++) {
			playerStates[i - 1] = game.world.players[i].accessState("get", '0');
		}

		resultMessage.setText(getResultMessage(game.world.players[game.playerNum]));

		player1Name.setText(game.world.player1.getName());
		player2Name.setText(game.world.player2.getName());
		player3Name.setText(game.world.player3.getName());
		player4Name.setText(game.world.player4.getName());

		player1State.setText(getStateText(playerStates[0]));
		player2State.setText(getStateText(playerStates[1]));
		player3State.setText(getStateText(playerStates[2]));
		player4State.setText(getStateText(playerStates[3]));

		player1State.setColor(getStateColor(playerStates[0]));
		player2State.setColor(getStateColor(playerStates[1]));
		player3State.setColor(getStateColor(playerStates[2]));
		player4State.setColor(getStateColor(playerStates[3]));

		elapsed = 0;
	}

	private String getResultMessage(Player player) {
		if (game.playerNum == Prototype.PLAYER_ROBOT_NUM) {
			switch (game.gameEndCode) {
				case MsgCodes.Server.SESSION_TERMINATE_GAMEOVER_ROBOT_WIN:
					return "You eliminated all the survivors";
				case MsgCodes.Server.SESSION_TERMINATE_GAMEOVER_SURVIVORS_WIN:
					resultMessage.setColor(Color.BLACK);
					return "You failed to eliminate all the survivors";
				default:
					return "";
			}
		}
		else {
			if (game.world.robot.disconnected)
				return "Robot disconnected from the server";
			char stateCode = player.accessState("get", '0');
			switch (stateCode) {
				case MsgCodes.Game.EXIT_STATE:
					return "Congratulations, you managed to escape!";
				case MsgCodes.Game.DEAD_STATE:
					resultMessage.setColor(Color.RED);
					return "You were killed by " + game.world.robot.getName();
				default:
					resultMessage.setColor(Color.BLACK);
					return "You failed to escape";
			}
		}
	}

	private String getStateText(char stateCode) {
		switch (stateCode) {
			case MsgCodes.Game.DEAD_STATE:
				return "DEAD";
			case MsgCodes.Game.EXIT_STATE:
				return "ESCAPED";
			default:
				return "FAILED";
		}
	}

	private Color getStateColor(char stateCode) {
		switch (stateCode) {
			case MsgCodes.Game.DEAD_STATE:
				return Color.RED;
			case MsgCodes.Game.EXIT_STATE:
				return Color.GREEN;
			default:
				return Color.DARK_GRAY;
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		ScreenUtils.clear(0.2f, 0.24f, 0.34f, 1);

		elapsed += delta;

		if (elapsed < 0.5f);
		else if (elapsed < 1.5f) {
			resultWindow.act(delta);
			if (playResultWindow) {
				playResultWindow = false;
				game.assets.resultWindowDown.play();
			}
		}
		else {
			if (elapsed >= 2) {
				resultTitle.setVisible(true);
			}
			if (elapsed >= 2.5f && !player1Name.isVisible() && !player1State.isVisible()) {
				player1Name.setVisible(true);
				player1State.setVisible(true);
				game.assets.resultPop.play();
			}
			if (elapsed >= 3.0f && !player2Name.isVisible() && !player2State.isVisible()) {
				player2Name.setVisible(true);
				player2State.setVisible(true);
				game.assets.resultPop.play();
			}
			if (elapsed >= 3.5f && !player3Name.isVisible() && !player3State.isVisible()) {
				player3Name.setVisible(true);
				player3State.setVisible(true);
				game.assets.resultPop.play();
			}
			if (elapsed >= 4.0f && !player4Name.isVisible() && !player4State.isVisible()) {
				player4Name.setVisible(true);
				player4State.setVisible(true);
				game.assets.resultPop.play();
			}
			if (elapsed >= 4.5f && !resultMessage.isVisible() && !okButton.isVisible()) {
				game.assets.resultMessage.play();
				resultMessage.setVisible(true);
				okButton.setVisible(true);
			}

			stage.act();
		}

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		Gdx.app.log("ResultScreen", "resized to: " + width + ", " + height);
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
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		// TODO Auto-generated method stub
		Gdx.app.log("ResultScreen", "disposed");
	}
	
}
