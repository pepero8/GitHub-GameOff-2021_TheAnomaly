package Prototype;

public class BoxObject implements Interactable {
	static final int BOXOBJECT_WIDTH = 32;
	static final int BOXOBJECT_HEIGHT = 32;

	private Interactable content;
	//private BoxObjectModel model;
	private float x;
	private float y;
	private String name;
	private int objectNum;

	private boolean interacting;
	private boolean interacted;

	BoxObject(float x, float y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
		//model = boxModel;
	}

	// @Override
	// public Interactable interact() {
	// 	if (!interacted) {
	// 		interacted = true;
	// 		return content;
	// 	}
	// 	return null;
	// }

	// @Override
	// public boolean overlap(Player player) {
	// 	// TODO Auto-generated method stub
	// 	return (x <= (player.getX() + player.getWidth())) &&
	// 		   ((x + BOXOBJECT_WIDTH) >= (player.getX())) &&
	// 		   (y <= (player.getY() + player.getHeight())) &&
	// 		   ((y + BOXOBJECT_HEIGHT) >= (player.getY()));
	// }

	@Override
	public boolean isContact(Player player, int range) {
		return (x < (player.getX() + player.getWidth() + range)) &&
			   ((x + BOXOBJECT_WIDTH) > (player.getX() - range)) &&
			   (y < (player.getY() + player.getHeight() + range)) &&
			   ((y + BOXOBJECT_HEIGHT) > (player.getY() - range));
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return BOXOBJECT_WIDTH;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return BOXOBJECT_HEIGHT;
	}

	@Override
	public void setNum(int num) {
		// TODO Auto-generated method stub
		objectNum = num;
	}
	
	@Override
	public int getNum() {
		// TODO Auto-generated method stub
		return objectNum;
	}

	@Override
	public void setInteracting(boolean bool) {
		// TODO Auto-generated method stub
		interacting = bool;
	}

	@Override
	public void setInteracted(boolean bool) {
		// TODO Auto-generated method stub
		interacted = bool;
	}

	@Override
	public boolean isInteracting() {
		// TODO Auto-generated method stub
		return interacting;
	}

	@Override
	public boolean interacted() {
		// TODO Auto-generated method stub
		return interacted;
	}
}
