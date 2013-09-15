import java.io.*;
import java.util.Scanner;

public class LevelLoader {	
	public Level loadLevel(int i) {
		Level l = new Level();
		String fileName = "levels/l"+ i +".txt";
		boolean planets = false;
		try{
			Scanner read = new Scanner(new File(fileName));
			while( read.hasNext() ) {
				if(planets) {
					read.nextLine();
					int x = read.nextInt();
					int y = read.nextInt();
					int r = read.nextInt();
					l.addPlanet(x, y, r);
					planets = true;
					System.out.println("added planet");
					continue;
				}
				
				String s = read.next();
				System.out.println("s is "+ s);
				if( s.equals("player") ) {
					read.nextLine();
					int x = read.nextInt();
					int y = read.nextInt();
					l.setPlayer(x, y);
					System.out.println("set Player");
				}
				if( s.equals("goal") ) {
					read.nextLine();
					int x = read.nextInt();
					int y = read.nextInt();
					int w = read.nextInt();
					int h = read.nextInt();
					l.setGoal(x, y, w, h);
					System.out.println("set goal");
				}
				if( s.equals("maxV") ){
					read.nextLine();
					int r = read.nextInt();
					l.setMaxV(r);
					System.out.println("set maxV");
				}
				if( s.equals("planets") ) {
					read.nextLine();
					int x = read.nextInt();
					int y = read.nextInt();
					int r = read.nextInt();
					l.addPlanet(x, y, r);
					System.out.println("added planet");
					System.out.println("Planets = true");
					planets = true;
				}
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return l;
	}
}
