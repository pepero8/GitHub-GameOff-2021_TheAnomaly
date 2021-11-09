package game;

import framework.MsgCodes;

public class NormalState extends PlayerState {
	private Player player;

	public NormalState(Player player) {
		this.player = player;
		code = MsgCodes.Game.NORMAL_STATE;
	}

	@Override
	public void update(long progressTime) {
		// TODO Auto-generated method stub
		// float prevX = player.x;
		// float prevY = player.y;

		if (player.moveUp) {
			player.y += player.moveSpeed * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
		}
		if (player.moveDown) {
			player.y -= player.moveSpeed * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
		}
		if (player.moveLeft) {
			player.x -= player.moveSpeed * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
		}
		if (player.moveRight) {
			player.x += player.moveSpeed * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
		}

		//if x or y changed
		// if (prevX == player.x && prevY < player.y) {
		// 	player.curDirection = MsgCodes.Game.DIRECTION_NORTH;
		// }
		// if (prevX < player.x && prevY < player.y) {
		// 	player.curDirection = MsgCodes.Game.DIRECTION_NORTH_EAST;
		// }
		// if (prevX < player.x && prevY == player.y) {
		// 	player.curDirection = MsgCodes.Game.DIRECTION_EAST;
		// }
		// if (prevX < player.x && prevY > player.y) {
		// 	player.curDirection = MsgCodes.Game.DIRECTION_SOUTH_EAST;
		// }
		// if (prevX == player.x && prevY > player.y) {
		// 	player.curDirection = MsgCodes.Game.DIRECTION_SOUTH;
		// }
		// if (prevX > player.x && prevY > player.y) {
		// 	player.curDirection = MsgCodes.Game.DIRECTION_SOUTH_WEST;
		// }
		// if (prevX > player.x && prevY == player.y) {
		// 	player.curDirection = MsgCodes.Game.DIRECTION_WEST;
		// }
		// if (prevX > player.x && prevY < player.y) {
		// 	player.curDirection = MsgCodes.Game.DIRECTION_NORTH_WEST;
		// }
	}
	
}
