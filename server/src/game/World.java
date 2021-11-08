package game;

import java.nio.ByteBuffer;

import framework.ClientHandler;
import framework.MsgCodes;

public class World {
	public static final int NUM_PLAYERS = 2;
	// ===============================CAPRICIOUS===============================

	private Player robot;
	private Player player1;
	//private Player player2;
	//private Player player3;
	//private Player player4;

	private Player[] players;

	private long remainingTime;

	public World() {
		robot = new Player();
		robot.setPosition(0, 0);
		player1 = new Player();
		player1.setPosition(100, 100);

		players = new Player[NUM_PLAYERS];
		players[0] = robot;
		players[1] = player1; // index matches with player number
	}

	public void processInput(ByteBuffer msg, int playerNum) {
		System.out.println("(World)Processing input: [" + msg.asCharBuffer() + "]");
		char key = msg.getChar();
		char downOrUp = msg.getChar();

		if (key == MsgCodes.Client.UP) {
			players[playerNum].setMoveUp(downOrUp == MsgCodes.Client.KEY_DOWN);
		}
		if (key == MsgCodes.Client.DOWN) {
			players[playerNum].setMoveDown(downOrUp == MsgCodes.Client.KEY_DOWN);
		}
		if (key == MsgCodes.Client.LEFT) {
			players[playerNum].setMoveLeft(downOrUp == MsgCodes.Client.KEY_DOWN);
		}
		if (key == MsgCodes.Client.RIGHT) {
			players[playerNum].setMoveRight(downOrUp == MsgCodes.Client.KEY_DOWN);
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
		packetBuffer.putFloat(robot.getX());
		packetBuffer.putFloat(robot.getY());
		//add player1's state
		packetBuffer.putChar('1');
		packetBuffer.putFloat(player1.getX());
		packetBuffer.putFloat(player1.getY());

		return packetBuffer.array();
	}
	// ===============================CAPRICIOUS===============================
}
