package Prototype;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

public class DoorObject extends Actor implements Interactable {
	static final int DOOROBJECT_WIDTH = 128;
	static final int DOOROBJECT_HEIGHT = 64;
	static final float DOOROBJECT_ANIMATION_DURATION = 0.6f;
	static final float DOOROBJECT_FRAME_DURATION = 0.2f;

	private Animation<TextureRegion> animation;
	private TextureRegion curFrame;
	private int objectNum;

	private boolean interacting;
	private boolean interacted;
	private boolean opened;

	private float stateTime;

	DoorObject(float x, float y, int width, int height, String name, Animation<TextureRegion> anim) {
		setBounds(x, y, width, height);
		setName(name);
		animation = anim;

		stateTime = DOOROBJECT_ANIMATION_DURATION;
		curFrame = animation.getKeyFrame(stateTime);

		RepeatAction loop = new RepeatAction();
		loop.setCount(RepeatAction.FOREVER);
		loop.setAction(new Action() {
			@Override
			public boolean act(float delta) {
				updateAnimation(delta);
				
				return true;
			}

		});

		addAction(loop);
	}

	@Override
	public boolean isContact(Player player, int range) {
		//for interaction
		float x = getX();
		float y = getY();
		return ((x > (player.getX() + player.getWidth())) || ((x + getWidth()) < (player.getX()))
				|| (y > (player.getY() + player.getHeight())) || ((y + getHeight()) < (player.getY())))
				&& (x < (player.getX() + player.getWidth() + range)) && ((x + getWidth()) > (player.getX() - range))
				&& (y < (player.getY() + player.getHeight() + range))
				&& ((y + getHeight()) > (player.getY() - range));
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

	private void updateAnimation(float delta) {
		if (stateTime >= DOOROBJECT_ANIMATION_DURATION && interacting) {
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
