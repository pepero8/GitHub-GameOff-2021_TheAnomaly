package framework;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
	/**
	 * HOW TO ADD A PLAYER
	 * Lobby
	 * - increase NUM_PLAYERS_PER_SESSION
	 * - add new player handling code in update()
	 * 
	 * Session
	 * - add player variable
	 * - change constructor's parameters
	 * - add code blocks handling extra player
	 * 
	 * World
	 * - increase NUM_PLAYERS value
	 * - add new player variable
	 * - initialize the player in constructor
	 * - add new player to players[]
	 * 
	 * ClientHandler
	 * - increase PACKET_SIZE
	 */
	private static final int PORT = 8014;

	// ===============================CAPRICIOUS===============================
	private static final int MAX_CLIENTS = 5; // limit client numbers
	// ===============================CAPRICIOUS===============================

	private ServerSocket welcomeSocket;

	// ===============================CAPRICIOUS===============================
	public Lobby lobbyThread;
	// ===============================CAPRICIOUS===============================

	private volatile int activeHandlerNum = 0;

	//constructor
	Server() {
		try {
			welcomeSocket = new ServerSocket(PORT);

			// ===============================CAPRICIOUS===============================
			lobbyThread = new Lobby();
			// ===============================CAPRICIOUS===============================
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void start() {
		//====================================FOR DEBUG==========================================
		System.out.println("server started at port:" + welcomeSocket.getLocalPort());
		//====================================FOR DEBUG==========================================

		// ===============================CAPRICIOUS===============================
		lobbyThread.start();
		// ===============================CAPRICIOUS===============================

		Socket connectionSocket;
		
		try {
			while (true) {
				//====================================FOR DEBUG==========================================
				System.out.println("(server)current num of connected clients: " + activeHandlerNum);
				//====================================FOR DEBUG==========================================
				
				connectionSocket = welcomeSocket.accept();

				if (activeHandlerNum == MAX_CLIENTS) connectionSocket.close(); //need testing
				else {
					ClientHandler newHandler = new ClientHandler();
					newHandler.init(connectionSocket, this);
					newHandler.start();
					newHandler.bind(lobbyThread);
					activeHandlerNum++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void clientClosed() {
		activeHandlerNum--;
	}
}
