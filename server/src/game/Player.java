package game;

public class Player {
	// ===============================CAPRICIOUS===============================
	private float x, y;

	private boolean moveUp;
	private boolean moveDown;
	private boolean moveLeft;
	private boolean moveRight;

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setMoveUp(boolean bool) {
		moveUp = bool;
	}

	public void setMoveDown(boolean bool) {
		moveDown = bool;
	}

	public void setMoveLeft(boolean bool) {
		moveLeft = bool;
	}

	public void setMoveRight(boolean bool) {
		moveRight = bool;
	}

	public void update(long progressTime) {
		if (moveUp) {
			y += 200 * progressTime/1000; //1초에 200픽셀씩 움직임
		}
		if (moveDown) {
			y -= 200 * progressTime / 1000; // 1초에 200픽셀씩 움직임
		}
		if (moveLeft) {
			x -= 200 * progressTime / 1000; // 1초에 200픽셀씩 움직임
		}
		if (moveRight) {
			x += 200 * progressTime / 1000; // 1초에 200픽셀씩 움직임
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	// ===============================CAPRICIOUS===============================
}
