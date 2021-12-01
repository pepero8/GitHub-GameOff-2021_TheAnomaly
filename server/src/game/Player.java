package game;

import framework.MsgCodes;

public class Player {
	World world;

	int playerNum;
	//private World world;
	// ===============================CAPRICIOUS===============================
	char curDirection; //direction the player's looking at

	Area curArea;
	MovableSpace curSpace;

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

	float moveSpeed; //movement speed(pixels per second)

	PlayerState curState; //player's current state
	private NormalState normalState;
	DodgeState dodgeState;
	private DeadState deadState;
	private DraggedState draggedState;
	GrabbingState grabbingState; //initialized by world(needs player[])
	AttackState attackState; //initialized by world(needs player[])
	RushState rushState; //initialized by world(needs player[])
	InteractState interactState;

	private ExitState exitState;
	boolean exit;

	long elapsedFromLastRush = World.RUSH_COOL_DOWN; //used by robot
	//constructor
	// public Player() {
	// 	normalState = new NormalState(this);
	// 	dodgeState = new DodgeState(this);
	// }

	//constructor
	public Player(World world, int playerNum, float initX, float initY, int playerWidth, int playerHeight, float initSpeed, int initState/* needed? */) {
		this.world = world;
		this.playerNum = playerNum;
		//this.world = world;
		curDirection = MsgCodes.Game.DIRECTION_SOUTH;
		x = initX;
		y = initY;
		width = playerWidth;
		height = playerHeight;
		moveSpeed = initSpeed;

		normalState = new NormalState(this);
		dodgeState = new DodgeState(this);
		deadState = new DeadState();
		draggedState = new DraggedState(this);
		interactState = new InteractState();
		exitState = new ExitState();

		//duplicate. replace with setState()
		switch (initState) {
			// case MsgCodes.Game.NORMAL_STATE:
			// 	curState = normalState;
			// 	break;
			case MsgCodes.Game.NORMAL_STATE_STANDING:
			case MsgCodes.Game.NORMAL_STATE_MOVING:
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
		if (curState == deadState) return;
		curState.reset();
		dropKey();
		// if (curState == dodgeState) {
		// 	dodgeState.reset();
		// }
		//dead = true;
		world.DEAD_SURVIVORS++;
		world.REMAINING_SURVIVORS--;
		curState = deadState;
	}

	public void dropKey() {
		if (haveKey) {
			Interactable cardkey = new CardKeyObject(curArea, x + width/4, y + height/4, CardKeyObject.CARDKEYOBJECT_WIDTH, CardKeyObject.CARDKEYOBJECT_HEIGHT, "card key");
			curArea.addObject(cardkey);
			haveKey = false;
			//world.cardKey = cardkey;
		}
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
			// case MsgCodes.Game.NORMAL_STATE:
			// 	curState = normalState;
			// 	break;
			case MsgCodes.Game.NORMAL_STATE_STANDING:
			case MsgCodes.Game.NORMAL_STATE_MOVING:
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
		if (playerNum == World.ROBOT_NUM) {
			elapsedFromLastRush += progressTime;
			//System.out.println("elapsedRush: " + elapsedFromLastRush);
		}

		float prevX = x;
		float prevY = y;
		if (!curState.update(progressTime)) {
			curState = normalState;
		}

		// if player overlaps with any object
		if (curArea.hitObject(this)) {
			//System.out.println("Player hit an object");
			x = prevX;
			y = prevY;
		}

		curSpace = curSpace.determineSpace(x, y);
		curArea = curArea.determineArea(this);


		if (curArea.getNumber() == Map.EXIT_AREA_NUM) {
			curState = exitState;
			if (!exit) {
				world.REMAINING_SURVIVORS--;
				exit = true;
			}
			x = 0;
			y = 0;
		}
		else {
			x = curSpace.clampPosX(x);
			y = curSpace.clampPosY(y);
		}

		// //if player overlaps with any object
		// if (curArea.hitObject(this)) {
		// 	x = prevX;
		// 	y = prevY;
		// }

		

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
