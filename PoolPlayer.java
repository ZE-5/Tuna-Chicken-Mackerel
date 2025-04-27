import java.awt.geom.Rectangle2D;

public class PoolPlayer extends Player {
    private Animation anim;
    private Sound[] hit;
    public PoolPlayer(int x, int y) {
        super(x, y, 200, 150, 100, 25, 8, 8);
        COOLDOWN = 25;
        INPUT_GRACE = 30;
        numAtk = 3;
        hit = new Sound[5];
        for (int i = 0; i < 5; i++) {
            hit[i] = new Sound("sounds/punch.wav", false, 0.7f);
        }
        anim = new Animation(this, "images/PoolPlayerSpriteSheet.gif", 5, 8, 60, true, Drawable.RIGHT, -100);
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
        Rectangle2D b = getBoundingBox();
        int xValue = (int) b.getX();
        int yValue = (int) b.getY();
        int wValue = 200;
        int hValue = (int) b.getHeight();
        if (!isFacingRight)
            xValue -= 140;
        return new Rectangle2D.Double(xValue, yValue, wValue, hValue);
    }

    public void attack() {
        if (isMoving)
            return;
        if (atkCount == 0) {
            damage = 15;
        } else if (atkCount == 1) {
            damage = 19;
        } else if (atkCount == 2) {
            damage = 28;
        }
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
            COOLDOWN = 40;
            INPUT_GRACE = 50;
        } else {
            COOLDOWN = 25;
            INPUT_GRACE = 30;
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
        return new Rectangle2D.Double(x + 20, y + 15, width - 140, height - 5);
    }
}
