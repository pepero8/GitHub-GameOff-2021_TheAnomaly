package game;

import framework.MsgCodes;

public class AttackState extends PlayerState {
	private long elapsed;
	private Player[] players;
	private Player robot;

	AttackState(Player[] players, Player robot) {
		this.players = players;
		this.robot = robot;
		code = MsgCodes.Game.ATTACK_STATE;

		elapsed = 0;
	}

	@Override
	boolean update(long progressTime) {
		elapsed += progressTime;
		
		if (elapsed < 300) {
			for (int i = 1; i < players.length; i++) {
				if (!players[i].isDead() && players[i].isContact(robot)) {
					//kill
					players[i].kill();
				}
			}
		}
		else if (elapsed >= 600) {
			elapsed = 0;
			//robot.setState(MsgCodes.Game.NORMAL_STATE);
			return false;
		}

		return true;
	}
	
}
