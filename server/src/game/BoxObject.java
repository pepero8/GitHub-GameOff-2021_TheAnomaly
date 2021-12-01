package game;

public class BoxObject implements Interactable {
	static final int BOXOBJECT_WIDTH = 32;
	static final int BOXOBJECT_HEIGHT = 32;
	static final long BOXOBJECT_REQUIRE_TIME = 2000; //required time to finish interaction in milliseconds
	
	Map map;
	
	private float x;
	private float y;
	private int width;
	private int height;
	private int objectNum;

	private boolean interacted;
	private boolean interacting;

	BoxObject(Map map, float x, float y, int width, int height, String name/*needed?*/) {
		this.map = map;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		map.REMAINING_BOX_OBJECTS++;
	}

	@Override
	public boolean interact(Player player) {
		interacted = true;
		int trial = (int)(Math.random() * map.REMAINING_BOX_OBJECTS);
		System.out.println("trial: " + trial + ", HIT: " + map.HIT);
		if (trial == map.HIT) {
			player.haveKey = true;
			map.HIT = -1; //no more keys
			return true;
		}
		map.REMAINING_BOX_OBJECTS--;

		return false;
	}

	@Override
	public boolean isContact(Player player, int range) {
		return (x < (player.x + player.width + range)) &&
			   ((x + width) > (player.x - range)) &&
			   (y < (player.y + player.height + range)) &&
			   ((y + height) > (player.y - range));
	}

	@Override
	public boolean isContact(float x, float y) {
		return x > this.x && x < this.x + width &&
			   y > this.y && y < this.y + height;
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

	@Override
	public boolean interacting() {
		// TODO Auto-generated method stub
		return interacting;
	}

	@Override
	public void setInteracting(boolean bool) {
		// TODO Auto-generated method stub
		interacting = bool;
	}
}
