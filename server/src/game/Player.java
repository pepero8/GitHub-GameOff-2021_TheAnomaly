package game;

import framework.MsgCodes;

public class Player {
	// ===============================CAPRICIOUS===============================
	char curDirection; //direction the player's looking at

	float x, y;

	boolean moveUp;
	boolean moveDown;
	boolean moveLeft;
	boolean moveRight;
	// char moveUp;
	// char moveDown;
	// char moveLeft;
	// char moveRight;

	int moveSpeed; //movement speed(pixels per second)

	PlayerState curState; //player's current state
	private NormalState normalState;
	private DodgeState dodgeState;

	//constructor
	public Player() {
		normalState = new NormalState(this);
		dodgeState = new DodgeState(this);
	}

	//constructor
	public Player(float initX, float initY, int initSpeed, int initState) {
		curDirection = MsgCodes.Game.DIRECTION_SOUTH;
		x = initX;
		y = initY;
		moveSpeed = initSpeed;

		normalState = new NormalState(this);
		dodgeState = new DodgeState(this);

		switch (initState) {
			case MsgCodes.Game.NORMAL_STATE:
				curState = normalState;
				break;
			case MsgCodes.Game.DODGE_STATE:
				curState = dodgeState;
				break;
			default:
		}
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setMoveUp(boolean bool) {
		moveUp = bool;
		updateDirection();
	}

	public void setMoveDown(boolean bool) {
		moveDown = bool;
		updateDirection();
	}

	public void setMoveLeft(boolean bool) {
		moveLeft = bool;
		updateDirection();
	}

	public void setMoveRight(boolean bool) {
		moveRight = bool;
		updateDirection();
	}

	public void setSpeed(int speed) {
		moveSpeed = speed;
	}

	public void dodge() {
		//dodgeState.setDirection(curDirection);
		//dodgeState.init(curDirection, moveUp, moveDown, moveLeft, moveRight);
		dodgeState.init(curDirection);
		curState = dodgeState;
	}

	public void setState(int state) {
		switch (state) {
			case MsgCodes.Game.NORMAL_STATE:
				curState = normalState;
				break;
			case MsgCodes.Game.DODGE_STATE:
				curState = dodgeState;
				break;
			default:
		}
	}

	public void update(long progressTime) {
		curState.update(progressTime);
	}

	public void updateDirection() {
		if (moveUp && !moveDown && !moveLeft && !moveRight) {
			curDirection = MsgCodes.Game.DIRECTION_NORTH;
		}
		else if (moveUp && !moveDown && !moveLeft && moveRight) {
			curDirection = MsgCodes.Game.DIRECTION_NORTH_EAST;
		}
		else if (!moveUp && !moveDown && !moveLeft && moveRight) {
			curDirection = MsgCodes.Game.DIRECTION_EAST;
		}
		else if (!moveUp && moveDown && !moveLeft && moveRight) {
			curDirection = MsgCodes.Game.DIRECTION_SOUTH_EAST;
		}
		else if (!moveUp && moveDown && !moveLeft && !moveRight) {
			curDirection = MsgCodes.Game.DIRECTION_SOUTH;
		}
		else if (!moveUp && moveDown && moveLeft && !moveRight) {
			curDirection = MsgCodes.Game.DIRECTION_SOUTH_WEST;
		}
		else if (!moveUp && !moveDown && moveLeft && !moveRight) {
			curDirection = MsgCodes.Game.DIRECTION_WEST;
		}
		else if (moveUp && !moveDown && moveLeft && !moveRight) {
			curDirection = MsgCodes.Game.DIRECTION_NORTH_WEST;
		}
	}

	// public float getX() {
	// 	return x;
	// }

	// public float getY() {
	// 	return y;
	// }
	// ===============================CAPRICIOUS===============================
}
