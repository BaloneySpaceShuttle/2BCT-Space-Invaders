
import java.awt.Image;
import java.awt.image.ImageObserver;

public class PlayerBullet extends Sprite2D {

	public boolean isAlive = true;

	public PlayerBullet(Image i, int windowWidth) {
		super(i, i, windowWidth);
	}

	public boolean move() {
		if (isAlive) {
			y -= xSpeed;

			return true;
		}
		return false;
	}

	public void remove() {
		isAlive = false;
		x = -500;
		y = 0;
	}
}