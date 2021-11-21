package Prototype;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class WallObject extends Actor implements Interactable {

	private int objectNum;

	WallObject(float x, float y, int width, int height) {
		setBounds(x, y, width, height);
	}

	@Override
	public boolean isContact(Player player, int range) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInteracting(boolean bool) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInteracted(boolean interacted, boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInteracting() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean interacted() {
		// TODO Auto-generated method stub
		return false;
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
	
}
