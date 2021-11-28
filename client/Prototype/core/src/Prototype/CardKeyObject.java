package Prototype;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class CardKeyObject extends Image implements Interactable {
	static final int CARDKEYOBJECT_WIDTH = 16;
	static final int CARDKEYOBJECT_HEIGHT = 16;

	private World world;
	private Area area;

	// private Interactable content;
	// private BoxObjectModel model;
	// private float x;
	// private float y;
	// private int width;
	// private int height;
	// private String name;
	private int objectNum;

	private boolean interacting;
	private boolean interacted;

	CardKeyObject(World world, Area area, float x, float y, int width, int height, String name, Texture image) {
		super(image);
		this.world = world;
		this.area = area;
		setBounds(x, y, width, height);
		setName(name);
		// this.x = x;
		// this.y = y;
		// this.width = width;
		// this.height = height;
		// this.name = name;
		//model = boxModel;
	}

	@Override
	public boolean isContact(Player player, int range) {
		float x = getX();
		float y = getY();
		return (x < (player.getX() + player.getWidth() + range)) && ((x + getWidth()) > (player.getX() - range))
				&& (y < (player.getY() + player.getHeight() + range))
				&& ((y + getHeight()) > (player.getY() - range));
	}

	// public void setPosition(float x, float y) {
	// 	// this.x = x;
	// 	// this.y = y;
	// }

	public void setArea(int areaNum) {
		if (area == null && areaNum == -1) {

		}
		else if (area == null && areaNum != -1) {
			area = world.areas[areaNum];
			area.addObject(this);
		}
		else if (area != null && areaNum == -1) {
			area.removeLast();
			area = null;
		}
		else if (area != null && areaNum != -1) {
			area.removeLast();
			area = world.areas[areaNum];
			area.addObject(this);
		}
	}

	@Override
	public void setInteracting(boolean bool) {
		// TODO Auto-generated method stub
		interacting = bool;
		
	}

	@Override
	public void setInteracted(boolean interacted, boolean success) {
		// TODO Auto-generated method stub
		//this.interacted = interacted;
	}

	@Override
	public boolean isInteracting() {
		// TODO Auto-generated method stub
		return interacting;
	}

	@Override
	public boolean interacted() {
		// TODO Auto-generated method stub
		return interacted;
	}

	// @Override
	// public float getX() {
	// 	// TODO Auto-generated method stub
	// 	return x;
	// }

	// @Override
	// public float getY() {
	// 	// TODO Auto-generated method stub
	// 	return y;
	// }

	// @Override
	// public String getName() {
	// 	// TODO Auto-generated method stub
	// 	return name;
	// }

	// @Override
	// public float getWidth() {
	// 	// TODO Auto-generated method stub
	// 	return width;
	// }

	// @Override
	// public float getHeight() {
	// 	// TODO Auto-generated method stub
	// 	return height;
	// }

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
	public void draw(Batch batch, float parentAlpha) {
		// if (nearby) {
		// 	//batch.setColor(Color.YELLOW);
		// 	nearby = false;
		// }
		super.draw(batch, parentAlpha);
		//batch.setColor(Color.CLEAR);
		//batch.setColor(1, 1, 1, 1);
	}
}
