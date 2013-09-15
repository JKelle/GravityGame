import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;


public class Player extends Rectangle implements LevelElement{
	
	private double centerx, centery;
	private double rx, ry;	//position
	private double vx, vy;	//velocity
	private Color color;
	private int N = 100;
	private ArrayList<double[]> pastLocs;
	
	public Player(int clickx, int clicky) {
		this(clickx-5, clicky-5, 10, 10);
	}
	
	public Player(int x, int y, int w, int h) {
		super(x,y,w,h);
		rx = x;
		ry = y;
		vx = 0;
		vy = 0;
		
		centerx = rx+w/2;
		centery = ry+h/2;
		
		pastLocs = new ArrayList<double[]>(N);
		color = Color.red;
	}
	
	public void setVelocity(double clickx, double clicky, double r) {
		this.vx = clickx-centerx;
		this.vy = clicky-centery;
		
		double v = Math.sqrt(vx*vx + vy*vy);
		if( v > r ) {
			vx = vx/v*r;
			vy = vy/v*r;
		}
		
		System.out.println("setV");
	}
	public double[] getVelocity() {
		return new double[]{vx, vy};
	}
	public double[] getR() {
		return new double[]{centerx, centery};
	}

	public Color getColor() {
		return color;
	}
	public ArrayList<double[]> getPastLocs() {
		return pastLocs;
	}
	public boolean update(HashSet<Planet> planets, double dt) {		
		for(Planet p : planets) {
			double[] E = p.getField(centerx, centery);
			double ax = E[0];
			double ay = E[1];
			
			double dvx = ax*dt;
			double dvy = ay*dt;
			
			vx += dvx;
			vy += dvy;
			
			double drx = vx*dt;
			double dry = vy*dt;
			
			double speedFactor = 1E-3;
			
			rx += drx*speedFactor;
			centerx += drx*speedFactor;
			ry += dry*speedFactor;
			centery += dry*speedFactor;
			
			x = (int)rx;
			y = (int)ry;
			pastLocs.add( new double[]{centerx, centery} );
			if( p.intersects(this) )
				return false;
		}
		for(Planet p : planets)
			if( p.intersects(this) )
				return false;
		return true;
	}
	
	public String toString() {
		return centerx +" "+ centery;
	}
	
}
