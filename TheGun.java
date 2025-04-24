import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class TheGun extends Player {
    private PlayerProjectile projPool[];
    private final int NUM_PROJECTILES = 20;
    public TheGun(int x, int y) {
        super(x, y, 40, 50, 100, 10, 8, 8);
        COOLDOWN = 5;
        INPUT_GRACE = 0;
        projPool = new PlayerProjectile[NUM_PROJECTILES];
        for (int i = 0; i < NUM_PROJECTILES; i++) {
            projPool[i] = new PlayerProjectile(x, y, 15, 15, damage, 10, 100);
        }
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }

    public void attack() {
        if (t < 0 && released) {
            t = 0;
            isAttacking = true;
            released = false;
            shoot();
        }
    }

    private void shoot() {
        int i = 0;
        while (projPool[i].isActive()) {
            i = (i + 1) % NUM_PROJECTILES;
        }
        projPool[i].fire(x, y, isFacingRight);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.draw(new Rectangle2D.Double(x, y, width, height));

        g2.setColor(Color.WHITE);
        g2.draw(generateAttackBoundingBox());
    }
}
