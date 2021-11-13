package game;

import framework.MsgCodes;

public class RushState extends PlayerState {
	private Player robot;
	private Player[] players;
	private long elapsed;

	RushState(Player[] players, Player robot) {
		this.players = players;
		this.robot = robot;
		code = MsgCodes.Game.RUSH_STATE;

		elapsed = 0;
	}

	@Override
	boolean update(long progressTime) {
		elapsed += progressTime;

		if (elapsed < 2000) {
			for (int i = 1; i < players.length; i++) {
				if (!players[i].isDead() && players[i].isContact(robot, 0)) {
					// kill
					players[i].kill();
				}
			}
			if (robot.moveUp) {
				robot.y += World.RUSH_SPEED * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
			}
			if (robot.moveDown) {
				robot.y -= World.RUSH_SPEED * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
			}
			if (robot.moveLeft) {
				robot.x -= World.RUSH_SPEED * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
			}
			if (robot.moveRight) {
				robot.x += World.RUSH_SPEED * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
			}
		}
		else if (elapsed >= 3000) {
			elapsed = 0;
			return false;
		}

		return true;
	}
	
}
