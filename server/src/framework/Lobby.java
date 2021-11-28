package framework;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

class Lobby extends WorkerThread {
	public static final int NUM_PLAYERS_PER_SESSION = 3;
	public static final int MAX_SESSIONS = 2; // limit session numbers
	
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
		if (msgType == MsgCodes.DATA) {
			byte[] playerNameBytes = new byte[24];
			msg.get(playerNameBytes);
			ch.playerName = playerNameBytes.clone();
		}
		// ===============================CAPRICIOUS===============================
	}

	@Override
	protected void update(long progressTime) {
		//====================================FOR DEBUG==========================================
		System.out.println("(lobby)tick, current number of waiting clients: " + clientQueue.size());
		//====================================FOR DEBUG==========================================

		// ===============================CAPRICIOUS===============================
		if (clientQueue.size() >= NUM_PLAYERS_PER_SESSION) {
			ClientHandler robot = clientQueue.poll();
			ClientHandler player1 = clientQueue.poll();
			ClientHandler player2 = clientQueue.poll();
			// ClientHandler player3 = clientQueue.poll();
			// ClientHandler player4 = clientQueue.poll();

			// send each client player names
			byte[] packet = new byte[ClientHandler.PACKET_SIZE];
			ByteBuffer packetBuffer = ByteBuffer.wrap(packet);
			packetBuffer.putChar(MsgCodes.DATA);
			packetBuffer.put(robot.playerName);
			packetBuffer.put(player1.playerName);
			packetBuffer.put(player2.playerName);
			// packetBuffer.put(player3.playerName);
			// packetBuffer.put(player4.playerName);

			robot.send(packet);
			player1.send(packet);
			player2.send(packet);
			// player3.send(packet);
			// player4.send(packet);

			//send each client session start message
			robot.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_START_PLAYER0);
			player1.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_START_PLAYER1);
			player2.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_START_PLAYER2);
			// player3.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_START_PLAYER3);
			// player4.send(MsgCodes.MESSAGECODE, MsgCodes.Server.SESSION_START_PLAYER4);
			
			robot.unbind();
			player1.unbind();
			player2.unbind();
			// player3.unbind();
			// player4.unbind();

			Session newSession = new Session();
			newSession.init(robot, player1, player2);

			robot.bind(newSession);
			player1.bind(newSession);
			player2.bind(newSession);
			// player3.bind(newSession);
			// player4.bind(newSession);

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
