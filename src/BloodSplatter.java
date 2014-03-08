import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class BloodSplatter extends GameObj {
	
	public static final String img_file = "images/Blood_splatter.png";
	private static BufferedImage img;
	public static final int SIZE = 40;
	public Direction facing; 
	public static int space = 20;

	public BloodSplatter(Direction facing, int x, int y) {
		super (0, x, y, SIZE,SIZE, 300, 300);
		this.facing = facing;
		
		
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
		
		switch (this.facing){
	    case UP:pos_y -= space;
	    		break;
	    case UP_RIGHT:pos_y -= space;
	    			pos_x += space;
	    			break;
	    case RIGHT:pos_x += space;
	    			break;
	    case DOWN_RIGHT:pos_x += space;
	    				pos_y += space;
	    				break;
	    case DOWN: pos_y += space;
	    		 	break;
	    case DOWN_LEFT: pos_x -= space;
	    				pos_y += space;
	    				break;
	    case LEFT: pos_x -= space;
	    			break;
	    case UP_LEFT:pos_y -= space;
	    			pos_x -= space;
	    			break;
		}
	}
	
	public void draw(Graphics g){
	    int theta = 0;

	   double rotationRequired = Math.toRadians(theta+90);
	   double locationX = img.getWidth() / 2;
	   double locationY = img.getHeight() / 2;
	   AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, 
			   locationX, locationY);
	   AffineTransformOp op = new AffineTransformOp(tx, 
			   AffineTransformOp.TYPE_BILINEAR);

	   // Drawing the rotated image at the required drawing locations
	  // g.drawImage(op.filter(img, null), pos_x, pos_y, width, height, null);
	   g.drawImage(img, pos_x, pos_y, width, height, null);
	}
	
	
}
