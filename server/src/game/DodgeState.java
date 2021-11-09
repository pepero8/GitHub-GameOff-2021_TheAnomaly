package game;

import framework.MsgCodes;

public class DodgeState extends PlayerState {
	private static final int DODGE_SPEED = 500;

	private char direction;
	private Player player;

	// boolean moveUp;
	// boolean moveDown;
	// boolean moveLeft;
	// boolean moveRight;

	private long elapsed; //time elapsed from start of dodge in millisecond

	DodgeState(Player player) {
		this.player = player;
		code = MsgCodes.Game.DODGE_STATE;
		elapsed = 0;
	}

	public void init(char direction) {
		this.direction = direction;
	}

	// public void init(char direction, boolean moveUp, boolean moveDown, boolean moveLeft, boolean moveRight) {
	// 	this.direction = direction;
	// 	this.moveUp = moveUp;
	// 	this.moveDown = moveDown;
	// 	this.moveLeft = moveLeft;
	// 	this.moveRight = moveRight;
	// }

	// public void setDirection(char direction) {
	// 	this.direction = direction;
	// }

	// public void setMoves(boolean moveUp, boolean moveDown, boolean moveLeft, boolean moveRight) {
	// 	this.moveUp = moveUp;
	// 	this.moveDown = moveDown;
	// 	this.moveLeft = moveLeft;
	// 	this.moveRight = moveRight;
	// }

	@Override
	public void update(long progressTime) {
		// TODO Auto-generated method stub
		elapsed += progressTime;

		//닷지 모션은 0.5초동안 대쉬후 0.5초동안 가만히 멈춤
		if (elapsed < 500) {
			// if (moveUp) {
			// 	player.y += DODGE_SPEED * progressTime / 1000; // 1초에 DODGE_SPEED픽셀씩 움직임
			// }
			// if (moveDown) {
			// 	player.y -= DODGE_SPEED * progressTime / 1000; // 1초에 DODGE_SPEED픽셀씩 움직임
			// }
			// if (moveLeft) {
			// 	player.x -= DODGE_SPEED * progressTime / 1000; // 1초에 DODGE_SPEED픽셀씩 움직임
			// }
			// if (moveRight) {
			// 	player.x += DODGE_SPEED * progressTime / 1000; // 1초에 DODGE_SPEED픽셀씩 움직임
			// }
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
			//state 초기화
			elapsed = 0;
			player.updateDirection();
			player.setState(MsgCodes.Game.NORMAL_STATE);
			return;
		}

		player.curDirection = direction; //dodge 모션중 방향키를 입력해도 캐릭터 방향을 바꾸지 않기 위함
	}	
}
