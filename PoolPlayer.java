import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class PoolPlayer extends Player {
    public PoolPlayer(int x, int y) {
        super(x, y, 30, 50, 100, 25, 7, 7);
        COOLDOWN = 30;
        INPUT_GRACE = 60;
        numAtk = 4;
    }

    protected Rectangle2D generateAttackBoundingBox() {
        if (isFacingRight)
            return new Rectangle2D.Double(x + width, y, 120, 30);
        else
            return new Rectangle2D.Double(x - 120, y, 120, 30);
    }

    public void attack() {
        super.attack();
        if (atkCount == 3) {
            COOLDOWN = 50;
        } else {
            COOLDOWN = 30;
        }

        if (atkCount == 2) {
            damage = 40;
        } else {
            damage = 25;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.draw(new Rectangle2D.Double(x, y, width, height));

        g2.setColor(Color.WHITE);
        g2.draw(generateAttackBoundingBox());
    }
}
