package game;

import framework.MsgCodes;

public class DeadState extends PlayerState{

	DeadState() {
		code = MsgCodes.Game.DEAD_STATE;
	}

	@Override
	boolean update(long progressTime) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	void reset() {
		// TODO Auto-generated method stub
		
	}
	
}
