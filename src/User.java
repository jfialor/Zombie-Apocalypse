import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class User extends GameObj {
	public static final int SIZE = 40;
	public static final int INIT_X = 0;
	public static final int INIT_Y = 0;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static int courtWidth;
	public static int courtHeight;
	private static BufferedImage[] imgs;
	private static BufferedImage[] tank_imgs;
	private static String tank_filename = "images/daTank.png";
	public static boolean tank = false;
	public int score;
	public int Life;
	public int Initial_life;
	public boolean dead = false;
	private boolean woundImage = false;
    /** 
     * Note that because we don't do anything special
     * when constructing a Square, we simply use the
     * superclass constructor called with the correct parameters 
     */
    public User(int courtWidth, int courtHeight, int Life){
        super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, 
        		SIZE, SIZE, courtWidth, courtHeight);
        User.courtWidth = courtWidth;
        User.courtHeight = courtHeight;
        facing = Direction.RIGHT;
        imgs = new BufferedImage[8];
        tank_imgs = new BufferedImage[8];
        for (int i = 0; i < imgs.length; i++) {    
        	try {
        		if (imgs[i] == null) {
        			String filename = "images/User" + i + ".png";
        			imgs[i] = ImageIO.read(new File(filename));
        		}
        	} catch (IOException e) {
        		System.out.println("Internal Error:" + e.getMessage());
        	}
        }
        for (int i = 0; i < imgs.length; i++) {    
        	try {
        		if (tank_imgs[i] == null) {
        			String filename = "images/tank" + i + ".png";
        			tank_imgs[i] = ImageIO.read(new File(filename));
        		}
        	} catch (IOException e) {
        		System.out.println("Internal Error:" + e.getMessage());
        	}
        }
        this.Initial_life = Life;
        this.Life = Life;
    }
    
    public void wound() {
    	this.Life -= 1;
    	if (Life <= 0) {
    		die();
    	}
    	beHit(opposite(facing), 2);
    	//if (Life % 20 == 0)
    	woundImage = true;
    }
    
    public void die() {
    	dead = true;
    }
    
    public static void tankmode () {
    	tank = true;
    }
  
    
    
    @Override
    public void draw(Graphics g) {
        //g.setColor(Color.BLACK);
        //g.fillRect(pos_x, pos_y, width, height); 
        
        int imgIndex = 0;
        int tank_width = width;
        int tank_height = height;
        
        if (this.facing == null)
	    	facing = Direction.UP;
	    
	    switch (this.facing){
	    case UP: imgIndex = 0; 
	    tank_height += 40;
	    break;
	    case UP_RIGHT: imgIndex = 1;
	    tank_width += 40;
	    tank_height += 40;
	    break;
	    case RIGHT: tank_width += 40;
	    imgIndex = 2; break;
	    case DOWN_RIGHT: imgIndex = 3; 
	    tank_width += 40;
	    tank_height += 40;
	    break;
	    case DOWN: imgIndex = 4; 
	    tank_height += 40;
	    break;
	    case DOWN_LEFT: imgIndex = 5;
	    tank_width += 40;
	    tank_height += 40;
	    break;
	    case LEFT: imgIndex = 6;
	    tank_width += 40;
	 break;
	    case UP_LEFT: imgIndex = 7;
	    tank_width += 40;
	    tank_height += 40;
	    break;
	    }
	    BufferedImage image = imgs[imgIndex];
	    BufferedImage tank_image = tank_imgs[imgIndex];
        
	    if (tank){
	    	g.drawImage(tank_image, pos_x, pos_y, tank_width, tank_height, null);
		    }

	    else {
	    	g.drawImage(image, pos_x, pos_y, width, height, null);
	    }
       
        
        g.drawRect(pos_x, pos_y - 20, width, 10);
        if (woundImage)
        	g.setColor(Color.red);
        else 
        	g.setColor(Color.green);
        woundImage = false;
       
        int bar = (int) (((double) Life/ (double) Initial_life) * width);
        if (!dead)
        g.fill3DRect(pos_x, pos_y - 20, bar, 10, false);
        g.setColor(Color.black);
    }

	public Bullet emitBullet(int vel) {
		int bulletx = pos_x;
		int bullety = pos_y;
		if (this.facing == Direction.UP || this.facing == Direction.DOWN){
			bulletx += width/2;
		}
		else if (this.facing == Direction.RIGHT){
			bulletx += width;
		}
		else if (this.facing == Direction.UP_RIGHT){
			bulletx += width;
			bullety += height/3;
		}
		Bullet b = new Bullet(courtWidth, courtHeight, bulletx, bullety, vel);
		b.setDirection(this.facing);
		return b;
	}
	
	public void score () {
		score += 100;
	}

}
