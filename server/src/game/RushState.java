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
				robot.y += World.RUSH_SPEED * progressTime / 1000; // go 'moveSpeed' pixels in 1 sec
			}
			if (robot.moveDown) {
				robot.y -= World.RUSH_SPEED * progressTime / 1000;
			}
			if (robot.moveLeft) {
				robot.x -= World.RUSH_SPEED * progressTime / 1000;
			}
			if (robot.moveRight) {
				robot.x += World.RUSH_SPEED * progressTime / 1000;
			}
		}
		else if (elapsed >= 3000) {
			reset();
			return false;
		}

		return true;
	}

	@Override
	void reset() {
		// TODO Auto-generated method stub
		robot.elapsedFromLastRush = 0;
		elapsed = 0;
	}
	
}
