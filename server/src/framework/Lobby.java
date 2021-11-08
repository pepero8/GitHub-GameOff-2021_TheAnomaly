package framework;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

class Lobby extends WorkerThread {
	
	Queue<ClientHandler> clientQueue;

	private int activeSessions = 0;

	//constructor
	public Lobby() {
		super();
		clientQueue = new ArrayDeque<ClientHandler>();
	}

	@Override
	protected void processInput(ClientHandler ch) {
		// ===============================CAPRICIOUS===============================
		ByteBuffer msg = ByteBuffer.wrap(ch.getClientMsg());
		char msgType = msg.getChar();
		//====================================FOR DEBUG==========================================
		System.out.println("(lobby)processing input:[" + msg.asCharBuffer() + "]");
		//====================================FOR DEBUG==========================================
		if (msgType == MsgCodes.MESSAGECODE) {
			char msgCode = msg.getChar();
			if (msgCode == MsgCodes.Client.CLOSE_CONNECTION) {
				clientQueue.remove(ch); //update 이전에 수행되어야 함
				ch.unbind();
			}
		}
		// ===============================CAPRICIOUS===============================
	}

	@Override
	protected void update(long progressTime) {
		//====================================FOR DEBUG==========================================
		System.out.println("(lobby)tick, current number of waiting clients: " + clientQueue.size());
		//====================================FOR DEBUG==========================================

		// ===============================CAPRICIOUS===============================
		if (clientQueue.size() >= 2) {
			ClientHandler robot = clientQueue.poll();
			ClientHandler player1 = clientQueue.poll();

			robot.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_START_PLAYER0);
			player1.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_START_PLAYER1);
			
			robot.unbind();
			player1.unbind();

			Session newSession = new Session();
			newSession.init(robot, player1);

			robot.bind(newSession);
			player1.bind(newSession);

			newSession.start();

			//====================================FOR DEBUG==========================================
			System.out.println("(lobby)new session started");
			//====================================FOR DEBUG==========================================
		}
		// ===============================CAPRICIOUS===============================
	}

	@Override
	protected void broadcast() {
		// ===============================CAPRICIOUS===============================
		// ===============================CAPRICIOUS===============================
	}

	@Override
	public synchronized void bind(ClientHandler ch) {
		// ===============================CAPRICIOUS===============================
		clientQueue.offer(ch);
		// ===============================CAPRICIOUS===============================
	}
}
