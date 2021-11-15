package game;

import framework.MsgCodes;

public class DraggedState extends PlayerState {

	DraggedState() {
		code = MsgCodes.Game.DRAGGED_STATE;
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
