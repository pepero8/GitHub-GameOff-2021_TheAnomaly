package game;

import framework.MsgCodes;

public class DraggedState extends PlayerState {
	private Player player;

	DraggedState(Player player) {
		this.player = player;
		code = MsgCodes.Game.DRAGGED_STATE;
	}

	@Override
	boolean update(long progressTime) {
		// TODO Auto-generated method stub
		player.curArea = player.curArea.determineArea(player);
		player.curSpace = player.curSpace.determineSpace(player.x, player.y);
		
		return true;
	}

	@Override
	void reset() {
		// TODO Auto-generated method stub
		
	}
	
}
