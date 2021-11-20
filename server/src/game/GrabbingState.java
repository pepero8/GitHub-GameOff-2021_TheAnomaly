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

	//float signX; //increase or decrease of projectileX
	//float signY; //increase or decrease of projectileY

	//float numeratorX, numeratorY, denominator;

	float startX, startY, endX, endY;

	//float ratioX, ratioY;

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

		startX = projectileX;
		startY = projectileY;

		//calculate length between cursor and projectile
		float cursorToProjLen = (float) Math.sqrt(Math.pow((double)cursorX - (double)projectileX, 2) + Math.pow((double)cursorY - (double)projectileY, 2));
		//get the nearest point on the grab-range-circle of robot to the cursor
		endX = projectileX + (cursorX - projectileX) * World.PROJECTILE_DISTANCE / cursorToProjLen;
		endY = projectileY + (cursorY - projectileY) * World.PROJECTILE_DISTANCE / cursorToProjLen;

		//System.out.println("startX: " + startX + ", startY: " + startY);
		//System.out.println("endX: " + endX + ", endY: " + endY);

		// signX = (cursorX < projectileX) ? -1 : 1;
		// signY = (cursorY < projectileY) ? -1 : 1;

		// numeratorX = Math.abs(cursorX - projectileX);
		// numeratorY = Math.abs(cursorY - projectileY);
		// denominator = numeratorX + numeratorY;
		// ratioX = numeratorX / denominator;
		// ratioY = numeratorY / denominator;
		reset();
	}

	@Override
	void attack() {
		code = MsgCodes.Game.ATTACK_GRABBING_STATE;
	}

	@Override
	boolean update(long progressTime) {

		//there are duplicate codes here

		elapsed += progressTime;

		if (code == MsgCodes.Game.ATTACK_GRABBING_STATE) {
			if (!robot.attackState.update(progressTime)) {
				code = MsgCodes.Game.GRABBING_STATE;
			}
		}

		if (retrieve) {
			//pullPlayer(target);
			//projectileX += -1 * signX * World.PROJECTILE_SPEED * ratioX * progressTime / 1000;
			projectileX = endX + (startX - endX) * (elapsed - projectileElapsed) / projectileElapsed;
			//projectileY += -1 * signY * World.PROJECTILE_SPEED * ratioY * progressTime / 1000;
			projectileY = endY + (startY - endY) * (elapsed - projectileElapsed) / projectileElapsed;
			if (pulling && !target.isDead())
				target.setPosition(projectileX - target.width/2, projectileY - target.height/2); //sets projectile's pos at the middle of the target

			if (robot.isContact(projectileX, projectileY)) {
				//elapsed = 0;
				if (pulling && !target.isDead())
					target.setState(MsgCodes.Game.NORMAL_STATE_STANDING);
				//robot.setState(MsgCodes.Game.NORMAL_STATE);
				// target = null;
				// pulling = false;
				// retrieve = false;
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
			//if projectile hits the area's wall or hits any object in the area
			if (robot.curArea.hitWall(projectileX, projectileY) || robot.curArea.hitObject(projectileX, projectileY)) {
				retrieve = true;
				projectileElapsed = elapsed;
				endX = projectileX;
				endY = projectileY;
				return true;
			}
			for (int i = 1; i < players.length; i++) {
				if (!players[i].isDead() && players[i].isContact(projectileX, projectileY)) {
					//pullPlayer(players[i]);
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
			//projectileX += signX * World.PROJECTILE_SPEED * ratioX * progressTime / 1000;
			projectileX = startX + (endX - startX) * elapsed / World.PROJECTILE_CAST_TIME;
			//projectileY += signY * World.PROJECTILE_SPEED * ratioY * progressTime / 1000;
			projectileY = startY + (endY - startY) * elapsed / World.PROJECTILE_CAST_TIME;
		}
		else if (elapsed < 1000) {
			//projectileX += -1 * signX * World.PROJECTILE_SPEED * ratioX * progressTime / 1000;
			projectileX = endX + (startX - endX) * (elapsed-World.PROJECTILE_CAST_TIME) / World.PROJECTILE_CAST_TIME;
			//projectileY += -1 * signY * World.PROJECTILE_SPEED * ratioY * progressTime / 1000;
			projectileY = endY + (startY - endY) * (elapsed-World.PROJECTILE_CAST_TIME) / World.PROJECTILE_CAST_TIME;
		}
		else {
			// elapsed = 0;
			// pulling = false;
			// retrieve = false;
			reset();
			//robot.setState(MsgCodes.Game.NORMAL_STATE);
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

	// private void updateAttack() {
	// 	if (elapsed < 300) {
	// 		for (int i = 1; i < players.length; i++) {
	// 			if (!players[i].isDead() && players[i].isContact(robot)) {
	// 				// kill
	// 				players[i].kill();
	// 			}
	// 		}
	// 	} else if (elapsed >= 600) {
	// 		code = MsgCodes.Game.GRABBING_STATE;
	// 	}
	// }
}