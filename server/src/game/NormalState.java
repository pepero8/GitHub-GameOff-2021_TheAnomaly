package game;

import framework.MsgCodes;

public class NormalState extends PlayerState {
	private Player player;

	public NormalState(Player player) {
		this.player = player;
		code = MsgCodes.Game.NORMAL_STATE_STANDING;
	}

	@Override
	void dodge() {
		player.dodgeState.init(player.curDirection);
		player.curState = player.dodgeState; //replace with setState()?
	}

	@Override
	void attack() {
		player.curState = player.attackState;
	}

	@Override
	void grab(float cursorX, float cursorY) {
		player.grabbingState.init(cursorX, cursorY);
		player.curState = player.grabbingState;
	}

	@Override
	void rush() {
		if (player.elapsedFromLastRush >= World.RUSH_COOL_DOWN)
			player.curState = player.rushState;
	}

	@Override
	void interact(int playerNum, Interactable target) {
		if (!target.isInteractable(playerNum) || !target.isContact(player, Interactable.INTERACTION_AVAILABLE_RANGE) || target.interacted() || target.interacting()) {
			return;
		}
		player.interactState.init(player, target);
		player.curState = player.interactState;
	}

	@Override
	public boolean update(long progressTime) {
		// TODO Auto-generated method stub

		code = MsgCodes.Game.NORMAL_STATE_STANDING;

		if (player.moveUp) {
			player.y += player.moveSpeed * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
			code = MsgCodes.Game.NORMAL_STATE_MOVING;
		}
		if (player.moveDown) {
			player.y -= player.moveSpeed * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
			code = MsgCodes.Game.NORMAL_STATE_MOVING;
		}
		if (player.moveLeft) {
			player.x -= player.moveSpeed * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
			code = MsgCodes.Game.NORMAL_STATE_MOVING;
		}
		if (player.moveRight) {
			player.x += player.moveSpeed * progressTime / 1000; // 1초에 moveSpeed픽셀씩 움직임
			code = MsgCodes.Game.NORMAL_STATE_MOVING;
		}
		
		return true;
	}

	@Override
	void reset() {
		// TODO Auto-generated method stub
		
	}
	
}
