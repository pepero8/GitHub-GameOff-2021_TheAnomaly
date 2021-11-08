package net;

public class MsgCodes {
	public static final char MESSAGECODE = 'm';
	public static final char DATA = 'd';

	// server message code
	public class Server {
		public static final char SERVER_FULL = 'f';
		public static final char SESSION_START_PLAYER0 = '0';
		public static final char SESSION_START_PLAYER1 = '1';
		public static final char SESSION_START_PLAYER2 = '2';
		public static final char SESSION_START_PLAYER3 = '3';
		public static final char SESSION_START_PLAYER4 = '4';
		public static final char SESSION_TERMINATE_OD = 't'; // opponent player disconnected
	}

	// client message code
	public class Client {
		public static final char CLOSE_CONNECTION = 'c';
		public static final char UP = 'n';
		public static final char DOWN = 's';
		public static final char LEFT = 'l';
		public static final char RIGHT = 'r';
		public static final char KEY_UP = 'u'; //key released
		public static final char KEY_DOWN = 'd'; //key pressed
	}
}