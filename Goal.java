import java.awt.Color;
import java.awt.Rectangle;

public class Goal extends Rectangle implements LevelElement {
	private Color color;
	double centerx;
	double centery;
	
	public Goal(int clickx, int clicky, int w, int h) {
		super(clickx-w/2, clicky-h/2,w,h);
		color = Color.green;
	}
	public Color getColor() {
		return color;
	}
	
	public String toString() {
		return centerx +" "+ centery +" "+ width +" "+ height;
	}
}