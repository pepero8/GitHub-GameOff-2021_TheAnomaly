package framework;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
	private static final int PORT = 8014;

	private static final int MAX_CLIENTS = 100; // limit client numbers

	private ServerSocket welcomeSocket;

	public Lobby lobbyThread;

	private volatile int activeHandlerNum = 0;

	//constructor
	Server() {
		try {
			welcomeSocket = new ServerSocket(PORT);
			lobbyThread = new Lobby();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void start() {
		//====================================FOR DEBUG==========================================
		System.out.println("server started at port:" + welcomeSocket.getLocalPort());
		//====================================FOR DEBUG==========================================

		lobbyThread.start();

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
