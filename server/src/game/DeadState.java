package game;

import framework.MsgCodes;

public class DeadState extends PlayerState{

	DeadState() {
		code = MsgCodes.Game.DEAD_STATE;
	}

	@Override
	void update(long progressTime) {
		// TODO Auto-generated method stub
		
	}
	
}
