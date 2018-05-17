

import java.awt.*;
//import javax.swing.*;

import javax.swing.ImageIcon;

public class Sprite2D {

	//member data
	protected double x,y;
	protected double xSpeed=0;
	protected Image myImage;
	protected Image myImage2;
	int framesDrawn=0;
	int winWidth;
	//Constructor
	public Sprite2D(Image i1, Image i2, int windowWidth){
		myImage = i1;
		myImage2 = i2;
		winWidth = windowWidth;
	}
	
	public void setPostion(double xx, double yy){
		x=xx;
		y=yy;
	}
	
	public void setXSpeed(double dx){
		xSpeed = dx;
	}
	
	public void paint(Graphics g){
		framesDrawn++;
		if( framesDrawn%100<50 )
		g.drawImage(myImage, (int)x, (int)y, null);
		else
		g.drawImage(myImage2, (int)x, (int)y, null);
	}

	public int getX() {
		return (int) x;
	}
	public int getY() {
		return (int) y;
	}
}
