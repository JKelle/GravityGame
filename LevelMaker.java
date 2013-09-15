import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LevelMaker {
	private FileWriter writer;
	private boolean hasPlayer;
	private boolean hasGoal;
	private boolean planets;
	
	public LevelMaker(int i) {
		String filename = "levels/l"+ i +".txt";
		planets = false;
		try{
			writer = new FileWriter(new File(filename));
		}catch(Exception e){ System.out.println(e); }
	}
	
	public boolean done() {
		planets = false;
		try {
			writer.close();
		} catch (IOException e) {System.out.println(e);}
		return hasPlayer && hasGoal;
	}
	
	public void writeMaxV(int r) {
		try{
			writer.write("maxV r\n");
			writer.write(r +"\n");
			writer.write("\n");
		}catch(Exception e){System.out.println(e);}
	}
	
	public void writePlayer(Player p) {
		planets = false;
		try{
			writer.write("player x, y, w, h\n");
			writer.write(p +"\n");
			writer.write("\n");
		}catch(Exception e){System.out.println(e);}
	}
	
	public void writeGoal(Goal g) {
		planets = false;
		try{
			writer.write("goal x, y, w, h\n");
			writer.write(g +"\n");
			writer.write("\n");
		}catch(Exception e){System.out.println(e);}		
	}
	
	public void writePlanet(Planet p) {
		try{
			if( !planets )
				writer.write("planets x, y, r\n");
			planets = true;
			writer.write(p +"\n");
		}catch(Exception e){System.out.println(e);}
	}
}