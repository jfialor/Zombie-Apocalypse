import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.Icon;

/** @see http://stackoverflow.com/a/11556441/230513 */
public enum Ground implements Icon {

    DIRT(new Color(205, 133, 63)), GRASS(new Color(0, 107, 60)),
    WATER(new Color(29, 172, 214)), CITY(Color.lightGray);
    private static final int SIZE = 42;
    private Random random = new Random();
    private TexturePaint paint;

    private Ground(Color color) {
        this.paint = initPaint(color);
    }

    private TexturePaint initPaint(Color color) {
        BufferedImage image = new BufferedImage(
            SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, SIZE, SIZE);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (random.nextBoolean()) {
                    image.setRGB(col, row, color.getRGB());
                } else {
                    if (random.nextBoolean()) {
                        image.setRGB(col, row, color.darker().getRGB());
                    } else {
                        image.setRGB(col, row, color.brighter().getRGB());
                    }
                }
            }
        }
        return new TexturePaint(image, rect);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(paint);
        g.fillRect(0, 0, SIZE, SIZE);
    }

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }
}
