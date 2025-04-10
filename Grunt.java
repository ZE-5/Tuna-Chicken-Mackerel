import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Grunt extends Enemy {
    private int t, atk_t;
    private final int CHARGE = 100, ATTACK_DUR = 20;
    public Grunt(Player player, int x, int y) {
        super(player, x, y, 40, 40, 40, 5, 1, 1, 20);
        t = 0;
        atk_t = -1;
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
        System.out.println(atk_t);
    }

    public void draw(Graphics2D g2) {
        if (atk_t >= 0)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.GREEN);
        g2.fillRect(x, y, width, height);
    }

    private boolean inRange() {
        return getBoundingBox().intersects(player.getBoundingBox());
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }
    
}
