package framework;

import java.nio.ByteBuffer;

import game.World;

public class Session extends WorkerThread {
	// ===============================CAPRICIOUS===============================
	ClientHandler robot; //로봇(0번 플레이어)
	ClientHandler player1; //1번 플레이어
	//ClientHandler player2; //2번 플레이어
	//ClientHandler player3; //3번 플레이어
	//ClientHandler player4; //4번 플레이어
	// ===============================CAPRICIOUS===============================

	private World world;
	
	//constructor
	public Session() {
		super();

		// ===============================CAPRICIOUS===============================
		MS_PER_UPDATE = 16; // 대략 1초에 60번 업데이트
		// ===============================CAPRICIOUS===============================
	}

	public void init(ClientHandler robot, ClientHandler player1/* , ClientHandler player2, ClientHandler player3, ClientHandler player4 */) {
		// ===============================CAPRICIOUS===============================
		this.robot = robot;
		this.player1 = player1;
		//this.player2 = player2;
		//this.player3 = player3;
		//this.player4 = player4;

		world = new World();

		//send each client their player number
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
				if (robot == ch) {
					player1.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
				}
				else {
					robot.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_TERMINATE_OD);
				}
				
				terminate();
				// robot.unbind();
				// player1.unbind();

				// terminate = true;

				// //====================================FOR DEBUG==========================================
				// System.out.println("(Session)session over");
				// //====================================FOR DEBUG==========================================
			}
		}
		//delegate handling of inputs to world
		else if (msgType == MsgCodes.DATA) {
			if (robot == ch) {
				world.processInput(msg, 0); //로봇의 입력을 반영
			}
			else if (player1 == ch) {
				world.processInput(msg, 1); //1번 플레이어의 입력을 반영
			}
		}
		// ===============================CAPRICIOUS===============================
	}

	@Override
	protected void update(long progressTime) {
		if (!world.gameEnd)
			world.update(progressTime);
		else {
			robot.send(MsgCodes.MESSAGECODE, world.gameEndCode);
			player1.send(MsgCodes.MESSAGECODE, world.gameEndCode);
			//player2.send(MsgCodes.MESSAGECODE, world.gameEndCode);
			//player3.send(MsgCodes.MESSAGECODE, world.gameEndCode);
			//player4.send(MsgCodes.MESSAGECODE, world.gameEndCode);
			terminate();
		}
		//====================================FOR DEBUG==========================================
		//System.out.println("(Session)updating game state...");
		//====================================FOR DEBUG==========================================
	}

	private void terminate() {
		robot.unbind();
		player1.unbind();

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
		byte[] packet = world.getStatePacket();
		robot.send(packet);
		player1.send(packet);
		//player2.send(packet);
		//player3.send(packet);
		//player4.send(packet);
		// ===============================CAPRICIOUS===============================
	}
}
