package game;

public class DoorObject implements Interactable {
	static final int DOOROBJECT_WIDTH = 256;
	static final int DOOROBJECT_HEIGHT = 32;
	static final long DOOROBJECT_REQUIRE_TIME = 50; // required time to finish interaction in milliseconds
													//minimum must be 17
	//static int TOTAL_DOOR_OBJECTS = 0;
	//static int REMAINING_DOOR_OBJECTS = 0;
	// static float SUCCESS_RATE;

	// private Interactable content;
	// private BoxObjectModel model;
	private float x;
	private float y;
	private int width;
	private int height;
	private String name; // needed?
	private int objectNum;

	private boolean interacted;
	private boolean open;

	DoorObject(float x, float y, int width, int height, String name) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		// model = boxModel;
		//TOTAL_DOOR_OBJECTS++;
		//REMAINING_DOOR_OBJECTS++;
	}

	@Override
	public boolean interact(Player player) {
		open = !open;

		// if (!interacted) {
		//interacted = true;
		//int hit = (int) (Math.random() * REMAINING_BOX_OBJECTS);
		//System.out.println("hit: " + hit);
		//if (hit == 0) {
			// player.setPossession(new CardKey());
		//	player.haveKey = true;
		//	REMAINING_BOX_OBJECTS--;
		//	return true;
		//}
		//REMAINING_BOX_OBJECTS--;
		// }
		return true;
	}

	// @Override
	// public boolean overlap(Player player) {
	// // TODO Auto-generated method stub
	// return (x <= (player.x + player.width)) && ((x + BOXOBJECT_WIDTH) >=
	// (player.x))
	// && (y <= (player.y + player.height)) && ((y + BOXOBJECT_HEIGHT) >=
	// (player.y));
	// }
	@Override
	public boolean isContact(Player player, int range) {
		//for interaction
		if (range > 0)
			return (x < (player.x + player.width + range)) && ((x + width) > (player.x - range))
				&& (y < (player.y + player.height + range)) && ((y + height) > (player.y - range));
		//for collision with player
		else if (!open) {
			return (x < (player.x + player.width)) && ((x + width) > (player.x))
				&& (y < (player.y + player.height)) && ((y + height) > (player.y));
		}
		return false;
	}

	@Override
	public boolean isContact(float x, float y) {
		if (!open)
			return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
		return false;
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
	public boolean isInteractable(int playerNum) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean interacted() {
		return interacted;
	}

	@Override
	public long getRequireTime() {
		// TODO Auto-generated method stub
		return DOOROBJECT_REQUIRE_TIME;
	}
}
