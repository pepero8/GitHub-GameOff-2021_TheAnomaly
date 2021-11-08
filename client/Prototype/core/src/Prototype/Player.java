package Prototype;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Disposable;

/**
 * player character for test
 * can move around within the map
 */
public class Player extends Actor implements Disposable {

	private Area curArea; //area where the player is located

	//private Rectangle bound; //bounding rectangle of this player

	//private boolean killed;

	//constructor
	public Player() {
		setSize(Prototype.CHAR_WIDTH, Prototype.CHAR_HEIGHT);
		//bound = new Rectangle(getX(), getY(), getWidth(), getHeight());

		//configure action
		// RepeatAction loop = new RepeatAction();
		// loop.setCount(RepeatAction.FOREVER);
		// loop.setAction(new Action() {
		// 	@Override
		// 	public boolean act(float delta) {
		// 		// check if position value is within its range
		// 		//setX(MathUtils.clamp(getX(), minX, maxX));
		// 		//setX(MathUtils.clamp(getX(), curArea.getX(), curArea.getX()+curArea.getWidth()));
		// 		//setY(MathUtils.clamp(getY(), minY, maxY));
		// 		//setY(MathUtils.clamp(getY(), curArea.getY(), curArea.getY()+curArea.getHeight()));

		// 		//curArea = curArea.determineArea(getX(), getY()); //determine player's area according to player's location.
				
		// 		//bound.setPosition(getX(), getY()); //update the rectangle

		// 		return true;
		// 	}

		// });

		//addAction(loop);
	}

	// public void setCurrentArea(Area curArea) {
	// 	this.curArea = curArea;
	// }

	public synchronized Area accessCurArea(String operation, Area curArea) {
		if (operation.contentEquals("set")) {
			this.curArea = curArea;
		}
		else if (operation.contentEquals("get")) {
			return curArea;
		}
		return null;
	}

	// public Area getCurrentArea() {
	// 	return curArea;
	// }

	/**
	 * Returns true if this player is in touch with the other player
	 * @param otherPlayer player to check contact
	 * @return boolean
	 */
	// public boolean isContact(Player otherPlayer) {
	// 	return bound.overlaps(otherPlayer.getBoundingRectangle());
	// }

	// public void kill() {
	// 	killed = true;
	// }

	// public boolean isKilled() {
	// 	return killed;
	// }

	/**
	 * deprecated?
	 * 
	 * Sets the position of this character.
	 * Instead of Actor.setPosition(), this method should be called in order to change position
	 * as it also updates the position of bounding rectangle
	 * @param x new x
	 * @param y new y
	 */
	// public void setPos(float x, float y) {
	// 	setPosition(x, y);
	// 	bound.setPosition(x, y);
	// }

	// public Rectangle getBoundingRectangle() {
	// 	return bound;
	// }

	/**
	 * access to position value to get or set its position
	 * It is synchronized
	 * @param operation
	 * @param x
	 * @param y
	 * @return
	 */
	public synchronized float[] accessPosition(String operation, float x, float y) {
		if (operation.contentEquals("set")) {
			setX(x);
			setY(y);
			//Gdx.app.log("Player", "Position set to: (" + x + ", " + y + ")");
		}
		else if (operation.contentEquals("get")) {
			return new float[] {getX(), getY()};
		}
		return null;

		//update rectangle?
	}

	/**
	 * Limits the range of x and y
	 * @param minX min value of x
	 * @param maxX max value of x
	 * @param minY min value of y
	 * @param maxY max value of y
	 */
	// public void setPosRange(float minX, float maxX, float minY, float maxY) {
	// 	this.minX = minX;	this.maxX = maxX;
	// 	this.minY = minY;	this.maxY = maxY;
	// }

	@Override
	public void draw (Batch batch, float parentAlpha) {
		//Gdx.app.log("Player", "drawing...");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		Gdx.app.log("Player", "disposed");
	}
}
