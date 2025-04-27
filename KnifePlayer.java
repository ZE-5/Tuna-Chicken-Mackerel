import java.awt.geom.Rectangle2D;

public class KnifePlayer extends Player {
    private Animation anim;

    public KnifePlayer(int x, int y) {
        super(x, y, 90, 90, 100, 4, 10, 10);
        COOLDOWN = 10;
        INPUT_GRACE = 20;
        anim = new Animation(this, "images/KnifePlayerSpriteSheet.gif", 3, 5, 60);
        anim.rowAnim("DEFAULT", 0);
        anim.rowAnim("MOVE", 1);
        anim.rowAnim("STAB", 2);
        anim.setState("DEFAULT");
        drawable = anim;
        drawable.setImageFX(damageFX);
    }

    protected Rectangle2D generateAttackBoundingBox() {
        if (isFacingRight)
            return new Rectangle2D.Double(x + width - 40, y, 50, height);
        else
            return new Rectangle2D.Double(x - 15, y, 50, height);
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
        if (isFacingRight)
            return new Rectangle2D.Double(x, y, width - 40, height);
        else
            return new Rectangle2D.Double(x + 30, y, width - 40, height);
    }
}
