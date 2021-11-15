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
	}

	public class Game {
		// keyboard keys
		public static final char UP = 'n';
		public static final char DOWN = 's';
		public static final char LEFT = 'l';
		public static final char RIGHT = 'r';
		public static final char DODGE = 'd';
		public static final char ATTACK = 'a';
		public static final char GRAB = 'g';
		public static final char RUSH = 'u';
		public static final char INTERACT = 'i';

		public static final char KEY_UP = 'u'; // key released
		public static final char KEY_DOWN = 'd'; // key pressed

		// player's states
		public static final char NORMAL_STATE = 'n';
		public static final char DODGE_STATE = 'd';
		public static final char ATTACK_STATE = 'a';
		public static final char GRABBING_STATE = 'g';
		public static final char ATTACK_GRABBING_STATE = 't';
		public static final char DEAD_STATE = 'k';
		public static final char DRAGGED_STATE = 'r';
		public static final char RUSH_STATE = 'u';
		public static final char INTERACT_STATE = 'i';
		public static final char INTERACT_SUCCESS_STATE = 's';
		public static final char INTERACT_FAILED_STATE = 'f';

		// player's direction
		public static final char DIRECTION_NORTH = '0';
		public static final char DIRECTION_NORTH_EAST = '1';
		public static final char DIRECTION_EAST = '2';
		public static final char DIRECTION_SOUTH_EAST = '3';
		public static final char DIRECTION_SOUTH = '4';
		public static final char DIRECTION_SOUTH_WEST = '5';
		public static final char DIRECTION_WEST = '6';
		public static final char DIRECTION_NORTH_WEST = '7';

		public static final char HAS_KEY = 'y';
		public static final char NO_KEY = 'n';
	}
}