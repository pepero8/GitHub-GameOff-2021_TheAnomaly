package game;

public class GateObject implements Interactable {
	static final int GATEOBJECT_WIDTH = 256;
	static final int GATEOBJECT_HEIGHT = 64;
	static final long GATEOBJECT_REQUIRE_TIME = 50; // required time to finish interaction in milliseconds
													// minimum must be 17

	private float x;
	private float y;
	private int width;
	private int height;
	private int objectNum;

	private boolean interacted;
	private boolean interacting;
	private boolean open;

	GateObject(float x, float y, int width, int height, String name/*needed?*/) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean interact(Player player) {
		if (player.haveKey) {
			open = true;
			interacted = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean isContact(Player player, int range) {
		// for interaction
		if (range > 0)
			return (x < (player.x + player.width + range)) && ((x + width) > (player.x - range))
					&& (y < (player.y + player.height + range)) && ((y + height) > (player.y - range));
		// for collision with player
		else if (!open) {
			return (x < (player.x + player.width)) && ((x + width) > (player.x)) && (y < (player.y + player.height))
					&& ((y + height) > (player.y));
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
		return playerNum != 0;
	}

	@Override
	public boolean interacted() {
		return interacted;
	}

	@Override
	public long getRequireTime() {
		// TODO Auto-generated method stub
		return GATEOBJECT_REQUIRE_TIME;
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
