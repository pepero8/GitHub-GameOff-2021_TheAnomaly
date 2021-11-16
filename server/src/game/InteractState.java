package game;

import framework.MsgCodes;

public class InteractState extends PlayerState {
	private Player player;
	//private int areaNum;
	//private int objectNum;
	private Interactable targetObj;
	private long elapsed;
	private long requiredTime;
	private boolean halt;
	private boolean interacted;

	InteractState() {
		code = MsgCodes.Game.INTERACT_STATE;
	}

	void init(Player player, Interactable target) {
		this.player = player;
		//this.areaNum = areaNum;
		//this.objectNum = objectNum;
		targetObj = target;
		requiredTime = target.getRequireTime();

		// elapsed = 0;
		// halt = false;
		// code = MsgCodes.Game.INTERACT_STATE;
		reset();
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
				//elapsed = 0;
				reset();
				return false;
			}
		}
		else if (elapsed < requiredTime + 500) {
			//if (targetObj.interacted());
			if (interacted);
			else if (targetObj.interact(player)) {
				//System.out.println("success!");
				code = MsgCodes.Game.INTERACT_SUCCESS_STATE;
			}
			else {
				code = MsgCodes.Game.INTERACT_FAILED_STATE;
			}
			interacted = true;
		}
		else {
			//elapsed = 0;
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
		code = MsgCodes.Game.INTERACT_STATE;
	}
	
}
