package Prototype;

public interface Interactable {
	int INTERACTION_AVAILABLE_RANGE = 15;
	
	boolean isContact(Player player, int range);
	void setInteracting(boolean bool);
	void setInteracted(boolean interacted, boolean success);
	boolean isInteracting();
	boolean interacted();
	void setNum(int num);
	int getNum();
}
