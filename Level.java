import java.awt.Rectangle;
import java.util.HashSet;


public class Level {
	private HashSet<Planet> planets;
	private Goal goal;
	private Player player;
//	private HashSet<LevelElement> elems;
	private int maxV;
	
	public Level() {
		planets = new HashSet<Planet>();;
		//goal = new Goal( Play.getWidth()-100, Play.getHeight()-100, 10, 10 );
		//elems.add(player);
	}
	public void addPlanet(int x, int y, int r) {
		addPlanet( new Planet(x,y,r) );
	}
	public void addPlanet(Planet p) {
		planets.add(p);
//		elems.add(p);
	}
	public void setPlayer(int x, int y, int w, int h) {
		player = new Player(x, y, w, h);
	}
	public void setPlayer(int clickx, int clicky) {
		player = new Player(clickx, clicky);
	}
	public void setGoal(int x, int y, int w, int h) {
		goal = new Goal(x,y,w,h);
	}
	public void setMaxV(int r) {
		maxV = r;
		player.setVelocity(player.getCenterX()+20, player.getCenterY(), maxV);
	}	
	
	public Goal getGoal() {
		return goal;
	}
	public int getMaxV() {
		return maxV;
	}
	public HashSet<Planet> getPlanets() {
		return planets;
	}
	public Player player() {
		return player;
	}
}
