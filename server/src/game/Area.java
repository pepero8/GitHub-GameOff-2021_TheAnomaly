package game;

public abstract class Area {
	private Interactable[] objects;
	private int index = -1;

	private int areaNum;

	private float x;
	private float y;
	private int width;
	private int height;

	Area(float x, float y, int width, int height, int areaNum, int numObjects) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.areaNum = areaNum;
		objects = new Interactable[numObjects];
	}

	void addObject(Interactable object) {
		objects[++index] = object;
		object.setNum(index);
	}

	Interactable[] getObjects() {
		return objects;
	}

	boolean hitWall(float objX, float objY) {
		return objX <= x || objX >= x + width || objY <= y || objY >= y + height;
	}

	boolean hitObject(float x, float y) {
		for (Interactable obj : objects) {
			if (obj.isContact(x, y)) {
				return true;
			}
		}
		return false;
	}

	boolean hitObject(Player player) {
		for (Interactable obj : objects) {
			if (obj.isContact(player, 0)) {
				return true;
			}
		}
		return false;
	}

	abstract Area determineArea(float x, float y);
}
