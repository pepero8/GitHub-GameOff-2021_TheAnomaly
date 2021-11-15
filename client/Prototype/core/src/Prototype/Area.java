package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public abstract class Area extends Actor implements Disposable {
	private Interactable[] objects;
	private int index = -1;

	private int areaNum;

	Area(int areaNum, int numObjects) {
		this.areaNum = areaNum;
		objects = new Interactable[numObjects];
	}

	public void addObject(Interactable object) {
		objects[++index] = object;
		//objects.add(object);
		object.setNum(index);
	}

	public Interactable[] getObjects() {
		return objects;
	}

	public int getNum() {
		return areaNum;
	}

	public Interactable checkCollision(Player player) {
		for (Interactable object : objects) {
			if (object.isContact(player, Interactable.INTERACTION_AVAILABLE_RANGE)) {
				return object;
			}
		}

		return null;
	}

	public abstract Area determineArea(float x, float y);

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		Gdx.app.log("Area", "disposed");
	}
	
}
