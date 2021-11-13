package game;

public abstract class PlayerState {
	char code;

	void dodge() {}
	void attack() {}
	void grab(float cursorX, float cursorY) {}
	void rush() {}

	/**
	 * @param progressTime
	 * @return false if this state's process is over
	 */
	abstract boolean update(long progressTime);
}
