/**
 *	Copyright 2021 Jaehwan Lee

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	<http://www.apache.org/licenses/LICENSE-2.0>
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
	private static final String SERVER_IP = "13.125.202.253"; //test server
	//private static final String SERVER_IP = "3.144.77.66";
	//private static final String SERVER_IP = "192.168.123.106";
	private static final int SERVER_PORT = 8014;
	private static final int PACKET_SIZE = 108;

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
		
		//int read_ch = 0;
		try {
			socket = Gdx.net.newClientSocket(Protocol.TCP, SERVER_IP, SERVER_PORT, null);
			in = socket.getInputStream();
			out = socket.getOutputStream();

			sendPlayerName();
			
			//long cur, latency;
			while(true) {
				//cur = System.currentTimeMillis();
				byte[] netResponse = in.readNBytes(PACKET_SIZE);
				//latency = TimeUtils.timeSinceMillis(cur);
				//System.out.println("[Client] latency: " + latency + "ms");

				//read_ch = in.read(netResponse);
				// if (read_ch != PACKET_SIZE)
				// 	System.out.println("read bytes: " + read_ch);
				//if (read_ch == -1) break;
				if (netResponse == null) break;
				//if (read_ch == PACKET_SIZE)
					handleResponse(netResponse);
				//emptyMsgBuffer();
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
		//System.out.println("===========================");
		ByteBuffer packetBuffer = ByteBuffer.wrap(packet);
		char msgType = packetBuffer.getChar();
		//System.out.println("msgCode = " + msgType);

		//lot of duplicate codes here
		if (msgType == MsgCodes.INPUT) {

			packetBuffer.getChar();
			//robot
			float x = packetBuffer.getFloat();
			//System.out.println("robot x: " + x);
			float y = packetBuffer.getFloat();
			//System.out.println("robot  : (" + x + ", " + y + ")");
			float projectileX = 0;
			float projectileY = 0;
			char state = packetBuffer.getChar();
			if (state == MsgCodes.Game.GRABBING_STATE || state == MsgCodes.Game.ATTACK_GRABBING_STATE) {
				projectileX = packetBuffer.getFloat();
				projectileY = packetBuffer.getFloat();
			}
			char direction = packetBuffer.getChar();
			game.world.robot.accessPosition("set", x, y);
			game.world.robot.accessState("set", state);
			game.world.robot.accessProjectilePos("set", projectileX, projectileY);
			game.world.robot.accessDirection("set", direction);

			packetBuffer.getChar();
			//player1
			x = packetBuffer.getFloat();
			y = packetBuffer.getFloat();
			//System.out.println("player1: (" + x + ", " + y + ")");
			state = packetBuffer.getChar();
			char hasKey = packetBuffer.getChar();
			direction = packetBuffer.getChar();
			game.world.player1.accessPosition("set", x, y);
			game.world.player1.accessState("set", state);
			game.world.player1.accessHasKey("set", hasKey == MsgCodes.Game.HAS_KEY);
			game.world.player1.accessDirection("set", direction);

			packetBuffer.getChar();
			// player2
			x = packetBuffer.getFloat();
			y = packetBuffer.getFloat();
			//System.out.println("player2: (" + x + ", " + y + ")");
			state = packetBuffer.getChar();
			hasKey = packetBuffer.getChar();
			direction = packetBuffer.getChar();
			game.world.player2.accessPosition("set", x, y);
			game.world.player2.accessState("set", state);
			game.world.player2.accessHasKey("set", hasKey == MsgCodes.Game.HAS_KEY);
			game.world.player2.accessDirection("set", direction);

			packetBuffer.getChar();
			// player3
			x = packetBuffer.getFloat();
			y = packetBuffer.getFloat();
			//System.out.println("player3: (" + x + ", " + y + ")");
			state = packetBuffer.getChar();
			hasKey = packetBuffer.getChar();
			direction = packetBuffer.getChar();
			game.world.player3.accessPosition("set", x, y);
			game.world.player3.accessState("set", state);
			game.world.player3.accessHasKey("set", hasKey == MsgCodes.Game.HAS_KEY);
			game.world.player3.accessDirection("set", direction);

			packetBuffer.getChar();
			// player4
			x = packetBuffer.getFloat();
			y = packetBuffer.getFloat();
			//System.out.println("player4: (" + x + ", " + y + ")");
			state = packetBuffer.getChar();
			hasKey = packetBuffer.getChar();
			direction = packetBuffer.getChar();
			game.world.player4.accessPosition("set", x, y);
			game.world.player4.accessState("set", state);
			game.world.player4.accessHasKey("set", hasKey == MsgCodes.Game.HAS_KEY);
			game.world.player4.accessDirection("set", direction);

			//card key
			float cardKeyX = packetBuffer.getFloat();
			float cardKeyY = packetBuffer.getFloat();
			int areaNum = packetBuffer.getInt();
			game.world.cardKey.setPosition(cardKeyX, cardKeyY);
			game.world.cardKey.setArea(areaNum);

			//remaining time
			long remainingTime = packetBuffer.getLong();
			game.world.setRemainTime(remainingTime);
		}
		else if (msgType == MsgCodes.MESSAGECODE) {
			char msgCode = packetBuffer.getChar();
			if (msgCode == MsgCodes.Server.SESSION_TERMINATE_OD) {
				//Gdx.app.log("Client", "Session terminated[SESSION_TERMINATE_OD]");
				game.sessionStart = false;
			}
			else if (msgCode == MsgCodes.Server.DISCONNECT_ROBOT) {
				game.world.robot.disconnected = true;
				game.sessionStart = false;
			}
			else if (msgCode == MsgCodes.Server.DISCONNECT_PLAYER1) {
				game.world.player1.disconnected = true;
			}
			else if (msgCode == MsgCodes.Server.DISCONNECT_PLAYER2) {
				game.world.player2.disconnected = true;
			}
			else if (msgCode == MsgCodes.Server.DISCONNECT_PLAYER3) {
				game.world.player3.disconnected = true;
			}
			else if (msgCode == MsgCodes.Server.DISCONNECT_PLAYER4) {
				game.world.player4.disconnected = true;
			}
			else if (msgCode == MsgCodes.Server.SESSION_TERMINATE_GAMEOVER_ROBOT_WIN ||
					 msgCode == MsgCodes.Server.SESSION_TERMINATE_GAMEOVER_SURVIVORS_WIN) {
				//Gdx.app.log("Client", "Session terminate[GAMEOVER]: " + msgCode);
				game.gameEndCode = msgCode;
				game.sessionStart = false;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER0) {
				game.sessionStart = true;
				game.playerNum = 0;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER1) {
				game.sessionStart = true;
				game.playerNum = 1;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER2) {
				game.sessionStart = true;
				game.playerNum = 2;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER3) {
				game.sessionStart = true;
				game.playerNum = 3;
			}
			else if (msgCode == MsgCodes.Server.SESSION_START_PLAYER4) {
				game.sessionStart = true;
				game.playerNum = 4;
			}
			else if (msgCode == MsgCodes.Server.START_GAME) {
				game.gameStart = true;
			}
		}
		else if (msgType == MsgCodes.DATA) {
			try {
				byte[] nextPlayerName = new byte[Prototype.MAX_PLAYERNAME_LEN*2];

				try {
					packetBuffer.get(nextPlayerName);
					String playername = new String(nextPlayerName, "UTF-8");
					//System.out.println("next playername: " + playername);
					game.world.robot.setName(playername);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					game.world.robot.setName(new String(nextPlayerName));
				}
				
				try {
					packetBuffer.get(nextPlayerName);
					String playername = new String(nextPlayerName, "UTF-8");
					//System.out.println("next playername: " + playername);
					game.world.player1.setName(playername);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					game.world.player1.setName(new String(nextPlayerName));
				}

				try {
					packetBuffer.get(nextPlayerName);
					String playername = new String(nextPlayerName, "UTF-8");
					//System.out.println("next playername: " + playername);
					game.world.player2.setName(playername);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					game.world.player2.setName(new String(nextPlayerName));
				}

				try {
					packetBuffer.get(nextPlayerName);
					String playername = new String(nextPlayerName, "UTF-8");
					//System.out.println("next playername: " + playername);
					game.world.player3.setName(playername);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					game.world.player3.setName(new String(nextPlayerName));
				}

				try {
					packetBuffer.get(nextPlayerName);
					String playername = new String(nextPlayerName, "UTF-8");
					//System.out.println("next playername: " + playername);
					game.world.player4.setName(playername);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					game.world.player4.setName(new String(nextPlayerName));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("[Client] invalid packet");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendInput(char key, char downOrUp) {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		packetBuffer.putChar(MsgCodes.INPUT);
		packetBuffer.putChar(key);
		packetBuffer.putChar(downOrUp);

		byte[] packet = packetBuffer.array();

		try {
			out.write(packet);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//called when grab attack input is received
	public void sendInput(char key, float x, float y, char downOrUp) {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		packetBuffer.putChar(MsgCodes.INPUT);
		packetBuffer.putChar(key);
		packetBuffer.putChar(downOrUp);
		packetBuffer.putFloat(x);
		packetBuffer.putFloat(y);

		byte[] packet = packetBuffer.array();

		try {
			out.write(packet);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//called when interact input is received
	public void sendInput(char key, int areaNum, int objectNum, char downOrUp) {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		packetBuffer.putChar(MsgCodes.INPUT);
		packetBuffer.putChar(key);
		packetBuffer.putChar(downOrUp);
		packetBuffer.putInt(areaNum);
		packetBuffer.putInt(objectNum);

		//System.out.println("(Client)sent(" + packetBuffer.asCharBuffer() + ")");

		byte[] packet = packetBuffer.array();

		try {
			out.write(packet);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendPlayerName() {
		ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE);
		//ByteBuffer packetBuffer = ByteBuffer.allocate(PACKET_SIZE_PLAYERNAME);
		packetBuffer.putChar(MsgCodes.DATA);
		packetBuffer.put(game.playerName.getBytes());

		byte[] packet = packetBuffer.array();

		try {
			out.write(packet);
			out.flush();
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
		//Gdx.app.log("Client", "disposed");
	}
}
