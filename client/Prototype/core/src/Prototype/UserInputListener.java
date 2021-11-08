package Prototype;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import net.Client;
import net.MsgCodes;

public class UserInputListener extends InputListener {
	Client client;

	public UserInputListener(Client client) {
		this.client = client;
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode) {
		switch (keycode) {
			case Keys.W:
				client.sendInput(MsgCodes.Client.UP, MsgCodes.Client.KEY_DOWN);
				return true;
			case Keys.A:
				client.sendInput(MsgCodes.Client.LEFT, MsgCodes.Client.KEY_DOWN);
				return true;
			case Keys.S:
				client.sendInput(MsgCodes.Client.DOWN, MsgCodes.Client.KEY_DOWN);
				return true;
			case Keys.D:
				client.sendInput(MsgCodes.Client.RIGHT, MsgCodes.Client.KEY_DOWN);
				return true;
		default:
			return false;
		}
	}

	@Override
	public boolean keyUp(InputEvent event, int keycode) {
		switch (keycode) {
		case Keys.W:
			client.sendInput(MsgCodes.Client.UP, MsgCodes.Client.KEY_UP);
			return true;
		case Keys.A:
			client.sendInput(MsgCodes.Client.LEFT, MsgCodes.Client.KEY_UP);
			return true;
		case Keys.S:
			client.sendInput(MsgCodes.Client.DOWN, MsgCodes.Client.KEY_UP);
			return true;
		case Keys.D:
			client.sendInput(MsgCodes.Client.RIGHT, MsgCodes.Client.KEY_UP);
			return true;
		default:
			return false;
		}
	}
}
