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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class BoxObject extends Image implements Interactable {
	static final int BOXOBJECT_WIDTH = 32;
	static final int BOXOBJECT_HEIGHT = 32;

	private TextureRegionDrawable boxClosedTexture;
	private TextureRegionDrawable boxOpenedTexture;

	private int objectNum;

	private boolean interacting;
	private boolean interacted;

	BoxObject(float x, float y, int width, int height, String name, Texture imageClosed, Texture imageOpened) {
		super(imageClosed);
		boxClosedTexture = new TextureRegionDrawable(imageClosed);
		boxOpenedTexture = new TextureRegionDrawable(imageOpened);
		setBounds(x, y, width, height);
		setName(name);
	}

	@Override
	public boolean isContact(Player player, int range) {
		if (player.playerNum == Prototype.PLAYER_ROBOT_NUM) {
			return false;
		}
		float x = getX();
		float y = getY();
		return (x < (player.getX() + player.getWidth() + range)) &&
			   ((x + getWidth()) > (player.getX() - range)) &&
			   (y < (player.getY() + player.getHeight() + range)) &&
			   ((y + getHeight()) > (player.getY() - range));
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
		this.interacted = interacted;
		if (interacted)
			setDrawable(boxOpenedTexture);
		else
			setDrawable(boxClosedTexture);
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

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
