package game;

public abstract class MovableSpace {
	private float x;
	private float y;
	private int width;
	private int height;

	MovableSpace(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	float clampPosX(float oldX) {
		//System.out.println("clampX - min: " + x + ", oldX: " + oldX);
		if (oldX < x) return x;
		if (oldX > x + width) return x + width;
		return oldX;
	}

	float clampPosY(float oldY) {
		if (oldY < y) return y;
		if (oldY > y + height) return y + height;
		return oldY;
	}

	abstract MovableSpace determineSpace(float x, float y);
}
