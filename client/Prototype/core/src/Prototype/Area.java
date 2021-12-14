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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

public abstract class Area extends Actor {
	private Texture image;
	private Interactable[] objects;
	private int index = -1;

	private int areaNum;

	private int offsetX, offsetY; //used for texture rendering

	Area(int areaNum, int numObjects, int offsetX, int offsetY, Texture image) {
		this.image = image;
		this.areaNum = areaNum;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
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

	//used for interacting validity check
	public Interactable checkCollision(Player player) {
		//returns card key first if exists
		for (int i = objects.length - 1; i != -1; i--) {
			if (objects[i] != null && objects[i].isContact(player, Interactable.INTERACTION_AVAILABLE_RANGE)) {
				return objects[i];
			}
		}

		return null;
	}

	public abstract Area determineArea(float x, float y);

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(image, getX() - offsetX, getY() - offsetY);

		for (int i = 0; i != objects.length - 1; i++) {
			if (objects[i] != null) {
				((Actor)objects[i]).draw(batch, parentAlpha);
			}
		}
	}
}
