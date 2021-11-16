package Prototype;

public class DoorObject implements Interactable {
	static final int DOOROBJECT_WIDTH = 256;
	static final int DOOROBJECT_HEIGHT = 32;

	//private Interactable content;
	// private BoxObjectModel model;
	private float x;
	private float y;
	private int width;
	private int height;
	private String name;
	private int objectNum;

	private boolean interacting;
	private boolean interacted;

	DoorObject(float x, float y, int width, int height, String name) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		//model = boxModel;
	}

	@Override
	public boolean isContact(Player player, int range) {
		return (x < (player.getX() + player.getWidth() + range)) && ((x + width) > (player.getX() - range))
				&& (y < (player.getY() + player.getHeight() + range))
				&& ((y + height) > (player.getY() - range));
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
		return width;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return height;
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
