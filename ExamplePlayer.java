import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class ExamplePlayer extends Player {
    Rectangle2D attackBoundingBox;

    public ExamplePlayer(int x, int y) {
        super(x, y, 10, 10, 100, 10, 10, 10);
    }


    public void update() {
        
    }


    protected Rectangle2D generateAttackBoundingBox() {
        if (isFacingRight)
            return new Rectangle2D.Double(x + width, y, 50, height);
        else
            return new Rectangle2D.Double(x - 50, y, 50, height);
    }


    public void draw(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.draw(new Rectangle2D.Double(x, y, width, height));

        g2.setColor(Color.WHITE);
        g2.draw(generateAttackBoundingBox());
    }
}
