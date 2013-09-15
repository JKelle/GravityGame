import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

import java.util.HashSet;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Play implements Runnable
{
	JFrame frame;
	Canvas canvas;
	BufferStrategy bufferStrategy;
	MouseControl mouse;
	KeyControl key;
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 700;
	private static final int PANALWIDTH = 160;
	private static final int SPACEWIDTH = WIDTH-PANALWIDTH;
	
	private HashSet<PanalElement> panalElems = new HashSet<PanalElement>();
	
	Rectangle reset = new Rectangle(WIDTH-PANALWIDTH/2 -20, 50, 40, 20);
	Rectangle makeLevelButton = new Rectangle(reset.x, reset.y+2*reset.height, reset.width, reset.height);
	LevelLoader loader = new LevelLoader();
	LevelMaker levelMaker;
	
	private Level currentLevel;
	private int currentLevelNum;
	private Player player;
	
	private String dots = "";
	private String message = "";
	private long dotsStart;
	private boolean isPaused = true;
	private String mode = "Set Velocity";

	//edit init------------------------------------------------------------------
	private void init()	{ 
		currentLevelNum = 1;
		reset(currentLevelNum);
	}
	//edit update----------------------------------------------------------------
	protected void update(int deltaTime) {
		if(!isPaused) {
			Player p = currentLevel.player();
			Goal g = currentLevel.getGoal();
			if( !p.update(currentLevel.getPlanets(), deltaTime) ){
				System.out.println("You Lose!");
				playPause();
			}
			if( p.intersects(g) ) {
				System.out.println("You Win!");
				mode = "Congrats";
				dotsStart = System.currentTimeMillis();
			}
		}
		if( mode.equals("Next Level") ) {
			currentLevelNum++;
			reset(currentLevelNum);
		}
			
	}
	//edit render----------------------------------------------------------------
	protected void render(Graphics2D g)	{
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	
		for( Planet p : currentLevel.getPlanets() ) {
			g.setColor(p.getColor());
			g.fill(p);
		}

		g.setColor(currentLevel.getGoal().getColor());
		g.fill(currentLevel.getGoal());
		
		if(mode.equals("Set Velocity")) {
			double[] r = player.getR();
			double maxV = currentLevel.getMaxV();
			Ellipse2D.Double maxVBoundry = new Ellipse2D.Double(r[0]-maxV, r[1]-maxV, maxV*2, maxV*2);
			g.setColor( new Color(220,220,200) );
			g.fill(maxVBoundry);
			
			g.setColor(Color.red);
			drawVelocity(g);
		}

		g.setColor(player.getColor());
		g.fill(player);
		
		drawPaths(g);
		drawPanal(g);
	}
	public void drawPanal(Graphics2D g) {
		g.setColor( new Color(72,83,235) );
		g.fillRect(WIDTH-PANALWIDTH, 0, PANALWIDTH, HEIGHT);
		g.setColor(Color.black);
		int panalBoarder = 10;
		g.fillRect(WIDTH-PANALWIDTH+panalBoarder, panalBoarder, PANALWIDTH-2*panalBoarder, HEIGHT-2*panalBoarder);
		
		g.setColor(Color.white);
		g.setFont( new Font("MONOSPACE", Font.BOLD, 13) );
		
		for(PanalElement e : panalElems) {
			if( e.image != null )
				g.drawImage(e.image, e.x, e.y, e.width, e.height, canvas);
		}
		g.setColor(Color.blue);
		g.fill(reset);
		g.drawString("Reset", reset.x+3, reset.y-10);

		g.fill(makeLevelButton);
		g.drawString("Make a level", makeLevelButton.x+3, makeLevelButton.y-10);
		
		g.drawString(message, SPACEWIDTH+25, HEIGHT/2-50);
		if(mode.equals("Congrats")) {			
			g.setColor(Color.green);
			g.drawString("On to the", SPACEWIDTH+50, HEIGHT/2-30);
			g.drawString("next level", SPACEWIDTH+45, HEIGHT/2-10);
			if(System.currentTimeMillis() - dotsStart > 1000) {
				dots += ".";
				dotsStart = System.currentTimeMillis();
			}
			if( dots.length() < 3 )
				g.drawString(dots, SPACEWIDTH+110, HEIGHT/2-10);
			else {
				dots = "";
				mode = "Next Level";
			}
		}
	}
	
	public void drawVelocity(Graphics2D g) {
		double[] v = player.getVelocity();
		double[] r = player.getR();
		
		Arrow a = new Arrow(r[0], r[1], r[0]+v[0], r[1]+v[1]);
		
		g.drawLine((int)(a.tailx), (int)(a.taily), (int)(a.headx), (int)(a.heady));
		g.drawLine((int)(a.headx), (int)(a.heady), (int)(a.sidex1), (int)(a.sidey1));
		g.drawLine((int)(a.headx), (int)(a.heady), (int)(a.sidex2), (int)(a.sidey2));
	}
	
	public void drawPaths(Graphics2D g) {
		ArrayList<double[]>locs = currentLevel.player().getPastLocs();
		int N = 60;
		Color c = Color.cyan;
		for(int i = locs.size()-1; i > 0 && locs.size() - i < N; i--) {
			c = new Color( c.getRed(), c.getBlue(), c.getGreen(), c.getAlpha()-255/N);
			g.setColor( c );
			double[] prevLoc = locs.get(i-1);
			double[] curLoc = locs.get(i);
			g.drawLine((int)prevLoc[0], (int)prevLoc[1], (int)curLoc[0], (int)curLoc[1]);
		}
	}
	
	private void reset(int i) {
		isPaused = true;
		currentLevel = loader.loadLevel(i);
		mode = "Set Velocity";
		player = currentLevel.player();
		player.setVelocity(0, 0.5, currentLevel.getMaxV());		
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	public Play()
	{
		frame = new JFrame("Gravity Game");

		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setLayout(null);

		canvas = new Canvas();
		canvas.setBounds(0, 0, WIDTH, HEIGHT);
		canvas.setIgnoreRepaint(true);

		panel.add(canvas);

		mouse = new MouseControl();
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);

		key = new KeyControl();
		canvas.addKeyListener(key);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();

		canvas.requestFocus();

	}
	
	private class KeyControl extends KeyAdapter	{
		public void keyPressed(KeyEvent e) {
			if( e.getKeyCode() == KeyEvent.VK_SPACE ) {
				playPause();
				mode = "going";
			}
		}

		public void keyReleased(KeyEvent e)	{
		}

	}
	
	private class MouseControl extends MouseAdapter	{
		public void mouseClicked(MouseEvent e) {
			if( reset.contains(e.getPoint())) {
				reset(currentLevelNum);
			}
			if( mode.equals("Set Velocity") ) {
				player.setVelocity( e.getX(), e.getY(), currentLevel.getMaxV() );
			}
			if( makeLevelButton.contains(e.getPoint()) ){
				mode = "Making Level:player";
				message = "Place player";
				levelMaker = new LevelMaker(3);
				currentLevel = new Level();
			}
			/////////////////////////////////////
			if( mode.equals("Making Level: player") ) {
				currentLevel.setPlayer(e.getX(), e.getY());
				player = currentLevel.player();
				levelMaker.writePlayer(player);
				mode = "Making Level: goal center";
				message = "Place goal center";
			}
			if( mode.equals("Making Level: goal center") ) {
				currentLevel.setGoal(e.getX(), e.getY(), 20, 20);
				message = "Place out goal limit";
				mode = "Making Level: goal boundry";
			}
			if( mode.equals("Making Level: goal boundry") ) {
				int x = (int)(currentLevel.getGoal().centerx);
				int y = (int)(currentLevel.getGoal().centery);
				int r = (int)Math.sqrt( (x-e.getX())*(x-e.getX()) + (y-e.getY())*(y-e.getY()) );
				currentLevel.setGoal(x, y, r, r);
			}
		}
		public void mouseDragged(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {
		/*	for(PanalElement p : panalElems)
				if( p.contains(e.getPoint()) )
					p.setImage("hover"+ p.name);
				else
					p.setImage(p.name);
		*/
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseWheelMoved(MouseEvent e) {}
	}
	
	public void playPause() {
		isPaused = !isPaused;
	}

	long desiredFPS = 60;
	long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
	boolean running = true;

	public void run()
	{

		long beginLoopTime;
		long endLoopTime;
		long currentUpdateTime = System.nanoTime();
		long lastUpdateTime;
		long deltaLoop;
		int deltaTime;

		init();

		while(running)
		{
			beginLoopTime = System.nanoTime();

			render();

			lastUpdateTime = currentUpdateTime;
			currentUpdateTime = System.nanoTime();
			deltaTime = (int) ((currentUpdateTime - lastUpdateTime)/(1000*1000));
			update(deltaTime);

			endLoopTime = System.nanoTime();
			deltaLoop = endLoopTime - beginLoopTime;

			if(deltaLoop <= desiredDeltaLoop)
			{
				try
				{
					Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
				} catch(InterruptedException e) { /* Do nothing */ }
			}
		}
	}

	private void render()
	{
		Graphics2D g = (Graphics2D)bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		render(g);
		g.dispose();
		bufferStrategy.show();
	}

	public static void main(String[] args)
	{
		Play ex = new Play();
		new Thread(ex).start();
	}
	
	public static int getWidth() {
		return WIDTH;
	}
	public static int getHeight() {
		return HEIGHT;
	}
	public static int getSpaceWidth() {
		return SPACEWIDTH;
	}
	public static int getPanalWidth() {
		return PANALWIDTH;
	}
	public static void delay(int t) {
		long start = System.currentTimeMillis();
		while( System.currentTimeMillis() - start < t ){}
	}
	
}