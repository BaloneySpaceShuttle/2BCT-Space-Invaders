

import java.awt.Image;

public class Alien extends Sprite2D {

	public boolean isAlive = true;// used to check if alien should be moving
	public int speedup = 0;

	public Alien(Image i1, Image i2, int windowWidth) {
		super(i1, i2, windowWidth);
	}

	public boolean move() {
		if (isAlive) {// if Alien gets hit he no longer moves
			x += xSpeed;

			if (x <= 0 || x >= winWidth - myImage.getWidth(null))
				return true;
		}

		return false;

	}

	public void reverseDirection() {
		if (isAlive) {// if Alien gets hit he no longer moves
			xSpeed = -xSpeed;
			y += 20;
			speedup++;
		}
	}

	// moves alien off screen if he gets shot
	public void remove() {
		isAlive = false;
		x = -500;
		y = 0;
	}
}
