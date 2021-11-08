package framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ClientHandler extends Thread {
	// ===============================CAPRICIOUS===============================
	public static final int PACKET_SIZE = 22;
	// ===============================CAPRICIOUS===============================
	private Socket connectionSocket;
	private InetAddress ip_client;
	private int port_client;

	private InputStream in;
	private OutputStream out;

	private byte[] msgClient;
	private byte[] msgClientCache;

	private Server server;
	private WorkerThread curWorkerThread; //current bound worker thread

	void init(Socket connectionSocket, Server server) {
		this.connectionSocket = connectionSocket;
		this.server = server;

		try {
			//====================================FOR DEBUG==========================================
			ip_client = connectionSocket.getInetAddress();
			port_client = connectionSocket.getPort();
			//====================================FOR DEBUG==========================================

			in = connectionSocket.getInputStream();
			out = connectionSocket.getOutputStream();

			msgClient = new byte[PACKET_SIZE];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		//====================================FOR DEBUG==========================================
		System.out.println("======================================================");
		System.out.println("connected from: " + ip_client + ": " + port_client);
		System.out.println("listening port: " + port_client);
		System.out.println("======================================================");
		//====================================FOR DEBUG==========================================

		try {
			int read_ch; // number of characters read from input stream
			while (true) {
				read_ch = in.read(msgClient); // blocked until arrival of message
				if (read_ch == -1) break; //remote socket is closed
				//====================================FOR DEBUG==========================================
				System.out.print("[" + ip_client + ":" + port_client + "]");
				System.out.println("(" + read_ch + "bytes)");
				//====================================FOR DEBUG==========================================
				handleInput(msgClient);
				emptyMsgBuffer(); //cache is necessary since buffer is emptied right after forwarding message to work thread
			}

		} catch (SocketException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		terminate();
	}

	private void handleInput(byte[] input) {
		//====================================FOR DEBUG==========================================
		//System.out.println("(ClientHandler)handling input: [" + msg + "]");
		//====================================FOR DEBUG==========================================

		msgClientCache = input.clone();

		// delegate handling input
		if (curWorkerThread != null)
			curWorkerThread.getMsgQueue().offerMsg(this);
	}

	public byte[] getClientMsg() {
		return msgClientCache;
	}

	//called by worker threads or Server
	public void bind(WorkerThread workerThread) {
		curWorkerThread = workerThread;
		curWorkerThread.bind(this);
	}

	//called by worker threads
	public void unbind() {
		curWorkerThread = null;
	}

	// clears client msg buffer
	void emptyMsgBuffer() {
		Arrays.fill(msgClient, (byte) 0);
	}

	public void send(char msgType, char msgCode) {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		packetBuffer.putChar(msgType);
		packetBuffer.putChar(msgCode);

		byte[] packet = packetBuffer.array();

		try {
			out.write(packet);
			out.flush();
			// ====================================FOR DEBUG==========================================
			System.out.println("sent msg(" + ip_client + ":" + port_client + "): [" + packetBuffer.asCharBuffer() + "]");
			// ====================================FOR DEBUG==========================================
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(byte[] packet) {
		try {
			out.write(packet);
			out.flush();
			// ====================================FOR DEBUG==========================================
			//System.out.println("sent msg(" + ip_client + ":" + port_client + "): [" + ByteBuffer.wrap(packet).asCharBuffer() + "]");
			// ====================================FOR DEBUG==========================================
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void terminate() {
		try {
			connectionSocket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("closed connection from " + ip_client + ":" + port_client + '\n');
		server.clientClosed();
	}
}
