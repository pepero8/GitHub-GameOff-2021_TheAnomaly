package game;

import framework.MsgCodes;

public class Player {
	// ===============================CAPRICIOUS===============================
	char curDirection; //direction the player's looking at

	Area curArea;
	private MovableSpace curSpace;

	//private Interactable possession;
	boolean haveKey;

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
	//boolean dead;

	int moveSpeed; //movement speed(pixels per second)

	PlayerState curState; //player's current state
	private NormalState normalState;
	DodgeState dodgeState;
	private DeadState deadState;
	private DraggedState draggedState;
	GrabbingState grabbingState; //initialized by world(needs player[])
	AttackState attackState; //initialized by world(needs player[])
	RushState rushState; //initialized by world(needs player[])
	InteractState interactState;

	//constructor
	// public Player() {
	// 	normalState = new NormalState(this);
	// 	dodgeState = new DodgeState(this);
	// }

	//constructor
	public Player(float initX, float initY, int playerWidth, int playerHeight, int initSpeed, int initState/* needed? */) {
		curDirection = MsgCodes.Game.DIRECTION_SOUTH;
		x = initX;
		y = initY;
		width = playerWidth;
		height = playerHeight;
		moveSpeed = initSpeed;

		normalState = new NormalState(this);
		dodgeState = new DodgeState(this);
		deadState = new DeadState();
		draggedState = new DraggedState();
		interactState = new InteractState();

		//duplicate. replace with setState()
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
			case MsgCodes.Game.GRABBING_STATE:
				curState = grabbingState;
				break;
			case MsgCodes.Game.RUSH_STATE:
				curState = rushState;
				break;
			case MsgCodes.Game.DEAD_STATE:
				curState = deadState;
				break;
			case MsgCodes.Game.DRAGGED_STATE:
				curState = draggedState;
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

	public void setArea(Area area) {
		curArea = area;
	}

	public void setMovableSpace(MovableSpace space) {
		curSpace = space;
	}

	// public void setPossession(Interactable object) {
	// 	possession = object;
	// }

	public boolean isContact(Player player, int range) {
		// System.out.println("fuck player.x: " + player.x);
		// System.out.println("fuck player.width: " + player.width);
		// System.out.println("fuck attack_range: " + World.ROBOT_ATTACK_RANGE);
		//System.out.println("fuck: " + player.x + player.width + World.ROBOT_ATTACK_RANGE);

		boolean ret =  (x < (player.x + player.width + range)) &&
			   ((x + width) > (player.x - range)) &&
			   (y < (player.y + player.height + range)) &&
			   ((y + height) > (player.y - range));

		// System.out.println("[Player]contact with robot: " + ret);
		// System.out.println("[Player]player1's pos: (" + x + ", " + y + ")");
		// System.out.println("{Player]robot pos: (" + player.x + ", " + player.y + ")");

		return ret;
	}

	public boolean isContact(float projectileX, float projectileY) {
		return projectileX > x && projectileX < x + width &&
			   projectileY > y && projectileY < y + height;
	}

	// public void dodge() {
	// 	//dodgeState.setDirection(curDirection);
	// 	//dodgeState.init(curDirection, moveUp, moveDown, moveLeft, moveRight);
	// 	dodgeState.init(curDirection);
	// 	curState = dodgeState;
	// }

	// public void attack() {
	// 	curState = attackState;
	// }

	public void kill() {
		curState.reset();
		// if (curState == dodgeState) {
		// 	dodgeState.reset();
		// }
		//dead = true;
		curState = deadState;
	}

	// public void grab(float cursorX, float cursorY) {
	// 	grabbingState.init(cursorX, cursorY);
	// 	curState = grabbingState;
	// }

	//called by grabbingState
	public void drag() {
		// if (curState == dodgeState) {
		// 	dodgeState.reset();
		// }
		curState.reset();
		curState = draggedState;
	}

	public boolean isDead() {
		return curState == deadState;
	}

	public char getHaveKeyAsCode() {
		return (haveKey) ? MsgCodes.Game.HAS_KEY : MsgCodes.Game.NO_KEY;
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
			case MsgCodes.Game.GRABBING_STATE:
				curState = grabbingState;
				break;
			case MsgCodes.Game.RUSH_STATE:
				curState = rushState;
				break;
			case MsgCodes.Game.DEAD_STATE:
				curState = deadState;
				break;
			case MsgCodes.Game.DRAGGED_STATE:
				curState = draggedState;
				break;
			case MsgCodes.Game.INTERACT_STATE:
				curState = interactState;
				break;
			default:
		}
	}

	public void update(long progressTime) {
		float prevX = x;
		float prevY = y;
		if (!curState.update(progressTime)) {
			curState = normalState;
		}

		curSpace = curSpace.determineSpace(x, y);
		curArea = curArea.determineArea(x, y);

		//if player overlaps with any object
		if (curArea.hitObject(this)) {
			x = prevX;
			y = prevY;
		}

		x = curSpace.clampPosX(x);
		y = curSpace.clampPosY(y);

		// MovableSpace newSpace = curSpace.determineSpace(x, y);
		// if (newSpace != curSpace) {
		// 	System.out.println("space changed");
		// }

		// if (curState == grabbingState) {
		// 	grabbingState.projectileX = curArea.clampPosX(x);
		// 	grabbingState.projectileY = curArea.clampPosY(y);
		// }
		
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
