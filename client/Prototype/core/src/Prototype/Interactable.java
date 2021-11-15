package Prototype;

public interface Interactable {
	int INTERACTION_AVAILABLE_RANGE = 16;
	boolean isContact(Player player, int range);
	//boolean overlap(Player player);
	//Interactable interact();
	void setInteracting(boolean bool);
	void setInteracted(boolean bool);
	boolean isInteracting();
	boolean interacted();
	float getX();
	float getY();
	String getName();
	float getWidth();
	float getHeight();
	void setNum(int num);
	int getNum();
}
