import java.awt.Color;
import java.awt.Graphics;


public class Bullet extends GameObj {
	
	public static final int SIZE = 5;       


	public Bullet(int courtWidth, int courtHeight, int init_x, int init_y,
			int init_vel_x, int init_vel_y) {
		
		super(init_vel_x, init_vel_y, init_x, init_y, 
				SIZE, SIZE, courtWidth, courtHeight);
		
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillOval(pos_x, pos_y, width, height); 
	}
}
