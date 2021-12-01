package game;

import framework.MsgCodes;

public class DodgeState extends PlayerState {
	private static final int DODGE_SPEED = 270;

	private char direction;
	private Player player;

	private long elapsed; //time elapsed from start of dodge in millisecond

	DodgeState(Player player) {
		this.player = player;
		code = MsgCodes.Game.DODGE_STATE;
		elapsed = 0;
	}

	public void init(char direction) {
		this.direction = direction;
	}

	@Override
	public boolean update(long progressTime) {
		// TODO Auto-generated method stub
		elapsed += progressTime;

		if (elapsed < 500) {
			if (direction == MsgCodes.Game.DIRECTION_NORTH) {
				player.y += DODGE_SPEED * progressTime / 1000;
			}
			else if (direction == MsgCodes.Game.DIRECTION_NORTH_EAST) {
				player.x += DODGE_SPEED * progressTime / 1000;
				player.y += DODGE_SPEED * progressTime / 1000;
			}
			else if (direction == MsgCodes.Game.DIRECTION_EAST) {
				player.x += DODGE_SPEED * progressTime / 1000;
			}
			else if (direction == MsgCodes.Game.DIRECTION_SOUTH_EAST) {
				player.x += DODGE_SPEED * progressTime / 1000;
				player.y -= DODGE_SPEED * progressTime / 1000;
			}
			else if (direction == MsgCodes.Game.DIRECTION_SOUTH) {
				player.y -= DODGE_SPEED * progressTime / 1000;
			}
			else if (direction == MsgCodes.Game.DIRECTION_SOUTH_WEST) {
				player.x -= DODGE_SPEED * progressTime / 1000;
				player.y -= DODGE_SPEED * progressTime / 1000;
			}
			else if (direction == MsgCodes.Game.DIRECTION_WEST) {
				player.x -= DODGE_SPEED * progressTime / 1000;
			}
			else if (direction == MsgCodes.Game.DIRECTION_NORTH_WEST) {
				player.x -= DODGE_SPEED * progressTime / 1000;
				player.y += DODGE_SPEED * progressTime / 1000;
			}
		}
		else if (elapsed >= 1000) {
			reset();
			return false;
		}

		player.curDirection = direction; //in order to not change direction when player sends direction input while in dodge motion
		return true;
	}

	void reset() {
		elapsed = 0;
		player.updateDirection();
	}
}
