package game;

import framework.MsgCodes;

public class InteractState extends PlayerState {
	private Player player;
	private Interactable targetObj;
	private long elapsed;
	private long requiredTime;
	private boolean halt;
	private boolean interacted;

	InteractState() {
		code = MsgCodes.Game.INTERACT_STATE;
		elapsed = 0;
	}

	void init(Player player, Interactable target) {
		this.player = player;
		targetObj = target;
		requiredTime = target.getRequireTime();

		target.setInteracting(true);
	}

	@Override
	void haltInteract() {
		halt = true;
	}

	@Override
	boolean update(long progressTime) {
		// TODO Auto-generated method stub
		elapsed += progressTime;

		if (elapsed < requiredTime) {
			if (halt) {
				reset();
				return false;
			}
		}
		else if (elapsed < requiredTime + 500) {
			if (interacted);
			else if (targetObj.interact(player)) {
				code = MsgCodes.Game.INTERACT_SUCCESS_STATE;
			}
			else {
				code = MsgCodes.Game.INTERACT_FAILED_STATE;
			}
			interacted = true;
		}
		else {
			reset();
			return false;
		}

		return true;
	}

	@Override
	void reset() {
		// TODO Auto-generated method stub
		elapsed = 0;
		halt = false;
		interacted = false;
		targetObj.setInteracting(false);
		code = MsgCodes.Game.INTERACT_STATE;
	}
	
}
