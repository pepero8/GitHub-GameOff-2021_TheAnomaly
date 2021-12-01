package framework;

import java.nio.ByteBuffer;

import game.World;

public class Session extends WorkerThread {
	ClientHandler robot;
	ClientHandler player1;
	ClientHandler player2;
	ClientHandler player3;
	ClientHandler player4;

	private World world;

	private int playersReady = 0;
	private boolean gameStart;
	
	//constructor
	public Session() {
		super();

		MS_PER_UPDATE = 16; //approximately 60 updates in 1 sec
	}

	public void init(ClientHandler robot, ClientHandler player1, ClientHandler player2, ClientHandler player3, ClientHandler player4) {
		
		this.robot = robot;
		this.player1 = player1;
		this.player2 = player2;
		this.player3 = player3;
		this.player4 = player4;

		world = new World();
	}

	@Override
	public void bind(ClientHandler ch) {
		
		
	}

	@Override
	protected void processInput(ClientHandler ch) {
		ByteBuffer msg = ByteBuffer.wrap(ch.getClientMsg());

		char msgType = msg.getChar();
		
		if (msgType == MsgCodes.MESSAGECODE) {
			char msgCode = msg.getChar();
			if (msgCode == MsgCodes.Client.CLOSE_CONNECTION) {
				char msgcode = '0';
				if (robot == ch) {
					world.playerDisconnect(World.ROBOT_NUM);
					msgcode = MsgCodes.Server.DISCONNECT_ROBOT;
					terminate();
				}
				else if (player1 == ch) {
					world.playerDisconnect(World.PLAYER1_NUM);
					msgcode = MsgCodes.Server.DISCONNECT_PLAYER1;
				}
				else if (player2 == ch) {
					world.playerDisconnect(World.PLAYER2_NUM);
					msgcode = MsgCodes.Server.DISCONNECT_PLAYER2;
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
		if (gameStart) {
			byte[] packet = world.getStatePacket();
			robot.send(packet);
			player1.send(packet);
			player2.send(packet);
			player3.send(packet);
			player4.send(packet);
		}
		
	}
}
