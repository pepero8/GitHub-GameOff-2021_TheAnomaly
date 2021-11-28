package game;

import java.nio.ByteBuffer;

import framework.ClientHandler;
import framework.MsgCodes;

public class World {
	// ===============================CAPRICIOUS===============================
	public static final long TIME_LIMIT = 300000; //5min
	public static final int NUM_PLAYERS = 3;
	public static final float ROBOT_SPEED = 150f;
	public static final float RUSH_SPEED = 300f;
	public static final float PLAYER_SPEED = 200f;
	public static final int PLAYER_WIDTH = 32;
	public static final int PLAYER_HEIGHT = 32;
	public static final int ROBOT_ATTACK_RANGE = 15;
	public static final int PROJECTILE_DISTANCE = 300; //maximum distance the projectile travels
	public static final int PROJECTILE_CAST_TIME = 500; //arrives to the target location in 0.5 seconds

	public static final int ROBOT_NUM = 0;
	public static final int PLAYER1_NUM = 1;
	public static final int PLAYER2_NUM = 2;
	public static final int PLAYER3_NUM = 3;
	public static final int PLAYER4_NUM = 4;

	public static int REMAINING_SURVIVORS = NUM_PLAYERS - 1; //number of remaining survivors
	public static int DEAD_SURVIVORS = 0;
	
	private Player robot;
	private Player player1;
	private Player player2;
	//private Player player3;
	//private Player player4;

	//Interactable cardKey;

	private Map map;

	Player[] players;

	private long elapsed;

	public boolean gameEnd;
	public char gameEndCode;

	public World() {
		map = new Map();

		robot = new Player(ROBOT_NUM, 784+10, 1088+10, PLAYER_WIDTH, PLAYER_HEIGHT, ROBOT_SPEED, MsgCodes.Game.NORMAL_STATE_STANDING);
		robot.setMovableSpace(map.spaceMiddleArea);
		robot.setArea(map.mainArea);
		// robot = new Player();
		// robot.setPosition(0, 0);
		// robot.setSpeed(ROBOT_SPEED);
		// robot.setState(Player.NORMAL_STATE);
		player1 = new Player(PLAYER1_NUM, 784+95, 1088+95, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_SPEED, MsgCodes.Game.NORMAL_STATE_STANDING);
		player1.setMovableSpace(map.spaceMiddleArea);
		player1.setArea(map.mainArea);
		// player1 = new Player();
		// player1.setPosition(100, 100);
		// player1.setSpeed(PLAYER_SPEED);
		// player1.setState(Player.NORMAL_STATE);
		player2 = new Player(PLAYER2_NUM, player1.x + 95, player1.y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_SPEED, MsgCodes.Game.NORMAL_STATE_STANDING);
		player2.setMovableSpace(map.spaceMiddleArea);
		player2.setArea(map.mainArea);

		players = new Player[NUM_PLAYERS];
		players[0] = robot;
		players[1] = player1; // index matches with player number
		players[2] = player2; // index matches with player number
		// players[3] = player3; // index matches with player number
		// players[4] = player4; // index matches with player number

		//initialize robot's attackState, grabbingState
		robot.attackState = new AttackState(players, robot);
		robot.grabbingState = new GrabbingState(players, robot);
		robot.rushState = new RushState(players, robot);

		elapsed = 0;
	}

	public void processInput(ByteBuffer msg, int playerNum) {
		System.out.println("(World)Processing input: [" + msg.asCharBuffer() + "]");
		char key = msg.getChar();
		char downOrUp = msg.getChar();

		if (key == MsgCodes.Game.UP) {
			players[playerNum].setMoveUp(downOrUp == MsgCodes.Game.KEY_DOWN);
		}
		else if (key == MsgCodes.Game.DOWN) {
			players[playerNum].setMoveDown(downOrUp == MsgCodes.Game.KEY_DOWN);
		}
		else if (key == MsgCodes.Game.LEFT) {
			players[playerNum].setMoveLeft(downOrUp == MsgCodes.Game.KEY_DOWN);
		}
		else if (key == MsgCodes.Game.RIGHT) {
			players[playerNum].setMoveRight(downOrUp == MsgCodes.Game.KEY_DOWN);
		}
		else if (key == MsgCodes.Game.DODGE) {
			//if player is not the robot and not dead
			if (playerNum != ROBOT_NUM && !players[playerNum].isDead())
				players[playerNum].curState.dodge();
		}
		else if (key == MsgCodes.Game.ATTACK) {
			//if player is the robot
			if (playerNum == ROBOT_NUM)
				robot.curState.attack();
		}
		else if (key == MsgCodes.Game.GRAB) {
			float cursorX = msg.getFloat();
			float cursorY = msg.getFloat();
			// if player is the robot
			if (playerNum == ROBOT_NUM)
				robot.curState.grab(cursorX, cursorY);
		}
		else if (key == MsgCodes.Game.RUSH) {
			if (playerNum == ROBOT_NUM) {
				robot.curState.rush();
			}
		}
		else if (key == MsgCodes.Game.INTERACT) {
			if (downOrUp == MsgCodes.Game.KEY_DOWN) {
				int areaNum = msg.getInt();
				int objectNum = msg.getInt();
				//System.out.println("[World] areaNum: " + areaNum + ", objectNum: " + objectNum);
				Interactable obj = map.areas[areaNum].getObjects().get(objectNum);
				players[playerNum].curState.interact(playerNum, obj);
			}
			else if (downOrUp == MsgCodes.Game.KEY_UP) {
				Player curPlayer = players[playerNum];
				if (curPlayer.curState == curPlayer.interactState)
					((InteractState)players[playerNum].curState).haltInteract();
			}
			//map.areas[areaNum].getObjects()[objectNum].interact(playerNum);
		}
	}

	public void update(long progressTime) {
		elapsed += progressTime;
		for (Player player : players) {
			player.update(progressTime);
		}

		if (elapsed >= TIME_LIMIT || DEAD_SURVIVORS == NUM_PLAYERS - 1) {
			gameEndCode = MsgCodes.Server.SESSION_TERMINATE_GAMEOVER_ROBOT_WIN;
			gameEnd = true;
		}
		else if (REMAINING_SURVIVORS == 0) {
			gameEndCode = MsgCodes.Server.SESSION_TERMINATE_GAMEOVER_SURVIVORS_WIN;
			gameEnd = true;
		}
		//System.out.println(" robot.x + ", " + robot.y + ")");
	}

	public byte[] getStatePacket() {
		byte[] packet = new byte[ClientHandler.PACKET_SIZE];
		ByteBuffer packetBuffer = ByteBuffer.wrap(packet);
		
		packetBuffer.putChar(MsgCodes.INPUT);

		// add robot state
		packetBuffer.putChar('r');
		packetBuffer.putFloat(robot.x);
		packetBuffer.putFloat(robot.y);
		packetBuffer.putChar(robot.curState.code);
		//System.out.println("robot: " + robot.curState.code);
		//if robot's current state is grabbing
		if (robot.curState.code == MsgCodes.Game.GRABBING_STATE || robot.curState.code == MsgCodes.Game.ATTACK_GRABBING_STATE) {
			//System.out.println("(" + robot.grabbingState.projectileX + ", " + robot.grabbingState.projectileY + ")");
			packetBuffer.putFloat(robot.grabbingState.projectileX);
			packetBuffer.putFloat(robot.grabbingState.projectileY);
		}
		packetBuffer.putChar(robot.curDirection);
		//add player1's state
		packetBuffer.putChar('1');
		packetBuffer.putFloat(player1.x);
		packetBuffer.putFloat(player1.y);
		packetBuffer.putChar(player1.curState.code);
		//System.out.println(player1.curState.code);
		packetBuffer.putChar(player1.getHaveKeyAsCode());
		//System.out.println(player1.getHaveKeyAsCode());
		packetBuffer.putChar(player1.curDirection);

		// add player2's state
		packetBuffer.putChar('2');
		packetBuffer.putFloat(player2.x);
		packetBuffer.putFloat(player2.y);
		packetBuffer.putChar(player2.curState.code);
		packetBuffer.putChar(player2.getHaveKeyAsCode());
		packetBuffer.putChar(player2.curDirection);

		//add card key info
		packetBuffer.putFloat((map.cardKey == null) ? -1f : map.cardKey.getX());
		packetBuffer.putFloat((map.cardKey == null) ? -1f : map.cardKey.getY());
		packetBuffer.putInt((map.cardKey == null) ? -1 : map.cardKey.getArea().getNumber());

		//add remaining time in milliseconds
		packetBuffer.putLong(TIME_LIMIT - elapsed);

		return packetBuffer.array();
	}
	// ===============================CAPRICIOUS===============================
}
