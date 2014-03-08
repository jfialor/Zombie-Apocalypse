import java.awt.Color;
import java.awt.Graphics;


public class Bullet extends GameObj {
	
	public static final int SIZE = 5;       


	public Bullet(int courtWidth, int courtHeight, int init_x, int init_y,
			int absvel) {
		
		super(absvel, init_x, init_y, 
				SIZE, SIZE, courtWidth +10, courtHeight+10);
		
	}

	public void intedraw (Graphics g, boolean tank){
		if (tank){
			width = 15;
			height = 15;
		}
		draw(g);
		
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillOval(pos_x, pos_y, width, height); 
	}
	
	@Override
	public void move() {
		move(this.facing);
	}
	@Override
	public void move(Direction d) {
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
	}

}
