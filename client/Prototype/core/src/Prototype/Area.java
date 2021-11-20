package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public abstract class Area extends Image {
	private Interactable[] objects;
	private int index = -1;

	private int areaNum;

	Area(int areaNum, int numObjects, Texture image) {
		super(image);
		this.areaNum = areaNum;
		objects = new Interactable[numObjects];

		RepeatAction loop = new RepeatAction();
		loop.setCount(RepeatAction.FOREVER);
		loop.setAction(new Action() {
			@Override
			public boolean act(float delta) {
				for (Interactable obj : objects) {
					if (obj != null) {
						((Actor)obj).act(delta);
					}
				}

				return true;
			}

		});

		addAction(loop);
	}

	public void addObject(Interactable object) {
		objects[++index] = object;
		//objects.add(object);
		object.setNum(index);
	}

	//called for removing card key
	public void removeLast() {
		index--;
	}

	public Interactable[] getObjects() {
		return objects;
	}

	public int getNum() {
		return areaNum;
	}

	public Interactable checkCollision(Player player) {
		//returns card key first if exists
		for (int i = objects.length - 1; i != -1; i--) {
		//for (Interactable object : objects) {
			if (objects[i] != null && objects[i].isContact(player, Interactable.INTERACTION_AVAILABLE_RANGE)) {
				return objects[i];
			}
		}

		return null;
	}

	public abstract Area determineArea(float x, float y);

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		for (int i = 0; i != objects.length - 1; i++) {
			if (objects[i] != null) {
				((Actor)objects[i]).draw(batch, parentAlpha);
			}
		}
	}

	// @Override
	// public void dispose() {
	// 	// TODO Auto-generated method stub
	// 	Gdx.app.log("Area", "disposed");
	// }
	
}
