package game;

import framework.MsgCodes;

public class GrabbingState extends PlayerState {
	private long elapsed;
	private long projectileElapsed; //time in milliseconds taken for the projectile to be cast to the destination

	private Player robot;
	private Player[] players;

	private float cursorX; //mouse cursor's position's x
	private float cursorY; //mouse cursor's position's y

	float projectileX; //projectile's center point's x
	float projectileY; //projectile's center point's y
	private Area projectileArea;

	float startX, startY, endX, endY;

	private boolean pulling; //true when the projectile hits any player
	private boolean retrieve; //true when the projectile hits the area's wall or any object in the area
	private Player target;

	GrabbingState(Player[] players, Player robot) {
		this.players = players;
		this.robot = robot;
		code = MsgCodes.Game.GRABBING_STATE;
		
		elapsed = 0;
	}

	public void init(float x, float y) {
		cursorX = x;
		cursorY = y;

		projectileX = robot.x + robot.width/2;
		projectileY = robot.y + robot.height/2;

		projectileArea = robot.curArea;

		startX = projectileX;
		startY = projectileY;

		//calculate length between cursor and projectile
		float cursorToProjLen = (float) Math.sqrt(Math.pow((double)cursorX - (double)projectileX, 2) + Math.pow((double)cursorY - (double)projectileY, 2));
		//get the nearest point on the grab-range-circle of robot to the cursor
		endX = projectileX + (cursorX - projectileX) * World.PROJECTILE_DISTANCE / cursorToProjLen;
		endY = projectileY + (cursorY - projectileY) * World.PROJECTILE_DISTANCE / cursorToProjLen;

		reset();
	}

	@Override
	void attack() {
		code = MsgCodes.Game.ATTACK_GRABBING_STATE;
	}

	@Override
	boolean update(long progressTime) {
		projectileArea = projectileArea.determineArea(projectileX, projectileY);

		//there are duplicate codes here

		elapsed += progressTime;

		if (code == MsgCodes.Game.ATTACK_GRABBING_STATE) {
			if (!robot.attackState.update(progressTime)) {
				code = MsgCodes.Game.GRABBING_STATE;
			}
		}

		if (retrieve) {
			projectileX = endX + (startX - endX) * (elapsed - projectileElapsed) / projectileElapsed;
			projectileY = endY + (startY - endY) * (elapsed - projectileElapsed) / projectileElapsed;
			if (pulling && !target.isDead())
				target.setPosition(projectileX - target.width/2, projectileY - target.height/2); //sets projectile's pos to the middle of the target

			if (robot.isContact(projectileX, projectileY)) {
				if (pulling && !target.isDead())
					target.setState(MsgCodes.Game.NORMAL_STATE_STANDING);
				reset();
				if (code == MsgCodes.Game.ATTACK_GRABBING_STATE) {
					code = MsgCodes.Game.GRABBING_STATE;
					robot.setState(MsgCodes.Game.ATTACK_STATE);
					return true;
				}
				return false;
			}

			return true;
		}

		if (elapsed <= 500) {
			if (projectileArea.hitWall(projectileX, projectileY) || projectileArea.hitObject(projectileX, projectileY)) {
				retrieve = true;
				projectileElapsed = elapsed;
				endX = projectileX;
				endY = projectileY;
				return true;
			}
			for (int i = 1; i < players.length; i++) {
				if (!players[i].isDead() && players[i].isContact(projectileX, projectileY)) {
					players[i].drag();
					target = players[i];
					pulling = true;
					retrieve = true;
					projectileElapsed = elapsed;
					endX = projectileX;
					endY = projectileY;
					return true;
				}
			}
			projectileX = startX + (endX - startX) * elapsed / World.PROJECTILE_CAST_TIME;
			projectileY = startY + (endY - startY) * elapsed / World.PROJECTILE_CAST_TIME;
		}
		else if (elapsed < 1000) {
			projectileX = endX + (startX - endX) * (elapsed-World.PROJECTILE_CAST_TIME) / World.PROJECTILE_CAST_TIME;
			projectileY = endY + (startY - endY) * (elapsed-World.PROJECTILE_CAST_TIME) / World.PROJECTILE_CAST_TIME;
		}
		else {
			reset();

			if (code == MsgCodes.Game.ATTACK_GRABBING_STATE) {
				code = MsgCodes.Game.GRABBING_STATE;
				robot.setState(MsgCodes.Game.ATTACK_STATE);
				return true;
			}
			return false;
		}

		return true;
	}

	@Override
	void reset() {
		// TODO Auto-generated method stub
		elapsed = 0;
		target = null;
		pulling = false;
		retrieve = false;
	}
}