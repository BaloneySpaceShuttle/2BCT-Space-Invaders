

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

	// Member Data
	private static boolean isGraphicInitialised = false;
	private static boolean isGameInProgress = true;// game state boolean
	private static final Dimension WindowSize = new Dimension(800, 600);
	private static final int NUMALIENS = 30;
	private static final int firingInterval = 2; 
	private static long lastFire = 0, score = 0, highScore = 0;
	private static int speedUp = 4;
	private BufferStrategy strategy;
	private Alien[] AliensArray = new Alien[NUMALIENS];
	private Spaceship Playership;
	private Image bulletImage;
	private ArrayList<PlayerBullet> bulletList = new ArrayList<PlayerBullet>();

	// Constructor
	public InvadersApplication() {

		// Create and set up the window.
		this.setTitle("Pacman, or something..");

		// Display the window, centered on the screen
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width / 2 - WindowSize.width / 2;
		int y = screensize.height / 2 - WindowSize.height / 2;
		setBounds(x, y, WindowSize.width, WindowSize.height);
		setVisible(true);

		// instantiates the GameObject
		ImageIcon icon = new ImageIcon(
				"/bullet.png");
		bulletImage = icon.getImage();

		// Buffer
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		// starts thread
		Thread t = new Thread(this);
		t.start();

		addKeyListener(this);
		isGraphicInitialised = true;
	}

	// threads entry point
	public void run() {
		if (isGameInProgress == true) {
			startNewGame();
			while (true) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
				}
				boolean alienDirectionReversalNeeded = false;
				for (int i = 0; i < NUMALIENS; i++) {
					if (AliensArray[i].move()) {
						alienDirectionReversalNeeded = true;
					}
				}
				if (alienDirectionReversalNeeded) {
					for (int i = 0; i < NUMALIENS; i++) {
						AliensArray[i].reverseDirection();
					}
				}
				Playership.move();
				hitTest();
				this.repaint();
			}
		} else {
			while (true) {
				this.repaint();
			}
		}
	}

	// Starts new wave reseting number of dead aliens
	public void startNewWave() {
		ImageIcon icon = new ImageIcon(
				"/space_invader.png");
		ImageIcon icon2 = new ImageIcon(
				"/second_image.png");
		Image alienImage = icon.getImage();
		Image alienImage2 = icon2.getImage();

		speedUp++;
		for (int i = 0; i < NUMALIENS; i++) {
			AliensArray[i] = null;
			AliensArray[i] = new Alien(alienImage, alienImage2, WindowSize.width);
			double xx = (i % 5) * 80 + 70;
			double yy = (i / 5) * 40 + 50;
			AliensArray[i].setPostion(xx, yy);
			AliensArray[i].setXSpeed(speedUp);
		}
	}
	// Starts new game with reset score
	// Wasn't sure about images probably could have done something neater with that
	public void startNewGame() {
		ImageIcon icon = new ImageIcon(
				"/space_invader.png");
		ImageIcon icon2 = new ImageIcon(
				"/second_image.png");
		Image alienImage = icon.getImage();
		Image alienImage2 = icon2.getImage();

		for (int i = 0; i < NUMALIENS; i++) {
			AliensArray[i] = new Alien(alienImage, alienImage2, WindowSize.width);
			double xx = (i % 5) * 80 + 70;
			double yy = (i / 5) * 40 + 50;
			AliensArray[i].setPostion(xx, yy);
			AliensArray[i].setXSpeed(4);
		}
		icon = new ImageIcon(
				"/ship.png");
		Image shipImage = icon.getImage();
		Playership = new Spaceship(shipImage, WindowSize.width);
		Playership.setPostion(350, 530);
	}

	// Three keyboard Event-Handler functions
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			Playership.setXSpeed(-4);
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			Playership.setXSpeed(4);
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			shootBullet();// Fires bullets if space is pressed
		
		// Only works when not in game state
		if (isGameInProgress == false) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				isGameInProgress = true;
				startNewGame();
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
			Playership.setXSpeed(0); // Stops ship
	}

	public void keyTyped(KeyEvent e) {
	}

	public void shootBullet() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		// if we waited long enough, add bullet to list, and record the time.
		PlayerBullet b = new PlayerBullet(bulletImage, WindowSize.width);
		b.setPostion(Playership.x - 3, Playership.y - 20);
		repaint();
		bulletList.add(b);
		b.setXSpeed(5);
		lastFire = System.currentTimeMillis();
	}

	// responsible for checking if alien gets hit by bullet & ending game when alien reaches bottom
	public void hitTest() {
		int x1 = 0, y1 = 0, w1 = 24, h1 = 24, x2 = 0, y2 = 0, w2 = 32, h2 = 32, x3 = 0, y3 = 0, h3 = 32, w3 = 32;

		for (int x = 0; x < NUMALIENS; x++) {
			x1 = AliensArray[x].getX();
			y1 = AliensArray[x].getY();

			x3 = Playership.getX();
			y3 = Playership.getY();
			int deaths =0;
			Iterator<PlayerBullet> iterator = bulletList.iterator();

			while (iterator.hasNext()) {
				PlayerBullet b = (PlayerBullet) iterator.next();

				x2 = b.getX();
				y2 = b.getY();
				// checking alien - bullet collision
				if (((x1 < x2 && x1 + w1 > x2) || (x2 < x1 && x2 + w2 > x1))
						&& ((y1 < y2 && y1 + h1 > y2) || (y2 < y1 && y2 + h2 > y1))) {
					b.remove();
					b = null;
					AliensArray[x].remove();
					deaths = checkdeaths();
					score = score+(deaths*10);  
				}
				// Responsible for checking alien - ship collision
				/*if (((x1 < x3 && x1 + w1 > x3) || (x3 < x1 && x3 + w3 > x1))
						&& ((y1 < y3 && y1 + h1 > y3) || (y3 < y1 && y3 + h3 > y1))) {
					isGameInProgress = false;
					if (score > highScore) {
						highScore = score;
					}
				}*/
				
				// Decided to end game when aliens reach bottom instead of collision
				if((y1+10)>y3){
					isGameInProgress = false;
					if (score > highScore) {
						highScore = score;// sets a new highscore
					}
				}
				// When all aliens die create new wave
				if (deaths == 30) {
					startNewWave();
				}
			}
		}
		return;
	};

	// Applications paint method
	public void paint(Graphics g) {
		if (isGameInProgress == true) {
			if (isGraphicInitialised) {
				g = strategy.getDrawGraphics();

				g.setColor(Color.BLACK);
				g.fillRect(0, 0, WindowSize.width, WindowSize.height);
				g.setColor(Color.WHITE);
				g.drawString("Score: " + score, 300, 50); // Dynamic score display
				g.drawString("High Score: " + highScore, 390, 50);
				Iterator<PlayerBullet> iterator = bulletList.iterator();
				while (iterator.hasNext()) {
					PlayerBullet b = (PlayerBullet) iterator.next();
					b.move();
					hitTest();
					b.paint(g);
				}

				for (int i = 0; i < NUMALIENS; i++) {
					AliensArray[i].paint(g);
				}
				Playership.paint(g);

				g.dispose();
				strategy.show();
			}
		} else { // Alternative paint for other game state
			if (isGraphicInitialised) {
				g = strategy.getDrawGraphics();

				g.setColor(Color.BLACK);
				g.fillRect(0, 0, WindowSize.width, WindowSize.height);
				g.setColor(Color.WHITE);
				g.drawString("Game Over", 370, WindowSize.height / 2);
				g.drawString("Type Enter to play again", 340, (WindowSize.height / 2) + 40);
				g.dispose();
				strategy.show();
			}
		}
	}
	public int checkdeaths(){// counts alien deaths for starting a new wave
		int deaths = 0;
		for(int i=0;i<30;i++){
			if(!AliensArray[i].isAlive){
				deaths++;
			}
		}
		return deaths;
	}
//	 Applications entry point
	public static void main(String[] args) {

		InvadersApplication w = new InvadersApplication();

	}

	public void pleasewait(int time) {
		try {
			Thread.sleep(time); // 1000 milliseconds is one second.
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}