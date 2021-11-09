package game;

public abstract class PlayerState {
	char code;

	abstract void update(long progressTime);
}
