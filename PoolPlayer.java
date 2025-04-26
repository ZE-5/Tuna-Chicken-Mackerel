import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class PoolPlayer extends Player {
    private Animation anim;
    private Sound[] hit;
    public PoolPlayer(int x, int y) {
        super(x, y, 200, 150, 100, 25, 7, 7);
        COOLDOWN = 50;
        INPUT_GRACE = 60;
        numAtk = 3;
        hit = new Sound[5];
        for (int i = 0; i < 5; i++) {
            hit[i] = new Sound("sounds/punch.wav", false, 0.7f);
        }
        anim = new Animation(this, "images/PoolPlayerSpriteSheet.gif", 5, 8);
        anim.rowAnim("DEFAULT", 0);
        anim.rowAnim("WALK", 1);
        anim.rowAnim("ATK0", 2);
        anim.rowAnim("ATK1", 3);
        anim.rowAnim("ATK2", 4);
        anim.setLoop(true);
        anim.setState("DEFAULT");
        drawable = anim;
    }

    protected Rectangle2D generateAttackBoundingBox() {
        if (isFacingRight)
            return new Rectangle2D.Double(x + width, y, 120, 30);
        else
            return new Rectangle2D.Double(x - 120, y, 120, 30);
    }

    public void attack() {
        if (isMoving)
            return;
        if (t < 0 && released) {
            t = 0;
            input_t = 0;
            isAttacking = true;
            released = false;
            anim.setLoop(false);
            anim.resetStep();
            anim.setState("ATK" + atkCount);
            hit[atkCount].play();
            atkCount = (atkCount + 1) % numAtk;

        }
        if (atkCount == numAtk - 1) {
            COOLDOWN = 60;
        } else {
            COOLDOWN = 50;
        }

        if (atkCount == numAtk - 2) {
            damage = 35;
        } else {
            damage = 25;
        }
    }

    public void update() {
        super.update();
        if (isMoving) {
            anim.setLoop(true);
            anim.setState("WALK");
        } else {
            if (input_t < 0)
                anim.setState("DEFAULT");
        }
    }

    public void draw(Graphics2D g2) {
        super.draw(g2);

        g2.setColor(Color.WHITE);
        g2.draw(generateAttackBoundingBox());
    }
}
