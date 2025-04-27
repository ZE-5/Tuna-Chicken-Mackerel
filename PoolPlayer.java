import java.awt.geom.Rectangle2D;

public class PoolPlayer extends Player {
    private Animation anim;
    private Sound[] hit;
    public PoolPlayer(int x, int y) {
        super(x, y, 200, 150, 100, 25, 7, 7);
        COOLDOWN = 20;
        INPUT_GRACE = 50;
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
        drawable.setImageFX(damageFX);
    }

    protected Rectangle2D generateAttackBoundingBox() {
        if (isFacingRight)
            return new Rectangle2D.Double(x + 50, y, 150, height);
        else
            return new Rectangle2D.Double(x, y, 150, height);
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
            COOLDOWN = 30;
        } else {
            COOLDOWN = 20;
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

    public Rectangle2D getBoundingBox() {
        if (isFacingRight)
            return new Rectangle2D.Double(x, y, 100, height);
        else
            return new Rectangle2D.Double(x + 100, y, 100, height);
    }
}
