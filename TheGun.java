import java.awt.geom.Rectangle2D;

public class TheGun extends Player {
    private PlayerProjectile projPool[];
    private final int NUM_PROJECTILES = 20;
    private Animation anim;
    private int projX, projY;

    private Sound[] shot;

    public TheGun(int x, int y) {
        super(x, y, 150, 150, 100, 10, 8, 8);
        COOLDOWN = 5;
        INPUT_GRACE = 20;
        shot = new Sound[NUM_PROJECTILES];
        projPool = new PlayerProjectile[NUM_PROJECTILES];
        for (int i = 0; i < NUM_PROJECTILES; i++) {
            projPool[i] = new PlayerProjectile(x, y, 40, 40, damage, 10, 100);
            Animation proj = new Animation(projPool[i], "images/THE gun.gif", 1, 4, 80, true);
            proj.rowAnim("DEFAULT", 0);
            proj.setState("DEFAULT");
            projPool[i].setDrawable(proj);
            shot[i] = new Sound("sounds/gun.wav", false, 0.5f);
        }
        anim = new Animation(this, "images/TheGunSpriteSheet.gif", 3, 5, 60);
        anim.rowAnim("DEFAULT", 0);
        anim.rowAnim("MOVE", 1);
        anim.rowAnim("SHOOT", 2);
        anim.setLoop(true);
        anim.setState("MOVE");
        drawable = anim;
        drawable.setImageFX(damageFX);
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }

    public void attack() {
        if (t < 0 && released) {
            t = 0;
            input_t = 0;
            isAttacking = true;
            released = false;
            anim.setLoop(false);
            anim.resetStep();
            anim.setState("SHOOT");
            shoot();
        }
    }

    public void update() {
        if (isFacingRight) {
            projX = x + width - 60;
        } else {
            projX = x + 30;
        }
        projY = y - 5;
        super.update();
        if (isMoving) {
            if (input_t < 0) {
                anim.setLoop(true);
                anim.setState("MOVE");
            }
        } else {
            if (input_t < 0)
                anim.setState("DEFAULT");
        }
    }

    private void shoot() {
        int i = 0;
        while (projPool[i].isActive()) {
            i = (i + 1) % NUM_PROJECTILES;
        }
        shot[i].play();
        projPool[i].fire(projX, projY, isFacingRight);
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D.Double(x + 40, y, width - 80, height);
    }
}
