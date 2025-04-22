import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class KnifePlayer extends Player {
    public KnifePlayer(int x, int y) {
        super(x, y, 40, 40, 100, 10, 10, 10);
        COOLDOWN = 10;
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
