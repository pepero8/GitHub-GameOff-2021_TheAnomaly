package game;

import framework.MsgCodes;

public class ExitState extends PlayerState {

	ExitState() {
		code = MsgCodes.Game.EXIT_STATE;
	}

	@Override
	boolean update(long progressTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void reset() {
		// TODO Auto-generated method stub
		
	}
	
}
