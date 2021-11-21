package game;

public class WallObject implements Interactable {

	private float x;
	private float y;
	private int width;
	private int height;
	private int objectNum;

	WallObject(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean isContact(Player player, int range) {
		// TODO Auto-generated method stub
		if (range == 0)
			return (x < (player.x + player.width)) &&
			   	   ((x + width) > (player.x)) &&
			   	   (y < (player.y + player.height)) &&
			   	   ((y + height) > (player.y));
		return false;
	}

	@Override
	public boolean isContact(float x, float y) {
		// TODO Auto-generated method stub
		return x > this.x && x < this.x + width && y > this.y && y < this.y + height;
	}

	@Override
	public boolean isInteractable(int playerNum) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean interacted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean interact(Player player) {
		// TODO Auto-generated method stub
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
	public long getRequireTime() {
		// TODO Auto-generated method stub
		return 0;
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
	public boolean interacting() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInteracting(boolean bool) {
		// TODO Auto-generated method stub
		
	}
	
}
