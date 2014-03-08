import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ZombieMaster extends Zombie {
	
	 public static final String img_file = "images/zombie_red_03.png";
	 public static int SIZE = 40;
	 public final int INIT_POS_X = (int) (Math.random() * 300);  
	 public final int INIT_POS_Y = (int) (Math.random() * 300); 
	 public static final int INIT_VEL_X = 2;
	 public static final int INIT_VEL_Y = 3;
	 public static final int jump = 0; //best at 50
	 public boolean stillContact;
	 
	 private static BufferedImage img;
	 
	 public ZombieMaster(int posx, int posy,
			 int courtWidth, int courtHeight, int absvel, int maxlife) {
		 super(posx, posy, courtWidth, 
					courtHeight, absvel, maxlife);
		 width = SIZE;
		 height = SIZE;
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
		
		this.Life = maxlife;
	}

   @Override
	public void draw(Graphics g){
	    int theta = 0;
	    
	    if (this.facing == null)
	    	facing = Direction.UP_LEFT;
	    
	    switch (this.facing){
	    case UP: theta = 0; break;
	    case UP_RIGHT: theta = 45; break;
	    case RIGHT: theta = 90; break;
	    case DOWN_RIGHT: theta = 135; break;
	    case DOWN: theta = 180; break;
	    case DOWN_LEFT: theta = 225; break;
	    case LEFT: theta = 270; break;
	    case UP_LEFT: theta = 315; break;
	    }
	   
	   double rotationRequired = Math.toRadians(theta);
	   double locationX = img.getWidth() / 2;
	   double locationY = img.getHeight() / 2;
	   AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, 
			   locationX, locationY);
	   AffineTransformOp op = new AffineTransformOp(tx, 
			   AffineTransformOp.TYPE_BILINEAR);

	   // Drawing the rotated image at the required drawing locations
	   BufferedImage temp;
	   switch (Life){
	   case 3: temp = img; break;
	   //for additional image options
	   default: temp = img;
	   
	   }
	   g.drawImage(op.filter(temp, null), pos_x, pos_y, width, height, null);

	}
   public BloodSplatter wound(Direction ofBullet) {
	   if (!dead) {
	    this.Life -= 1;
	    if (Life <= 0){
	    	die();
	    	return spillBlood(ofBullet);
	    }
	    //beHit(ofBullet, jump);
	    //move2(ofBullet);
	    return spillBlood(ofBullet);
	   }
	   return null;
	}
   
   public void die () {
	   dead = true;
   }
   
   public BloodSplatter spillBlood(Direction d) {
	   return new BloodSplatter(d, pos_x, pos_y);
   }
   
   public void findUser(User user, Zombie[] zombies) {
	   int userx = user.getx();
	   int usery = user.gety();
	   Direction d = Direction.UP;


	   if (usery < pos_y && userx == pos_x ){
		   d = Direction.UP;
	   }
	   else if (usery > pos_y && userx == pos_x ){
		   d = Direction.DOWN;
	   }
	   else if (usery == pos_y && userx < pos_x ){
		   d = Direction.LEFT;
	   }
	   else if (usery == pos_y && userx > pos_x ){
		   d = Direction.RIGHT;
	   }
	   else if (usery < pos_y && userx < pos_x ){
		   d = Direction.UP_LEFT;
	   }
	   else if (usery < pos_y && userx > pos_x ){
		   d = Direction.UP_RIGHT;
	   }
	   else if (usery > pos_y && userx < pos_x ){
		   d = Direction.DOWN_LEFT;
	   }
	   else if (usery > pos_y && userx > pos_x ){
		   d = Direction.DOWN_RIGHT;
	   }
	   else {
		   move();
	   }
	   
	   if (willHitZombie(zombies, d) && !stillContact) {
		  beHit(opposite(d), 1);
		  move();
		  stillContact = true;
		  return;
		  //implement this.navigateAround
	   }
	   if (willHitZombie(zombies, d) && stillContact){
		   move();
		   stillContact = false;
		   return;
	   }

	   if (user != null && facing != null && withinDistance(user, facing)
			   && !dead){
		   user.wound();
		   beHit(opposite(d), 1);
	   }
	   move(d);   
   }
   
   public boolean willHitZombie(Zombie[] zombies, Direction d) {
	   
	   
	   
	   for (int i = 0; i < zombies.length; i++) {
		   
		   if (!this.equals(zombies[i]) && zombies[i] != null && 
			   withinDistance(zombies[i], d) && !zombies[i].dead){
			   return true;
		   }

	   }
	   return false;
   }
   public void randomize(){
	   pos_x = (int) (Math.random() * (max_x + width));
		pos_y = (int) (Math.random() * (max_y + height));
   }
   @Override
   public void move(Direction d) {
	    double m = Math.random();
	    
	    if (m > 0.8) {
		switch (d) {
		case UP: pos_y -= absvel; 
		 		 break;
		case DOWN: pos_y += absvel; 
		 		   break;
		case LEFT: pos_x -= absvel; 
		 		   break;
		case RIGHT: pos_x += absvel; 
		 break;
		case UP_RIGHT: pos_y -= absvel;
					   pos_x += absvel;
					   break;
		case UP_LEFT: pos_y -= absvel;
					  pos_x -= absvel;
		 			  break;
		case DOWN_RIGHT: pos_y += absvel; 
						 pos_x += absvel;
		 				 break;
		case DOWN_LEFT: pos_y += absvel;
						pos_x -= absvel;
		 				break;
		}
		this.facing = d;
		clip();
	}
   }
   

}
