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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
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
	private Label remainTimeLabel;

	private int curPlayer;

	private float elapsed = 0;
	
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
						curPlayer = (curPlayer + 1) % Prototype.NUM_PLAYERS;
						return true;
					default:
						return false;
				}
			}
		});

		observingPlayerLabel = new Label("Observing: player1", game.assets.skin, "default");
		instructionLabel = new Label("press 'tab' to see next player", game.assets.skin, "default");
		instructionLabel.setFontScale(0.5f, 0.5f);

		remainTimeLabel = new Label("0:00", game.assets.skin, "default") {};
		remainTimeLabel.setAlignment(Align.center);
	}

	public void init() { //needed?
		elapsed = 0;
	}

	@Override
	public void show() {
		game.assets.introBackground.loop(0.2f);
		// TODO Auto-generated method stub
		stage.addActor(game.world);
		stage.addActor(observingPlayerLabel);
		stage.addActor(instructionLabel);
		stage.addActor(remainTimeLabel);
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
				game.setScreen(new ResultScreen(game));
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

		adjustSounds();

		observingPlayerLabel.setPosition(playerCamera.position.x - Prototype.VP_WIDTH/2, playerCamera.position.y + Prototype.VP_HEIGHT/2 - observingPlayerLabel.getHeight());
		observingPlayerLabel.setText("Observing: " + game.world.players[curPlayer].getName());

		instructionLabel.setPosition(observingPlayerLabel.getX()+10, playerCamera.position.y - Prototype.VP_HEIGHT / 2);

		remainTimeLabel.setPosition(playerCamera.position.x + Prototype.VP_WIDTH / 2 - remainTimeLabel.getWidth(), playerCamera.position.y + Prototype.VP_HEIGHT/2 - remainTimeLabel.getHeight());
		int remainTimeMin = (int)((float)game.world.remainTime / 60000f);
		int remainTimeSec = ((int)((float)game.world.remainTime / 1000f) % 60);
		CharSequence remainTimeSecStr = String.valueOf(remainTimeSec);
		if (remainTimeSec < 10)
			remainTimeSecStr = "0" + remainTimeSecStr;
		if (remainTimeMin == 0)
			remainTimeLabel.setColor(Color.RED);
		remainTimeLabel.setText(String.valueOf(remainTimeMin) + ":" + remainTimeSecStr);
		
		stage.draw();
	}

	private void adjustSounds() {
		float[] position;
		double distance;
		float volume;

		for (Player character : game.world.players) {
			position = character.accessPosition("get", 0, 0);
			distance = Math.sqrt(Math.pow(position[0] - playerCamera.position.x, 2) +
					Math.pow(position[1] - playerCamera.position.y, 2));
			volume = (float) (1 - distance / Assets.AUDIBILITY_RANGE);
			character.setSoundVolume((volume < 0 ? 0 : volume));
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		//Gdx.app.log("ObservingScreen", "resized to: " + width + ", " + height);
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
		// TODO Auto-generated method stub
		//stage.dispose();
		Gdx.app.log("ObserverScreen", "disposed");
	}
	
}
