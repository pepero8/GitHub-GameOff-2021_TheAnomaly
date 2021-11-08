package framework;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
	private static final int PORT = 8014;

	// ===============================CAPRICIOUS===============================
	private static final int MAX_CLIENTS = 5; // limit client numbers
	public static final int MAX_SESSIONS = 2; // limit session numbers
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

				if (activeHandlerNum == MAX_CLIENTS) connectionSocket.close();
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
