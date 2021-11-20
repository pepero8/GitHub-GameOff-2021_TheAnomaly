package Prototype;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class BoxObject extends Image implements Interactable {
	static final int BOXOBJECT_WIDTH = 32;
	static final int BOXOBJECT_HEIGHT = 32;

	//private Interactable content;
	//private BoxObjectModel model;
	//private float x;
	//private float y;
	//private int width;
	//private int height;
	//private String name;
	private int objectNum;

	private boolean interacting;
	private boolean interacted;

	BoxObject(float x, float y, int width, int height, String name, Texture image) {
		super(image);
		setBounds(x, y, width, height);
		setName(name);
		// this.x = x;
		// this.y = y;
		// this.width = width;
		// this.height = height;
		// this.name = name;
		//model = boxModel;
	}

	// @Override
	// public Interactable interact() {
	// 	if (!interacted) {
	// 		interacted = true;
	// 		return content;
	// 	}
	// 	return null;
	// }

	// @Override
	// public boolean overlap(Player player) {
	// 	// TODO Auto-generated method stub
	// 	return (x <= (player.getX() + player.getWidth())) &&
	// 		   ((x + BOXOBJECT_WIDTH) >= (player.getX())) &&
	// 		   (y <= (player.getY() + player.getHeight())) &&
	// 		   ((y + BOXOBJECT_HEIGHT) >= (player.getY()));
	// }

	@Override
	public boolean isContact(Player player, int range) {
		float x = getX();
		float y = getY();
		return (x < (player.getX() + player.getWidth() + range)) &&
			   ((x + getWidth()) > (player.getX() - range)) &&
			   (y < (player.getY() + player.getHeight() + range)) &&
			   ((y + getHeight()) > (player.getY() - range));
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
	public void setInteracting(boolean bool) {
		// TODO Auto-generated method stub
		interacting = bool;
	}

	@Override
	public void setInteracted(boolean interacted, boolean success) {
		// TODO Auto-generated method stub
		this.interacted = interacted;
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
}
