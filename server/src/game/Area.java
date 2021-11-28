package game;

import java.util.ArrayList;

public abstract class Area {
	private Map map;
	//private Interactable[] objects;
	private ArrayList<Interactable> objects;
	private int index = -1;

	private int areaNum;

	protected float x;
	protected float y;
	private int width;
	private int height;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param areaNum
	 * @param numObjects number of objects in this area. It should have an empty room for the card key
	 */
	Area(Map map, float x, float y, int width, int height, int areaNum, int numObjects/*needed?*/) {
		this.map = map;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.areaNum = areaNum;
		objects = new ArrayList<Interactable>();
		//objects = new Interactable[numObjects];
	}

	void addObject(Interactable object) {
		//objects[++index] = object;
		objects.add(++index, object);
		object.setNum(index);

		if (object instanceof CardKeyObject) {
			map.cardKey = (CardKeyObject)object;
		}
	}

	//called only for card key
	Interactable removeObject(int objectNum) {
		Interactable ret = objects.remove(objectNum);
		index--;
		if (ret instanceof CardKeyObject) {
			map.cardKey = null;
		}
		return ret;
	}

	// Interactable[] getObjects() {
	// 	return objects;
	// }

	ArrayList<Interactable> getObjects() {
		return objects;
	}

	int getNumber() {
		return areaNum;
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
	
	public Area determineArea(Player player) {
		return determineArea(player.x, player.y);
	}
}
