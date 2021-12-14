/**
 *	Copyright 2021 Jaehwan Lee

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	<http://www.apache.org/licenses/LICENSE-2.0>
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package Prototype;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.MsgCodes;

public class TestScreen implements Screen {
	private Prototype game;

	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private SpriteBatch batch;
	private Stage stage;
	private FitViewport viewport;
	private OrthographicCamera playerCamera; // camera following the player

	private Matrix4 uiMatrix;

	//ui components
	private Label remainTimeLabel;
	private Label objectivesLabel;
	private Label objective1Label;
	private Label objective2Label;
	private Label messageLabel;
	private Image survivorPortraits;
	private Image player1State;
	private Image player2State;
	private Image player3State;
	private Image player4State;
	private Label player1Name;
	private Label player2Name;
	private Label player3Name;
	private Label player4Name;

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

		uiMatrix = playerCamera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / (viewport.getWorldWidth()/viewport.getWorldHeight()));
		
		//register actors to stage
		stage = new Stage(viewport);
		stage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub

				//if player is robot and left mouse button is clicked
				if (game.playerNum == Prototype.PLAYER_ROBOT_NUM && button == 0)
					game.client.sendInput(MsgCodes.Game.ATTACK, MsgCodes.Game.KEY_DOWN);
				// if player is robot and right mouse button is clicked
				if (game.playerNum == Prototype.PLAYER_ROBOT_NUM && button == 1) {
					game.client.sendInput(MsgCodes.Game.GRAB, x, y, MsgCodes.Game.KEY_DOWN);
				}
				return true;
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				switch (keycode) {
				case Keys.UP:
				case Keys.W:
					game.client.sendInput(MsgCodes.Game.UP, MsgCodes.Game.KEY_DOWN);
					return true;
				case Keys.LEFT:
				case Keys.A:
					game.client.sendInput(MsgCodes.Game.LEFT, MsgCodes.Game.KEY_DOWN);
					return true;
				case Keys.DOWN:
				case Keys.S:
					game.client.sendInput(MsgCodes.Game.DOWN, MsgCodes.Game.KEY_DOWN);
					return true;
				case Keys.RIGHT:
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
				case Keys.UP:
				case Keys.W:
					game.client.sendInput(MsgCodes.Game.UP, MsgCodes.Game.KEY_UP);
					return true;
				case Keys.LEFT:
				case Keys.A:
					game.client.sendInput(MsgCodes.Game.LEFT, MsgCodes.Game.KEY_UP);
					return true;
				case Keys.DOWN:
				case Keys.S:
					game.client.sendInput(MsgCodes.Game.DOWN, MsgCodes.Game.KEY_UP);
					return true;
				case Keys.RIGHT:
				case Keys.D:
					game.client.sendInput(MsgCodes.Game.RIGHT, MsgCodes.Game.KEY_UP);
					return true;
				case Keys.E:
						game.client.sendInput(MsgCodes.Game.INTERACT, MsgCodes.Game.KEY_UP);
						return true;
				default:
					return false;
				}
			}
		});

		//ui components
		remainTimeLabel = new Label("0:00", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		remainTimeLabel.setAlignment(Align.center);

		objectivesLabel = new Label("Objectives", game.assets.skin, "bold") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		objective1Label = new Label("Find a card key", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		objective2Label = new Label("Escape the facility", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};

		objectivesLabel.setFontScale(0.5f, 0.5f);
		objective1Label.setFontScale(0.5f, 0.5f);
		objective2Label.setFontScale(0.5f, 0.5f);

		objectivesLabel.setHeight(objectivesLabel.getHeight() * 0.5f);
		objective1Label.setHeight(objective1Label.getHeight() * 0.5f);
		objective2Label.setHeight(objective2Label.getHeight() * 0.5f);

		messageLabel = new Label("", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		messageLabel.setFontScale(0.5f, 0.5f);
		messageLabel.setAlignment(Align.bottomLeft);

		
		player1State = new Image(game.assets.portraitNormalDrawable) {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		player2State = new Image(game.assets.portraitNormalDrawable) {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		player3State = new Image(game.assets.portraitNormalDrawable) {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		player4State = new Image(game.assets.portraitNormalDrawable) {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};

		//player1 name
		player1Name = new Label("player1", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		player1Name.setText(game.world.player1.getName());
		player1Name.setColor(Color.RED);
		player1Name.setFontScale(0.5f);
		player1Name.setWidth(64);
		player1Name.setWrap(true);
		player1Name.setPosition(16, 16);
		//player2 name
		player2Name = new Label("player2", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		player2Name.setText(game.world.player2.getName());
		player2Name.setColor(Color.YELLOW);
		player2Name.setFontScale(0.5f);
		player2Name.setWidth(64);
		player2Name.setWrap(true);
		player2Name.setPosition(player1Name.getX()+64, 16);
		// player3 name
		player3Name = new Label("player3", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		player3Name.setText(game.world.player3.getName());
		player3Name.setColor(Color.BROWN);
		player3Name.setFontScale(0.5f);
		player3Name.setWidth(64);
		player3Name.setWrap(true);
		player3Name.setPosition(player2Name.getX() + 64, 16);
		// player4 name
		player4Name = new Label("player4", game.assets.skin, "default") {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		player4Name.setText(game.world.player4.getName());
		player4Name.setColor(Color.GREEN);
		player4Name.setFontScale(0.5f);
		player4Name.setWidth(64);
		player4Name.setWrap(true);
		player4Name.setPosition(player3Name.getX() + 64, 16);

		//portraits
		survivorPortraits = new Image(game.assets.portraitsTexture) {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setProjectionMatrix(uiMatrix);
				super.draw(batch, parentAlpha);
				batch.setProjectionMatrix(getStage().getCamera().combined);
			}
		};
		survivorPortraits.setPosition(16, player1Name.getY() + player1Name.getHeight());

		init();
	}

	public void init() {
		//change viewport size according to player number
		if (game.playerNum != Prototype.PLAYER_ROBOT_NUM) {
			viewport.setWorldWidth(Prototype.VP_WIDTH_SURVIVOR);
			viewport.setWorldHeight(Prototype.VP_HEIGHT_SURVIVOR);
		}
		else if (game.playerNum == Prototype.PLAYER_ROBOT_NUM) {
			objective1Label.setText("Eliminate all the survivors");
			objective2Label.setText(null);
		}
	}

	@Override
	public void show() {
		game.assets.introBackground.loop(0.2f);
		stage.addActor(game.world);
		stage.addActor(remainTimeLabel);
		stage.addActor(objectivesLabel);
		stage.addActor(objective1Label);
		stage.addActor(objective2Label);
		stage.addActor(messageLabel);
		stage.addActor(survivorPortraits);
		stage.addActor(player1State);
		stage.addActor(player2State);
		stage.addActor(player3State);
		stage.addActor(player4State);
		stage.addActor(player1Name);
		stage.addActor(player2Name);
		stage.addActor(player3Name);
		stage.addActor(player4Name);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		ScreenUtils.clear(0, 0, 0, 0); // clears the background(black)
		
		stage.act(delta);

		float[] robotPosition = game.world.robot.accessPosition("get", 0, 0); // [x, y]

		// System.out.println("===============================");
		// for (Player player : game.world.players) {
		// 	float[] position = player.accessPosition("get", 0, 0);
		// 	System.out.println("player" + player.playerNum + " : (" + position[0] + ", " + position[1] + ")");
		// }
		// System.out.println("===============================");

		//used for rendering robot's projectile
		float[] projectilePos = game.world.robot.accessProjectilePos("get", 0, 0);
		
		char robotState = game.world.robot.accessState("get", '0');
		
		//if player survived
		if (game.world.players[game.playerNum].accessState("get", '0') == MsgCodes.Game.EXIT_STATE) {
			//change screen
			game.setScreen(new ObserverScreen(game));
		}
		// if player is killed
		else if (game.world.players[game.playerNum].accessState("get", '0') == MsgCodes.Game.DEAD_STATE) {
			//play animation
			if (elapsed < 2)
				elapsed += delta;
			else {
				// change screen
				game.setScreen(new ObserverScreen(game));
			}
		}
		
		//update camera
		float[] playerInControlPos = game.world.players[game.playerNum].accessPosition("get", 0, 0);
		playerCamera.position.set(playerInControlPos[0] + Prototype.CHAR_WIDTH/2, playerInControlPos[1] + Prototype.CHAR_HEIGHT/2, 0);
		playerCamera.update(); // update the camera

		adjustSounds();

		//set ui
		//remainTime label
		remainTimeLabel.setPosition(screenWidth/2 - remainTimeLabel.getWidth()/2, screenHeight - remainTimeLabel.getHeight());
		int remainTimeMin = (int)((float)game.world.remainTime / 60000f);
		int remainTimeSec = ((int)((float)game.world.remainTime / 1000f) % 60);
		CharSequence remainTimeSecStr = String.valueOf(remainTimeSec);
		if (remainTimeSec < 10)
			remainTimeSecStr = "0" + remainTimeSecStr;
		if (remainTimeMin == 0)
			remainTimeLabel.setColor(Color.RED);
		remainTimeLabel.setText(String.valueOf(remainTimeMin) + ":" + remainTimeSecStr);

		//"objectives" label
		objectivesLabel.setPosition(10, screenHeight - objectivesLabel.getHeight());
		//objective1 label
		objective1Label.setPosition(objectivesLabel.getX(), objectivesLabel.getY() - objective1Label.getHeight());
		// objective2 label
		objective2Label.setPosition(objectivesLabel.getX(), objectivesLabel.getY() - objective2Label.getHeight()*2);
		
		//portraits & message label
		this.player1State.setPosition(16, player1Name.getY()+player1Name.getHeight());
		this.player2State.setPosition(this.player1State.getX() + this.player1State.getWidth(), this.player1State.getY());
		this.player3State.setPosition(this.player2State.getX() + this.player2State.getWidth(), this.player1State.getY());
		this.player4State.setPosition(this.player3State.getX() + this.player3State.getWidth(), this.player1State.getY());

		messageLabel.setPosition(screenWidth/2, this.player1State.getY());
		for (int i = 1; i != Prototype.NUM_PLAYERS; i++) {
			//update portrait image
			Player player = game.world.players[i];
			char state = player.accessState("get", '0');
			if (state == MsgCodes.Game.EXIT_STATE) {
				player.setPortraitDrawable(game.assets.portraitEscapedDrawable);
			}
			else if (game.playerNum != Prototype.PLAYER_ROBOT_NUM && player.accessHasKey("get", false)) {
				player.setPortraitDrawable(game.assets.portraitKeyDrawable);
			}
			else if (state == MsgCodes.Game.DEAD_STATE) {
				if (player.disconnected)
					player.setPortraitDrawable(game.assets.portraitDisconnectedDrawable);
				else
					player.setPortraitDrawable(game.assets.portraitKilledDrawable);
			}
			else
				player.setPortraitDrawable(game.assets.portraitNormalDrawable);
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
		else if (player.getNearbyObject() instanceof GateObject) {
			if (state == MsgCodes.Game.INTERACT_FAILED_STATE) {
				messageLabel.setText("you need a card key");
			}
		}
		else if (state == MsgCodes.Game.NORMAL_STATE_MOVING)
			messageLabel.setText("");

		this.player1State.setDrawable(game.world.player1.getPortraitDrawable());
		this.player2State.setDrawable(game.world.player2.getPortraitDrawable());
		this.player3State.setDrawable(game.world.player3.getPortraitDrawable());
		this.player4State.setDrawable(game.world.player4.getPortraitDrawable());
		
		//draw world
		stage.draw();
		
		shapeRenderer.setProjectionMatrix(playerCamera.combined);
		//if robot's current state is grabbing, render robot's projectile -> move to Player
		if (robotState == MsgCodes.Game.GRABBING_STATE || robotState == MsgCodes.Game.ATTACK_GRABBING_STATE) {
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.BLACK);
				shapeRenderer.rectLine(robotPosition[0] + Prototype.CHAR_WIDTH/2-2, robotPosition[1] + Prototype.CHAR_HEIGHT/2-2, projectilePos[0], projectilePos[1], 4);
			shapeRenderer.end();
			stage.getBatch().begin();
			stage.getBatch().draw(game.assets.projectileTexture, projectilePos[0] - Prototype.PROJECTILE_WIDTH/2, projectilePos[1] - Prototype.PROJECTILE_HEIGHT/2);
			stage.getBatch().end();
		}

		// if player's nearby object is interactable
		Interactable nearbyObj = game.world.players[game.playerNum].getNearbyObject();
		if (nearbyObj != null && !nearbyObj.interacted()) {
			stage.getBatch().begin();
			stage.getBatch().draw(game.assets.interactableMark,
								  ((Actor)nearbyObj).getX() + ((Actor) nearbyObj).getWidth()/2 - 16,
								  ((Actor)nearbyObj).getY() + ((Actor) nearbyObj).getHeight());
			stage.getBatch().end();
		}

		if (!game.sessionStart) {
			if (elapsed < 2) {
				elapsed += delta;
			}
			else {
				game.disconnect();
				//game.resultScreen.init();
				game.setScreen(new ResultScreen(game));
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

	@Override
	public void resize(int width, int height) {
		//Gdx.app.log("TestScreen", "resized to: " + width + ", " + height);
		viewport.update(width, height);
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / (viewport.getWorldWidth()/viewport.getWorldHeight()));
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		game.assets.introBackground.stop();
		//dispose();
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		batch.dispose();
		font.dispose();
		Gdx.app.log("TestScreen", "disposed");
	}
	
}
