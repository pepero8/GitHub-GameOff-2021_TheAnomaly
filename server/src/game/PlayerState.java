package game;

public abstract class PlayerState {
	char code;

	void dodge() {}
	void attack() {}
	void grab(float cursorX, float cursorY) {}
	void rush() {}
	void interact(int playerNum, Interactable target) {}
	void haltInteract() {}

	/**
	 * @param progressTime
	 * @return false if this state's process is over
	 */
	abstract boolean update(long progressTime);

	abstract void reset();
}
