/**
 *	Copyright 2021 Jaehwan Lee

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	<http://www.apache.org/licenses/LICENSE-2.0>
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package Prototype;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

public class GateObject extends Actor implements Interactable {
	static final int GATEOBJECT_WIDTH = 256;
	static final int GATEOBJECT_HEIGHT = 64;
	static final float GATEOBJECT_ANIMATION_DURATION = 0.6f;
	static final float GATEOBJECT_FRAME_DURATION = 0.2f;

	private Animation<TextureRegion> animation;
	private TextureRegion curFrame;
	private int objectNum;

	private int offsetX, offsetY;

	private boolean interacting;
	private boolean interacted;
	private boolean opened;

	private float stateTime;


	GateObject(float x, float y, int width, int height, int offsetX, int offsetY, String name, Animation<TextureRegion> anim) {
		setBounds(x, y, width, height);
		setName(name);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		animation = anim;

		stateTime = GATEOBJECT_ANIMATION_DURATION;
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
		float x = getX();
		float y = getY();
		return (x < (player.getX() + player.getWidth() + range)) && ((x + getWidth()) > (player.getX() - range))
				&& (y < (player.getY() + player.getHeight() + range)) && ((y + getHeight()) > (player.getY() - range));
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
		if (success)
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
		//it starts the animation when interacted changes to true
		if (!opened && interacted) {
			opened = true;
			stateTime = 0f;
			animation.setPlayMode(PlayMode.REVERSED);
		}

		if (stateTime < GATEOBJECT_ANIMATION_DURATION) {
			stateTime += delta;
			curFrame = animation.getKeyFrame(stateTime, false);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(curFrame, getX() + offsetX, getY() + offsetY);
	}

	
}
