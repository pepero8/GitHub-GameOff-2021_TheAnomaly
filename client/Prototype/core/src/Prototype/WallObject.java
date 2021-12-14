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
