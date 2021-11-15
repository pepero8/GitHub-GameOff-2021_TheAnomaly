package game;

public class BoxObject implements Interactable {
	static final int BOXOBJECT_WIDTH = 32;
	static final int BOXOBJECT_HEIGHT = 32;
	static final long BOXOBJECT_REQUIRE_TIME = 2000; //required time to finish interaction in milliseconds
	static int TOTAL_BOX_OBJECTS = 0;
	static int REMAINING_BOX_OBJECTS = 0;
	//static float SUCCESS_RATE;
	
	//private Interactable content;
	// private BoxObjectModel model;
	private float x;
	private float y;
	private String name; //needed?
	private int objectNum;

	private boolean interacted;

	BoxObject(float x, float y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
		// model = boxModel;
		TOTAL_BOX_OBJECTS++;
		REMAINING_BOX_OBJECTS++;
	}

	@Override
	public boolean interact(Player player) {
		//if (!interacted) {
			interacted = true;
			int hit = (int)(Math.random() * REMAINING_BOX_OBJECTS);
			System.out.println("hit: " + hit);
			if (hit == 0) {
				//player.setPossession(new CardKey());
				player.haveKey = true;
				REMAINING_BOX_OBJECTS--;
				return true;
			}
			REMAINING_BOX_OBJECTS--;
		//}
		return false;
	}

	// @Override
	// public boolean overlap(Player player) {
	// 	// TODO Auto-generated method stub
	// 	return (x <= (player.x + player.width)) && ((x + BOXOBJECT_WIDTH) >= (player.x))
	// 			&& (y <= (player.y + player.height)) && ((y + BOXOBJECT_HEIGHT) >= (player.y));
	// }
	@Override
	public boolean isContact(Player player, int range) {
		return (x < (player.x + player.width + range)) &&
			   ((x + BOXOBJECT_WIDTH) > (player.x - range)) &&
			   (y < (player.y + player.height + range)) &&
			   ((y + BOXOBJECT_HEIGHT) > (player.y - range));
	}

	@Override
	public boolean isContact(float x, float y) {
		return x > this.x && x < this.x + BOXOBJECT_WIDTH &&
			   y > this.y && y < this.y + BOXOBJECT_HEIGHT;
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
	public boolean isInteractable(int playerNum) {
		// TODO Auto-generated method stub
		return playerNum != 0;
	}

	@Override
	public boolean interacted() {
		return interacted;
	}

	@Override
	public long getRequireTime() {
		// TODO Auto-generated method stub
		return BOXOBJECT_REQUIRE_TIME;
	}
}
