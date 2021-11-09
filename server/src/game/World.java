package game;

import java.nio.ByteBuffer;

import framework.ClientHandler;
import framework.MsgCodes;

public class World {
	// ===============================CAPRICIOUS===============================
	public static final int NUM_PLAYERS = 2;
	public static final int ROBOT_SPEED = 150;
	public static final int PLAYER_SPEED = 200;

	private Player robot;
	private Player player1;
	//private Player player2;
	//private Player player3;
	//private Player player4;

	private Player[] players;

	public World() {
		robot = new Player(0, 0, ROBOT_SPEED, MsgCodes.Game.NORMAL_STATE);
		// robot = new Player();
		// robot.setPosition(0, 0);
		// robot.setSpeed(ROBOT_SPEED);
		// robot.setState(Player.NORMAL_STATE);
		player1 = new Player(100, 100, PLAYER_SPEED, MsgCodes.Game.NORMAL_STATE);
		// player1 = new Player();
		// player1.setPosition(100, 100);
		// player1.setSpeed(PLAYER_SPEED);
		// player1.setState(Player.NORMAL_STATE);

		players = new Player[NUM_PLAYERS];
		players[0] = robot;
		players[1] = player1; // index matches with player number
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
			//if player is not the robot
			if (playerNum != 0)
				players[playerNum].dodge();
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
