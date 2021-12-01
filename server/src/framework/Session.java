package framework;

import java.net.SocketException;
import java.nio.ByteBuffer;

import game.World;

public class Session extends WorkerThread {
	// ===============================CAPRICIOUS===============================
	ClientHandler robot;
	ClientHandler player1;
	ClientHandler player2;
	ClientHandler player3;
	ClientHandler player4;
	// ===============================CAPRICIOUS===============================

	private World world;

	private int playersReady = 0;
	private boolean gameStart;
	
	//constructor
	public Session() {
		super();

		// ===============================CAPRICIOUS===============================
		MS_PER_UPDATE = 16; // 대략 1초에 60번 업데이트
		// ===============================CAPRICIOUS===============================
	}

	public void init(ClientHandler robot, ClientHandler player1, ClientHandler player2, ClientHandler player3, ClientHandler player4) {
		// ===============================CAPRICIOUS===============================
		this.robot = robot;
		this.player1 = player1;
		this.player2 = player2;
		this.player3 = player3;
		this.player4 = player4;

		world = new World();

		// ===============================CAPRICIOUS===============================
	}

	@Override
	public void bind(ClientHandler ch) {
		// ===============================CAPRICIOUS===============================
		// ===============================CAPRICIOUS===============================
	}

	@Override
	protected void processInput(ClientHandler ch) {
		ByteBuffer msg = ByteBuffer.wrap(ch.getClientMsg());
		//====================================FOR DEBUG==========================================
		System.out.println("(Session)processing input:[" + msg.asCharBuffer() + "]");
		//====================================FOR DEBUG==========================================
		char msgType = msg.getChar();
		
		// ===============================CAPRICIOUS===============================
		if (msgType == MsgCodes.MESSAGECODE) {
			char msgCode = msg.getChar();
			if (msgCode == MsgCodes.Client.CLOSE_CONNECTION) {
				char msgcode = '0';
				if (robot == ch) {
					world.playerDisconnect(World.ROBOT_NUM);
					msgcode = MsgCodes.Server.DISCONNECT_ROBOT;
					//robot.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
					// player1.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
					// player2.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
					terminate();
				}
				else if (player1 == ch) {
					world.playerDisconnect(World.PLAYER1_NUM);
					msgcode = MsgCodes.Server.DISCONNECT_PLAYER1;
					//player1.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
					// robot.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
					// player2.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
				}
				else if (player2 == ch) {
					world.playerDisconnect(World.PLAYER2_NUM);
					msgcode = MsgCodes.Server.DISCONNECT_PLAYER2;
					//player2.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
					// robot.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
					// player1.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
				}
				else if (player3 == ch) {
					world.playerDisconnect(World.PLAYER3_NUM);
					msgcode = MsgCodes.Server.DISCONNECT_PLAYER3;
				}
				else if (player4 == ch) {
					world.playerDisconnect(World.PLAYER4_NUM);
					msgcode = MsgCodes.Server.DISCONNECT_PLAYER4;
				}

				robot.send(MsgCodes.MESSAGECODE, msgcode);
				player1.send(MsgCodes.MESSAGECODE, msgcode);
				player2.send(MsgCodes.MESSAGECODE, msgcode);
				player3.send(MsgCodes.MESSAGECODE, msgcode);
				player4.send(MsgCodes.MESSAGECODE, msgcode);
				
				//terminate();
				// robot.unbind();
				// player1.unbind();

				// terminate = true;

				// //====================================FOR DEBUG==========================================
				// System.out.println("(Session)session over");
				// //====================================FOR DEBUG==========================================
			}
			else if (msgCode == MsgCodes.Client.READY) {
				playersReady++;
				if (playersReady == World.NUM_PLAYERS) {
					robot.send(MsgCodes.MESSAGECODE, MsgCodes.Server.START_GAME);
					player1.send(MsgCodes.MESSAGECODE, MsgCodes.Server.START_GAME);
					player2.send(MsgCodes.MESSAGECODE, MsgCodes.Server.START_GAME);
					player3.send(MsgCodes.MESSAGECODE, MsgCodes.Server.START_GAME);
					player4.send(MsgCodes.MESSAGECODE, MsgCodes.Server.START_GAME);
					gameStart = true;
					System.out.println("[Session]starting game...");
				}
			}
		}
		//delegate handling of inputs to world
		else if (msgType == MsgCodes.INPUT) {
			if (robot == ch) {
				world.processInput(msg, World.ROBOT_NUM);
			}
			else if (player1 == ch) {
				world.processInput(msg, World.PLAYER1_NUM);
			}
			else if (player2 == ch) {
				world.processInput(msg, World.PLAYER2_NUM);
			}
			else if (player3 == ch) {
				world.processInput(msg, World.PLAYER3_NUM);
			}
			else if (player4 == ch) {
				world.processInput(msg, World.PLAYER4_NUM);
			}
		}
		// ===============================CAPRICIOUS===============================
	}

	@Override
	protected void update(long progressTime) {
		if (gameStart) {
			if (!world.gameEnd)
				world.update(progressTime);
			else {
				robot.send(MsgCodes.MESSAGECODE, world.gameEndCode);
				player1.send(MsgCodes.MESSAGECODE, world.gameEndCode);
				player2.send(MsgCodes.MESSAGECODE, world.gameEndCode);
				player3.send(MsgCodes.MESSAGECODE, world.gameEndCode);
				player4.send(MsgCodes.MESSAGECODE, world.gameEndCode);
				terminate();
			}
		}
		//====================================FOR DEBUG==========================================
		//System.out.println("(Session)updating game state...");
		//====================================FOR DEBUG==========================================
	}

	private void terminate() {
		robot.unbind();
		player1.unbind();
		player2.unbind();
		player3.unbind();
		player4.unbind();

		terminate = true;

		// ====================================FOR DEBUG==========================================
		System.out.println("(Session)session over");
		// ====================================FOR DEBUG==========================================
	}

	@Override
	protected void broadcast() {
		//====================================FOR DEBUG==========================================
		//System.out.println("(Session)broadcasting: [" + inputSet[0] + ", " + inputSet[1] + "]");
		//====================================FOR DEBUG==========================================

		// ===============================CAPRICIOUS===============================
		if (gameStart) {
				byte[] packet = world.getStatePacket();
				robot.send(packet);
				player1.send(packet);
				player2.send(packet);
				player3.send(packet);
				player4.send(packet);
		}
		// ===============================CAPRICIOUS===============================
	}
}
