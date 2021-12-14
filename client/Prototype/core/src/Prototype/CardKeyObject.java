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

public class CardKeyObject extends Image implements Interactable {
	static final int CARDKEYOBJECT_WIDTH = 16;
	static final int CARDKEYOBJECT_HEIGHT = 16;

	private World world;
	private Area area;

	private int objectNum;

	private boolean interacting;
	private boolean interacted;

	CardKeyObject(World world, Area area, float x, float y, int width, int height, String name, Texture image) {
		super(image);
		this.world = world;
		this.area = area;
		setBounds(x, y, width, height);
		setName(name);
	}

	@Override
	public boolean isContact(Player player, int range) {
		float x = getX();
		float y = getY();
		return (x < (player.getX() + player.getWidth() + range)) && ((x + getWidth()) > (player.getX() - range))
				&& (y < (player.getY() + player.getHeight() + range))
				&& ((y + getHeight()) > (player.getY() - range));
	}

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
		super.draw(batch, parentAlpha);
	}
}
