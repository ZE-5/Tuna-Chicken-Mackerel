import java.awt.geom.Rectangle2D;

public class KnifePlayer extends Player {
    private Animation anim;

    public KnifePlayer(int x, int y) {
        super(x, y, 90, 90, 100, 7, 10, 10);
        COOLDOWN = 10;
        INPUT_GRACE = 20;
        anim = new Animation(this, "images/KnifePlayerSpriteSheet.gif", 3, 5, 60, true, Drawable.RIGHT, -25);
        anim.rowAnim("DEFAULT", 0);
        anim.rowAnim("MOVE", 1);
        anim.rowAnim("STAB", 2);
        anim.setState("DEFAULT");
        drawable = anim;
        drawable.setImageFX(damageFX);
    }

    protected Rectangle2D generateAttackBoundingBox() {
        Rectangle2D b = getBoundingBox();
        int xValue = (int) b.getX();
        int yValue = (int) b.getY();
        int wValue = 80;
        int hValue = (int) b.getHeight();
        if (!isFacingRight)
            xValue -= 55;
        return new Rectangle2D.Double(xValue, yValue, wValue, hValue);
    }

    public void update() {
        super.update();
        if (isMoving) {
            anim.setLoop(true);
            anim.setState("MOVE");
        } else {
            if (input_t < 0)
                anim.setState("DEFAULT");
        }
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
            anim.setState("STAB");
        }
        
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D.Double(x + 10, y + 10, width - 45, height);
    }
}
