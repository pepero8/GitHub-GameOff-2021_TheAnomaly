package game;

public interface Interactable {
	int INTERACTION_AVAILABLE_RANGE = 15;

	//boolean overlap(Player player);
	boolean isContact(Player player, int range);
	boolean isContact(float x, float y); // called to check whether projectile has touched the object
	boolean isInteractable(int playerNum);
	boolean interacted();
	boolean interacting();
	void setInteracting(boolean bool);
	boolean interact(Player player);
	float getX();
	float getY();
	float getWidth();
	float getHeight();
	long getRequireTime();
	void setNum(int num);
	int getNum();
}
