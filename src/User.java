import java.awt.Color;
import java.awt.Graphics;


public class User extends GameObj {
	public static final int SIZE = 20;
	public static final int INIT_X = 0;
	public static final int INIT_Y = 0;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
	public static int courtWidth;
	public static int courtHeight;
	
    /** 
     * Note that because we don't do anything special
     * when constructing a Square, we simply use the
     * superclass constructor called with the correct parameters 
     */
    public User(int courtWidth, int courtHeight){
        super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, 
        		SIZE, SIZE, courtWidth, courtHeight);
        User.courtWidth = courtWidth;
        User.courtHeight = courtHeight;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(pos_x, pos_y, width, height); 
    }

	public Bullet emitBullet() {
		Bullet b = new Bullet(courtWidth, courtHeight, pos_x, pos_y, 10, 10);
		return b;
		
	}

}
