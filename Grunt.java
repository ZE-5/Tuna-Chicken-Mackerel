import java.awt.geom.Rectangle2D;

public class Grunt extends Enemy {
    private int t, atk_t;
    private final int CHARGE = 30, ATTACK_DUR = 20;
    private Animation gruntAnim;

    public Grunt(Player player, int x, int y) {
        super(player, x, y, 60, 60, 40, 3, 6, 6, 20);
        t = 0;
        atk_t = -1;
        gruntAnim = new Animation(this, "images/GruntSpriteSheet.gif", 3, 5, 60);
        gruntAnim.rowAnim("WALK", 0);
        gruntAnim.rowAnim("ATTACK", 1);
        gruntAnim.rowAnim("DEFAULT", 2);
        gruntAnim.setState("DEFAULT");
        drawable = gruntAnim;
        drawable.setImageFX(damageFX);
    }

    public void update() {
        stopAttack();
        if (!inRange()) {
            t = 0;
            atk_t = -1;
            stopAttack();
            moveToPlayer();
            gruntAnim.setLoop(true);
            gruntAnim.setState("WALK");
        }
        else {
            t++;
            if (t == CHARGE) {
                t = 0;
                atk_t = 0;
                attack();
                gruntAnim.setLoop(false);
                gruntAnim.setState("ATTACK");
            }
        }
        if (atk_t >= 0) {
            if (atk_t == ATTACK_DUR) {
                atk_t = -2;
                gruntAnim.setLoop(true);
                gruntAnim.setState("DEFAULT");
            }
            atk_t++;
        }
    }

    private boolean inRange() {
        return getBoundingBox().intersects(player.getBoundingBox());
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }
    
}
