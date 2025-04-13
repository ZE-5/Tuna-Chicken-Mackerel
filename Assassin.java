import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class Assassin extends Enemy {
    private int t, atk_t;
    private final int CHARGE = 100, ATTACK_DUR = 20;
    public Assassin(Player player, int x, int y) {
        super(player, x, y, 40, 60, 100, 20, 2, 2, 40);
    }
    
    public void update() {
        stopAttack();
        if (!inRange()) {
            t = 0;
            atk_t = -1;
            stopAttack();
            moveToPlayer();
        }
        else {
            t++;
            if (t == CHARGE) {
                t = 0;
                atk_t = 0;
                attack();
            }
        }
        if (atk_t >= 0) {
            if (atk_t == ATTACK_DUR) {
                atk_t = -2;
            }
            atk_t++;
        }
    }

    public void draw(Graphics2D g2) {
        if (atk_t >= 0)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.GREEN);
        g2.fillRect(x, y, width, height);
    }

    private boolean inRange() {
        Rectangle2D range = new Rectangle2D.Double(0, 0, 0, 0);
        if (isFacingRight) {
            range = new Rectangle2D.Double(getX() + width, getY(), width * 10, height);
        } else {
            range = new Rectangle2D.Double(getX() - width * 10, getY(), width * 10, height);
        }
        return range.intersects(player.getBoundingBox());
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }
    
}