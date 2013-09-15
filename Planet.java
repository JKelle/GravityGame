import java.awt.Color;
import java.awt.geom.Ellipse2D;


public class Planet extends Ellipse2D.Double implements LevelElement{
	
	double centerx, centery;
	double mass;
	double radius;
	double G;
	Color color;
	
	public Planet(double clickx, double clicky, double r) {
		super(clickx-r, clicky-r,r,r);
		radius = r;

		centerx = clickx;
		centery = clicky;
		
		G = Universe.getG();
		mass = radius*radius;
		color = Color.blue;
	}
	
	public void setMass(double m){
		mass = m;
	}
	public void setColor(Color c) {
		color = c;
	}
	public Color getColor() {
		return color;
	}
	
	public double[] getField(double rx, double ry) {
		double dx = centerx-rx;
		double dy = centery-ry;
		double rSqr = dx*dx + dy*dy;
		double rmag = Math.sqrt(rSqr);
		double Emag = G*mass/rSqr;
		double Ex = (dx == 0)? 0 : Emag*dx/rmag;
		double Ey = (dy == 0)? 0 : Emag*dy/rmag; 
		return new double[]{Ex, Ey};
	}
	
	public String toString() {
		return centerx +" "+ centery +" "+ radius;
	}
}
