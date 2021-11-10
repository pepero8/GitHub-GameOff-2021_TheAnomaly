package game;

import framework.MsgCodes;

public class Player {
	// ===============================CAPRICIOUS===============================
	char curDirection; //direction the player's looking at

	float x, y;

	int width; //player's width
	int height; //player's height

	boolean moveUp;
	boolean moveDown;
	boolean moveLeft;
	boolean moveRight;
	// char moveUp;
	// char moveDown;
	// char moveLeft;
	// char moveRight;
	boolean dead;

	int moveSpeed; //movement speed(pixels per second)

	PlayerState curState; //player's current state
	private NormalState normalState;
	private DodgeState dodgeState;
	private DeadState deadState;
	AttackState attackState; //initialized by world

	//constructor
	// public Player() {
	// 	normalState = new NormalState(this);
	// 	dodgeState = new DodgeState(this);
	// }

	//constructor
	public Player(float initX, float initY, int playerWidth, int playerHeight, int initSpeed, int initState) {
		curDirection = MsgCodes.Game.DIRECTION_SOUTH;
		x = initX;
		y = initY;
		width = playerWidth;
		height = playerHeight;
		moveSpeed = initSpeed;

		normalState = new NormalState(this);
		dodgeState = new DodgeState(this);
		deadState = new DeadState();

		switch (initState) {
			case MsgCodes.Game.NORMAL_STATE:
				curState = normalState;
				break;
			case MsgCodes.Game.DODGE_STATE:
				curState = dodgeState;
				break;
			case MsgCodes.Game.ATTACK_STATE:
				curState = attackState;
				break;
			case MsgCodes.Game.DEAD_STATE:
				curState = deadState;
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

	public boolean isContact(Player player) {
		// System.out.println("fuck player.x: " + player.x);
		// System.out.println("fuck player.width: " + player.width);
		// System.out.println("fuck attack_range: " + World.ROBOT_ATTACK_RANGE);
		//System.out.println("fuck: " + player.x + player.width + World.ROBOT_ATTACK_RANGE);

		boolean ret =  (x < (player.x + player.width + World.ROBOT_ATTACK_RANGE)) &&
			   ((x + width) > (player.x - World.ROBOT_ATTACK_RANGE)) &&
			   (y < (player.y + player.height + World.ROBOT_ATTACK_RANGE)) &&
			   ((y + height) > (player.y - World.ROBOT_ATTACK_RANGE));

		// System.out.println("[Player]contact with robot: " + ret);
		// System.out.println("[Player]player1's pos: (" + x + ", " + y + ")");
		// System.out.println("{Player]robot pos: (" + player.x + ", " + player.y + ")");

		return ret;
	}

	public void dodge() {
		//dodgeState.setDirection(curDirection);
		//dodgeState.init(curDirection, moveUp, moveDown, moveLeft, moveRight);
		dodgeState.init(curDirection);
		curState = dodgeState;
	}

	public void attack() {
		curState = attackState;
	}

	public void kill() {
		dead = true;
		curState = deadState;
	}

	public boolean isDead() {
		return dead;
	}

	public void setState(int state) {
		switch (state) {
			case MsgCodes.Game.NORMAL_STATE:
				curState = normalState;
				break;
			case MsgCodes.Game.DODGE_STATE:
				curState = dodgeState;
				break;
			case MsgCodes.Game.ATTACK_STATE:
				curState = attackState;
				break;
			case MsgCodes.Game.DEAD_STATE:
				curState = deadState;
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
