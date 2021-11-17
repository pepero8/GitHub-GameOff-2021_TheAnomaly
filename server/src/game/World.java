package game;

import java.nio.ByteBuffer;

import framework.ClientHandler;
import framework.MsgCodes;

public class World {
	// ===============================CAPRICIOUS===============================
	public static final long TIME_LIMIT = 10000;
	public static final int NUM_PLAYERS = 2;
	public static final int ROBOT_SPEED = 150;
	public static final int RUSH_SPEED = 300;
	public static final int PLAYER_SPEED = 200;
	public static final int PLAYER_WIDTH = 32;
	public static final int PLAYER_HEIGHT = 32;
	public static final int ROBOT_ATTACK_RANGE = 25;
	public static final int PROJECTILE_DISTANCE = 320; //maximum distance the projectile travels
	public static final int PROJECTILE_CAST_TIME = 500; //arrives to the target location in 0.5 seconds

	private Player robot;
	private Player player1;
	//private Player player2;
	//private Player player3;
	//private Player player4;

	//Interactable cardKey;

	private Map map;

	Player[] players;

	private long elapsed;
	public boolean gameover;

	public World() {
		map = new Map();

		robot = new Player(0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, ROBOT_SPEED, MsgCodes.Game.NORMAL_STATE);
		robot.setMovableSpace(map.spaceMainWest);
		robot.setArea(map.mainArea);
		// robot = new Player();
		// robot.setPosition(0, 0);
		// robot.setSpeed(ROBOT_SPEED);
		// robot.setState(Player.NORMAL_STATE);
		player1 = new Player(95, 95, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_SPEED, MsgCodes.Game.NORMAL_STATE);
		player1.setMovableSpace(map.spaceMainWest);
		player1.setArea(map.mainArea);
		// player1 = new Player();
		// player1.setPosition(100, 100);
		// player1.setSpeed(PLAYER_SPEED);
		// player1.setState(Player.NORMAL_STATE);

		players = new Player[NUM_PLAYERS];
		players[0] = robot;
		players[1] = player1; // index matches with player number

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
			if (playerNum != 0 && !players[playerNum].isDead())
				players[playerNum].curState.dodge();
		}
		else if (key == MsgCodes.Game.ATTACK) {
			//if player is the robot
			if (playerNum == 0)
				robot.curState.attack();
		}
		else if (key == MsgCodes.Game.GRAB) {
			float cursorX = msg.getFloat();
			float cursorY = msg.getFloat();
			// if player is the robot
			if (playerNum == 0)
				robot.curState.grab(cursorX, cursorY);
		}
		else if (key == MsgCodes.Game.RUSH) {
			if (playerNum == 0) {
				robot.curState.rush();
			}
		}
		else if (key == MsgCodes.Game.INTERACT) {
			if (downOrUp == MsgCodes.Game.KEY_DOWN) {
				int areaNum = msg.getInt();
				int objectNum = msg.getInt();
				//System.out.println("[World] areaNum: " + areaNum + ", objectNum: " + objectNum);
				players[playerNum].curState.interact(playerNum, map.areas[areaNum].getObjects().get(objectNum));
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

		if (elapsed >= TIME_LIMIT) {
			gameover = true;
		}
		//System.out.println(" robot.x + ", " + robot.y + ")");
	}

	public byte[] getStatePacket() {
		byte[] packet = new byte[ClientHandler.PACKET_SIZE];
		ByteBuffer packetBuffer = ByteBuffer.wrap(packet);
		
		packetBuffer.putChar(MsgCodes.DATA);

		// add robot state
		packetBuffer.putChar('r');
		packetBuffer.putFloat(robot.x);
		packetBuffer.putFloat(robot.y);
		packetBuffer.putChar(robot.curState.code);
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

		//add card key info
		packetBuffer.putFloat((map.cardKey == null) ? -1f : map.cardKey.getX());
		packetBuffer.putFloat((map.cardKey == null) ? -1f : map.cardKey.getY());
		packetBuffer.putInt((map.cardKey == null) ? -1 : map.cardKey.getArea().getNumber());

		//add remaining time in milliseconds
		packetBuffer.putLong(World.TIME_LIMIT - elapsed);

		return packetBuffer.array();
	}
	// ===============================CAPRICIOUS===============================
}
