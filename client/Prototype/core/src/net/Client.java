package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Disposable;

import Prototype.Prototype;

public class Client extends Thread implements Disposable {
	//private static final String SERVER_IP = "3.38.115.16";
	private static final String SERVER_IP = "192.168.123.106";
	private static final int SERVER_PORT = 8014;
	private static final int PACKET_SIZE = 60;

	public byte[] netResponse;
	public Socket socket;
	public InputStream in;
	public OutputStream out;

	private final Prototype game;

	//constructor
	public Client(final Prototype game) {
		this.game = game;
		netResponse = new byte[PACKET_SIZE];
	}

	@Override
	public void run() {
		
		int read_ch = 0;
		try {
			socket = Gdx.net.newClientSocket(Protocol.TCP, SERVER_IP, SERVER_PORT, null);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			while(true) {
				read_ch = in.read(netResponse);
				if (read_ch == -1) break; //클라이언트 쪽에서 소켓을 닫으면 어떻게 될까
				//====================================FOR DEBUG==========================================
				//System.out.println("(Client)(" + read_ch + "bytes read)");
				//====================================FOR DEBUG==========================================
				handleResponse(netResponse);
				emptyMsgBuffer();
			}

		} catch (SocketException e) {
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		game.disconnect();
	}

	private void handleResponse(byte[] packet) {
		ByteBuffer packetBuffer = ByteBuffer.wrap(packet);
		char msgType = packetBuffer.getChar();

		//lot of duplicate codes here
		if (msgType == MsgCodes.DATA) {
			char character = packetBuffer.getChar();

			//robot
			if (character == 'r') {
				float x = packetBuffer.getFloat();
				float y = packetBuffer.getFloat();
				float projectileX = 0;
				float projectileY = 0;
				char state = packetBuffer.getChar();
				if (state == MsgCodes.Game.GRABBING_STATE || state == MsgCodes.Game.ATTACK_GRABBING_STATE) {
					projectileX = packetBuffer.getFloat();
					projectileY = packetBuffer.getFloat();
					//Gdx.app.log("Projectile", "(" + projectileX + ", " + projectileY + ")");
				}
				char direction = packetBuffer.getChar();

				game.world.robot.accessPosition("set", x, y); //using this function to achieve synchronization
				game.world.robot.accessState("set", state);
				//if (state == MsgCodes.Game.GRABBING_STATE || state == MsgCodes.Game.ATTACK_GRABBING_STATE)
				game.world.robot.accessProjectilePos("set", projectileX, projectileY);
				game.world.robot.accessDirection("set", direction);
			}

			character = packetBuffer.getChar();
			//player1
			if (character == '1') {
				float x = packetBuffer.getFloat();
				float y = packetBuffer.getFloat();
				char state = packetBuffer.getChar();
				char hasKey = packetBuffer.getChar();
				char direction = packetBuffer.getChar();

				//Gdx.app.log("Client", "" + hasKey);

				game.world.player1.accessPosition("set", x, y); // using this function to achieve synchronization
				game.world.player1.accessState("set", state);
				game.world.player1.accessHasKey("set", hasKey == MsgCodes.Game.HAS_KEY);
				game.world.player1.accessDirection("set", direction);
			}

			//card key
			float cardKeyX = packetBuffer.getFloat();
			float cardKeyY = packetBuffer.getFloat();
			int areaNum = packetBuffer.getInt();
			//if (areaNum != -1)
			//	Gdx.app.log("Client", "(" + cardKeyX + ", " + cardKeyY + ")");
			game.world.cardKey.setPosition(cardKeyX, cardKeyY);
			game.world.cardKey.setArea(areaNum);

			//remaining time
			long remainingTime = packetBuffer.getLong();
			game.world.setRemainTime(remainingTime);
		}
		else if (msgType == MsgCodes.MESSAGECODE) {
			char msgCode = packetBuffer.getChar();
			if (msgCode == MsgCodes.Server.SESSION_TERMINATE_OD) {
				Gdx.app.log("Client", "Session terminated[SESSION_TERMINATE_OD]");
				game.start = false;
			}
			else if (msgCode == MsgCodes.Server.SESSION_TERMINATE_GAMEOVER) {
				Gdx.app.log("Client", "Session terminate[GAMEOVER]");
				game.start = false;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER0) {
				game.start = true;
				game.playerNum = 0;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER1) {
				game.start = true;
				game.playerNum = 1;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER2) {
				game.start = true;
				game.playerNum = 2;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER3) {
				game.start = true;
				game.playerNum = 3;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER4) {
				game.start = true;
				game.playerNum = 4;
			}
		}
	}

	void emptyMsgBuffer() {
		Arrays.fill(netResponse, (byte)0);
	}

	public void send(char msgType, byte[] msg) {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		packetBuffer.putChar(msgType);
		packetBuffer.put(msg);

		byte[] packet = packetBuffer.array();
		try {
			out.write(packet);
			out.flush();
			//====================================FOR DEBUG==========================================
			System.out.println("(Client)sent(" + packet.length + "bytes)");
			//====================================FOR DEBUG==========================================
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(char msgType, char msg) {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		packetBuffer.putChar(msgType);
		packetBuffer.putChar(msg);

		byte[] packet = packetBuffer.array();

		try {
			out.write(packet);
			out.flush();
			//====================================FOR DEBUG==========================================
			System.out.println("(Client)sent(" + packet.length + "bytes)");
			//====================================FOR DEBUG==========================================
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendInput(char key, char downOrUp) {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		packetBuffer.putChar(MsgCodes.DATA);
		packetBuffer.putChar(key);
		packetBuffer.putChar(downOrUp);

		byte[] packet = packetBuffer.array();

		try {
			out.write(packet);
			out.flush();
			//====================================FOR DEBUG==========================================
			System.out.println("(Client)sent(" + packet.length + "bytes)");
			//====================================FOR DEBUG==========================================
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//called when grab attack input is received
	public void sendInput(char key, float x, float y, char downOrUp) {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		packetBuffer.putChar(MsgCodes.DATA);
		packetBuffer.putChar(key);
		packetBuffer.putChar(downOrUp);
		packetBuffer.putFloat(x);
		packetBuffer.putFloat(y);

		byte[] packet = packetBuffer.array();

		try {
			out.write(packet);
			out.flush();
			//====================================FOR DEBUG==========================================
			System.out.println("(Client)sent(" + packet.length + "bytes)");
			//====================================FOR DEBUG==========================================
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//called when interact input is received
	public void sendInput(char key, int areaNum, int objectNum, char downOrUp) {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		packetBuffer.putChar(MsgCodes.DATA);
		packetBuffer.putChar(key);
		packetBuffer.putChar(downOrUp);
		packetBuffer.putInt(areaNum);
		packetBuffer.putInt(objectNum);

		//System.out.println("(Client)sent(" + packetBuffer.asCharBuffer() + ")");

		byte[] packet = packetBuffer.array();

		try {
			out.write(packet);
			out.flush();
			//====================================FOR DEBUG==========================================
			System.out.println("(Client)sent(" + packet.length + "bytes)");
			// System.out.println("(Client)sent(" + packetBuffer.asCharBuffer() + ")");
			//====================================FOR DEBUG==========================================
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		if (socket != null) {
			send(MsgCodes.MESSAGECODE, MsgCodes.Client.CLOSE_CONNECTION); //request to close connection
			socket.dispose();
		}
		Gdx.app.log("Client", "disposed");
	}
}
