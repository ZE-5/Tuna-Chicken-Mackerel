import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class StrengthPickup extends Pickup{
    StrengthPickup(int x, int y) {
        super(x, y);
    }


    public void update() {}


    public void draw(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
    }
}
