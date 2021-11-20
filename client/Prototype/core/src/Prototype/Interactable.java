package Prototype;

public interface Interactable {
	int INTERACTION_AVAILABLE_RANGE = 10;
	boolean isContact(Player player, int range);
	//boolean overlap(Player player);
	//Interactable interact();
	void setInteracting(boolean bool);
	void setInteracted(boolean interacted, boolean success);
	boolean isInteracting();
	boolean interacted();
	//float getX();
	//float getY();
	//String getName();
	//float getWidth();
	//float getHeight();
	void setNum(int num);
	int getNum();
}
