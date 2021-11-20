package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class DoorObject extends Actor implements Interactable {
	static final int DOOROBJECT_WIDTH = 256;
	static final int DOOROBJECT_HEIGHT = 32;
	static final float DOOROBJECT_ANIMATION_DURATION = 0.6f;
	static final float DOOROBJECT_FRAME_DURATION = 0.2f;

	private Animation<TextureRegion> animation;
	private TextureRegion curFrame;
	//private Interactable content;
	// private BoxObjectModel model;
	// private float x;
	// private float y;
	// private int width;
	// private int height;
	//private String name;
	private int objectNum;

	private boolean interacting;
	//private boolean prevInteracting;
	private boolean interacted;
	private boolean opened;

	private float stateTime;

	DoorObject(float x, float y, int width, int height, String name, Animation<TextureRegion> anim) {
		setBounds(x, y, width, height);
		setName(name);
		// this.x = x;
		// this.y = y;
		// this.width = width;
		// this.height = height;
		// this.name = name;
		//model = boxModel;
		animation = anim;

		stateTime = DOOROBJECT_ANIMATION_DURATION;
		curFrame = animation.getKeyFrame(stateTime);

		RepeatAction loop = new RepeatAction();
		loop.setCount(RepeatAction.FOREVER);
		loop.setAction(new Action() {
			@Override
			public boolean act(float delta) {
				//Gdx.app.log("DoorObject", "updating animation");
				updateAnimation(delta);
				
				
				return true;
			}

		});

		addAction(loop);
	}

	@Override
	public boolean isContact(Player player, int range) {
		float x = getX();
		float y = getY();
		return (x < (player.getX() + player.getWidth() + range)) && ((x + getWidth()) > (player.getX() - range))
				&& (y < (player.getY() + player.getHeight() + range))
				&& ((y + getHeight()) > (player.getY() - range));
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

	private void updateAnimation(float delta) {
		//Gdx.app.log("DoorObject", "" + interacting);
		if (stateTime >= DOOROBJECT_ANIMATION_DURATION && interacting) {
			//Gdx.app.log("DoorObject", "opening");
			//prevInteracting = interacting;
			stateTime = 0f;
			opened = !opened;
			animation.setPlayMode((opened) ? PlayMode.REVERSED : PlayMode.NORMAL);
		}

		if (stateTime < DOOROBJECT_ANIMATION_DURATION) {
			stateTime += delta;
			curFrame = animation.getKeyFrame(stateTime, false);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(curFrame, getX(), getY());
	}
}
