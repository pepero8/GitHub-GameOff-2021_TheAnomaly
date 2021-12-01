package game;

public class CardKeyObject implements Interactable {
	static final int CARDKEYOBJECT_WIDTH = 16;
	static final int CARDKEYOBJECT_HEIGHT = 16;
	static final long CARDKEYOBJECT_REQUIRE_TIME = 1;

	private float x;
	private float y;
	private int width;
	private int height;
	private int objectNum;
	private Area area;

	private boolean interacted;
	private boolean interacting;

	CardKeyObject(Area area, float x, float y, int width, int height, String name/*needed?*/) {
		this.area = area;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean isContact(Player player, int range) {
		// TODO Auto-generated method stub

		//for interaction
		if (range > 0)
			return (x < (player.x + player.width + range)) && ((x + width) > (player.x - range))
				&& (y < (player.y + player.height + range)) && ((y + height) > (player.y - range));
		return false;
	}

	@Override
	public boolean isContact(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInteractable(int playerNum) {
		// TODO Auto-generated method stub
		return playerNum != 0;
	}

	@Override
	public boolean interacted() {
		// TODO Auto-generated method stub
		return interacted;
	}

	@Override
	public boolean interact(Player player) {
		// TODO Auto-generated method stub
		area.removeObject(objectNum);
		player.haveKey = true;
		return true;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public float getY() {
		// TODO Au to-generated method stub
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
	public long getRequireTime() {
		// TODO Auto-generated method stub
		return CARDKEYOBJECT_REQUIRE_TIME;
	}

	public Area getArea() {
		return area;
	}

	@Override
	public void setNum(int num) {
		// TODO Auto-generated method stub
		this.objectNum = num;
	}

	@Override
	public int getNum() {
		// TODO Auto-generated method stub
		return objectNum;
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
