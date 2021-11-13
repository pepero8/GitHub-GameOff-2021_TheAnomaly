package game;

import java.nio.ByteBuffer;

import framework.ClientHandler;
import framework.MsgCodes;

public class World {
	// ===============================CAPRICIOUS===============================
	public static final int NUM_PLAYERS = 2;
	public static final int ROBOT_SPEED = 150;
	public static final int PLAYER_SPEED = 200;
	public static final int PLAYER_WIDTH = 64;
	public static final int PLAYER_HEIGHT = 128;
	public static final int ROBOT_ATTACK_RANGE = 25;
	public static final int PROJECTILE_DISTANCE = 250; //maximum distance the projectile travels
	public static final int PROJECTILE_CAST_TIME = 500; //arrives to the target location in 0.5 seconds

	private Player robot;
	private Player player1;
	//private Player player2;
	//private Player player3;
	//private Player player4;

	Player[] players;

	public World() {
		robot = new Player(0, 0, PLAYER_WIDTH, PLAYER_HEIGHT, ROBOT_SPEED, MsgCodes.Game.NORMAL_STATE);
		// robot = new Player();
		// robot.setPosition(0, 0);
		// robot.setSpeed(ROBOT_SPEED);
		// robot.setState(Player.NORMAL_STATE);
		player1 = new Player(100, 100, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_SPEED, MsgCodes.Game.NORMAL_STATE);
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
	}

	public void update(long progressTime) {
		for (Player player : players) {
			player.update(progressTime);
		}
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
		packetBuffer.putChar(player1.curDirection);

		return packetBuffer.array();
	}
	// ===============================CAPRICIOUS===============================
}
