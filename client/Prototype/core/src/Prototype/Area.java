package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public abstract class Area extends Actor implements Disposable{

	//public abstract Area determineArea(float x, float y);

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		Gdx.app.log("Area", "disposed");
	}
	
}
