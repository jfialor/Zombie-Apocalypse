import java.awt.Color;
import java.awt.Graphics;


public class Zombie extends GameObj {

	public static final int SIZE = 20;       
	public static final int INIT_POS_X = 170;  
	public static final int INIT_POS_Y = 170; 
	public static final int INIT_VEL_X = 2;
	public static final int INIT_VEL_Y = 3;

	public Zombie(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, 
				SIZE, SIZE, courtWidth, courtHeight);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(pos_x, pos_y, width, height); 
	}
}
