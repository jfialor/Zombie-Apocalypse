/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

/**
 * GameCourt
 * 
 * This class holds the primary game logic of how different objects 
 * interact with one another.  Take time to understand how the timer 
 * interacts with the different methods and how it repaints the GUI 
 * on every tick().
 *
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	private User player1;        
	private LinkedList<Bullet> bullets;
	private ArrayList<Zombie> zombies;
	private ArrayList<BloodSplatter> blood;
	private Bullet[] bulletarr;
	private Zombie[] zombiearr;
	private BloodSplatter[] bloodarr;
	private int level;
	public boolean level2 = false;
	public boolean level3 = false;
	int second;
	public String bigMessage;
	public boolean won = false;
	
	public boolean playing = false;  // whether the game is running
	private JLabel status;       // Current status text (i.e. Running...)
	public boolean instructions = false;

	// Game constants
	public static final int COURT_WIDTH = 600;
	public static final int COURT_HEIGHT = 600;
	public static final int PLAYER_VELOCITY = 3;
	public static final int BULLET_VELOCITY = 10;
	public static final int ZOMBIE_VELOCITY = 3;
	public static final int ZOMBIE_LIFE = 3;
	public static final int ZOMBIE_NUMBER = 10;
	// Update interval for timer in milliseconds 
	public static final int INTERVAL = 35; 
	public static final int PLAYER_LIFE = 700;
	
	public static final String img_file = "images/Background1.jpg";
	private static BufferedImage background;
	
	public static final String img_file_test = "images/Blood_splatter.png";
	private static BufferedImage test_image;
	
	private BufferedReader b;
	private String instructions_file = "Instructions"; 
	private ArrayList<String> instructions_lines;
	
	
	
	

	public GameCourt(JLabel status){
		// creates border around the court area, JComponent method
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		instructions_lines = new ArrayList<String>();
        try {
		 b = new BufferedReader (new FileReader (instructions_file));
		 while(b.ready()){
			    String line = b.readLine();
				if (line != null) {
					instructions_lines.add(line);
				}
			}
        }
        catch (Exception e){
        	System.out.println("Internal Error:" + e.getMessage());
        }
		try {
			if (background == null) {
				background = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
		try {
			if (test_image == null) {
				test_image = ImageIO.read(new File(img_file_test));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
		
		
		
        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called 
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
		Timer timer = new Timer(INTERVAL, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tick();
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		// this key listener allows the square to move as long
		// as an arrow key is pressed, by changing the square's
		// velocity accordingly. (The tick method below actually 
		// moves the square.)
		addKeyListener(new KeyAdapter(){
			private final Set<Integer> pressed = new HashSet<Integer>();
			public boolean machine_gun = false;
			public void keyPressed(KeyEvent e){
				pressed.add(e.getKeyCode());
				//if (pressed.size() > 1) {
				//TWO SIDED
					if (pressed.contains(KeyEvent.VK_UP) && 
						pressed.contains(KeyEvent.VK_LEFT)) {
						player1.v_x = -PLAYER_VELOCITY;
						player1.v_y = -PLAYER_VELOCITY;
						player1.setDirection(Direction.UP_LEFT);
					}
					else if (pressed.contains(KeyEvent.VK_UP) && 
						pressed.contains(KeyEvent.VK_RIGHT)) {
						player1.v_x = PLAYER_VELOCITY;
						player1.v_y = -PLAYER_VELOCITY;
						player1.setDirection(Direction.UP_RIGHT);
					}
					else if (pressed.contains(KeyEvent.VK_DOWN) &&
						pressed.contains(KeyEvent.VK_LEFT)) {
						player1.v_y = PLAYER_VELOCITY;
						player1.v_x = -PLAYER_VELOCITY;
						player1.setDirection(Direction.DOWN_LEFT);
					}
					else if (pressed.contains(KeyEvent.VK_DOWN) &&
						pressed.contains(KeyEvent.VK_RIGHT)) {
						player1.v_y = PLAYER_VELOCITY;
						player1.v_x = PLAYER_VELOCITY;
						player1.setDirection(Direction.DOWN_RIGHT);
					}
				//}
				//else {
				
					else if (pressed.contains(KeyEvent.VK_LEFT)) {
						player1.v_x = -PLAYER_VELOCITY;
						player1.setDirection(Direction.LEFT);
					}
					else if (pressed.contains(KeyEvent.VK_RIGHT)) {
						player1.v_x = PLAYER_VELOCITY;
						player1.setDirection(Direction.RIGHT);
					}
					else if (pressed.contains(KeyEvent.VK_DOWN)) {
						player1.v_y = PLAYER_VELOCITY;
						player1.setDirection(Direction.DOWN);
					}
					else if (pressed.contains(KeyEvent.VK_UP)) {
						player1.v_y = -PLAYER_VELOCITY;
						player1.setDirection(Direction.UP);
					}
					
					
				//}
				//machine gun
					if (machine_gun) {
				if (pressed.contains(KeyEvent.VK_SPACE)){
					bullets.add(player1.emitBullet(BULLET_VELOCITY));
				}
				}
				if (pressed.contains(KeyEvent.VK_M) && 
						pressed.contains(KeyEvent.VK_C) &&
						pressed.contains(KeyEvent.VK_G)){
					machine_gun = true;
				}
				
				if (pressed.contains(KeyEvent.VK_U) && 
						pressed.contains(KeyEvent.VK_T) &&
						pressed.contains(KeyEvent.VK_M)){
					User.tankmode();
				}
				
			}
			
			public void keyReleased(KeyEvent e){
				pressed.remove(e.getKeyCode());
				
				player1.v_x = 0;
				player1.v_y = 0;
			//if (!machine_gun){
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					bullets.add(player1.emitBullet(BULLET_VELOCITY));
			//}
			}
			//public void keyTyped(KeyEvent arg0) {
		});

		this.status = status;
		bullets = new LinkedList<Bullet>();
		zombies = new ArrayList<Zombie>();
		blood = new ArrayList<BloodSplatter>();
		this.level = 1;
		second = 0;
	}
	
		
	/** (Re-)set the state of the game to its initial state.
	 */
	public void reset() {

		player1 = new User(COURT_WIDTH, COURT_HEIGHT, PLAYER_LIFE);
		User.tank = false;
		bullets = new LinkedList<Bullet>();
		zombies = new ArrayList<Zombie>();
		blood = new ArrayList<BloodSplatter>();
		
		for (int i = 0; i < ZOMBIE_NUMBER; i++) {
			
			
			Zombie zombie = (new Zombie( (int) (Math.random() * COURT_WIDTH), 
					(int) (Math.random() * COURT_HEIGHT),COURT_WIDTH, 
					COURT_HEIGHT, ZOMBIE_VELOCITY, ZOMBIE_LIFE));
			while (zombie.willHitZombie(zombies.toArray(new Zombie[1]), 
					Direction.UP_LEFT))
				{
				zombie.randomize();
				zombie.findUser(player1, zombies.toArray(new Zombie[1]));
				};
			zombies.add(zombie);
		}

		playing = true;
		status.setText("Running...");

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

    /**
     * This method is called every time the timer defined
     * in the constructor triggers.
     */
	void tick(){
		
		if (playing) {
			// advance the square and snitch in their
			// current direction.
			second++;
			player1.move();
			zombiearr = new Zombie[zombies.size()];
			zombiearr = zombies.toArray(zombiearr);
		if (second%1 == 0) {
			for (int i = 0; i < zombiearr.length; i++) {
				zombies.get(i).findUser(player1, zombiearr);
			}
		}
			
			bulletarr = new Bullet[bullets.size()];
			bulletarr = bullets.toArray(bulletarr);
			for (int i = 0; i < bulletarr.length; i++) { 
				bullets.get(i).move();
			}
			boolean allDead = true;
			// make the zombies bounce off walls...
			for (int i = 0; i < zombies.size(); i++) {
				Zombie tempzombie = zombies.get(i);
				tempzombie.bounce(tempzombie.hitWall());
	    		allDead = allDead && tempzombie.dead;
			}
			
			for (int i = 0; i < bullets.size(); i++) {
		    	Bullet tempbullet = bullets.get(i);
		    	for (int j = 0; j < zombiearr.length; j++) {
		    		Zombie tempzombie = zombies.get(j);
		    		if (!tempzombie.dead && tempzombie.intersects(tempbullet)) {
		    			blood.add(tempzombie.wound(tempbullet.facing));
		    			if (!player1.tank){
		    			bullets.remove(i);
		    			}
		    			player1.score();
		    			break;
		    			//break;
		    			//playing = false;
		    			//status.setText("You win!");
		    		}
		    	}
		    }
			if (allDead && level2 && level3){
				youWon();
			}
			else if (allDead && level2){
				level3();
			}
			else if (allDead){
				level2();
			}
			
			if (player1.dead){
				youLost();
			}
		}
			// update the display
			repaint();
			
		 
	}
	
	void pause() {
		playing = !playing;
		requestFocusInWindow();
		if (!playing){
			status.setText("Paused");
		}
		else {
			status.setText("Running...");
		}
	}
	
	public void instructions() {
		instructions = !instructions;
		pause();
	}

	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(background, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
		Graphics tempg = g;
		tempg.setColor(Color.YELLOW);
		tempg.drawString("SCORE : " + player1.score, 100, 20);
		tempg.drawString("LEVEL : " + level, 10, 20);
		tempg.setColor(Color.blue);
		bloodarr = new BloodSplatter[blood.size()];
		bloodarr = blood.toArray(bloodarr);
		for (int i = 0; i < bloodarr.length; i++) {
			BloodSplatter temp = blood.get(i);
			if (temp != null){
				temp.draw(g);
			}
		}
		//g.drawImage(test_image, 0, 0,40,40, null);
		player1.draw(g);
		zombiearr = new Zombie[zombies.size()];
		zombiearr = zombies.toArray(zombiearr);
		for (int i = 0; i < zombies.size(); i++) {
			Zombie tz  = zombies.get(i);
			if (tz != null && !tz.dead) {
				tz.draw(g);
			}
		}
		if (playing){
		bulletarr = new Bullet[bullets.size()];
		bulletarr = bullets.toArray(bulletarr);
	    boolean tank = player1.tank;
		for (int i = 0; i < bulletarr.length; i++) {
			bullets.get(i).intedraw(g, tank);
		}
	}
		if (instructions) {
			int y = 140;
			g.fillRoundRect(100, 100, 400, 500, 50, 50);
			Iterator<String> i = instructions_lines.iterator();
			Graphics temp = g;
			temp.setColor(Color.RED);
			while (i.hasNext()){
				g.drawString(i.next(), 130, y);
				temp.setColor(Color.YELLOW);
				y+= 20;
			}
		}
		if (won){
			tempg.setColor(Color.yellow);
		}
		else {
			tempg.setColor(Color.red);
		}
		if (bigMessage != null){
			g.setFont(new Font("default", Font.BOLD, 40));
			tempg.drawString(bigMessage, 150, 300);
			playing = false;
		}
		tempg.setColor(Color.BLACK);
		

	}
	
	public void level2 () {
		level = 2;
		level2 = true;
		for (int i = 0; i < ZOMBIE_NUMBER; i++) {
			ZombieMaster zombie = 
					(new ZombieMaster( (int) (Math.random() * COURT_WIDTH), 
					(int) (Math.random() * COURT_HEIGHT),COURT_WIDTH, 
					COURT_HEIGHT, ZOMBIE_VELOCITY, ZOMBIE_LIFE * 2));
			while (zombie.willHitZombie(zombies.toArray(new Zombie[1]), 
					Direction.UP_LEFT))
				{
				zombie.randomize();
				zombie.findUser(player1, zombies.toArray(new Zombie[1]));
				};
			zombies.add(zombie);
		}
	}
	
	public void level3() {
		level = 3;
		level3 = true;
		for (int i = 0; i < ZOMBIE_NUMBER; i++) {
			Zombie zombie = (new Zombie( (int) (Math.random() * COURT_WIDTH), 
					(int) (Math.random() * COURT_HEIGHT),COURT_WIDTH, 
					COURT_HEIGHT, ZOMBIE_VELOCITY, ZOMBIE_LIFE));
			while (zombie.willHitZombie(zombies.toArray(new Zombie[1]), 
					Direction.UP_LEFT))
				{
				zombie.randomize();
				zombie.findUser(player1, zombies.toArray(new Zombie[1]));
				};
			zombies.add(zombie);
		}
		for (int i = 0; i < ZOMBIE_NUMBER; i++) {
			ZombieMaster zombie = 
					(new ZombieMaster( (int) (Math.random() * COURT_WIDTH), 
					(int) (Math.random() * COURT_HEIGHT),COURT_WIDTH, 
					COURT_HEIGHT, ZOMBIE_VELOCITY, ZOMBIE_LIFE * 2));
			while (zombie.willHitZombie(zombies.toArray(new Zombie[1]), 
					Direction.UP_LEFT))
				{
				zombie.randomize();
				zombie.findUser(player1, zombies.toArray(new Zombie[1]));
				};
			zombies.add(zombie);
		}
	}
	
	public void youWon() {
		bigMessage = "YOU WON!!!";
		playing = false;
		won = true;
	}
	public void youLost() {
		bigMessage = "YOU LOST";
		playing = false;
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(COURT_WIDTH,COURT_HEIGHT);
	}
}
