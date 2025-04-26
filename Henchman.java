import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Graphics2D;

public class Henchman extends Enemy {
    private int t, atk_t;
    private final int ATK1_CHARGE = 30, ATK2_CHARGE = 30, ATK3_CHARGE = 50, COOLDOWN = 30;
    private final int ATK1_DUR = 10, ATK2_DUR = 15, ATK3_DUR = 25;
    private final int ATK1_DMG = 10, ATK2_DMG = 8, ATK3_DMG = 18;
    private enum State {
        LIGHT1,
        LIGHT2,
        HEAVY,
        ATTACK,
        APPROACH
    }

    private State state;
    private Animation anim;

    public Henchman(Player player, int x, int y) {
        super(player, x, y, 200, 200, 175, -1, 1, 1, 80);
        t = 0;
        atk_t = -1;
        state = State.APPROACH;
        anim = new Animation(this, "images/HenchmanSpriteSheet.gif", 4, 8);
        anim.rowAnim("DEFAULT", 0);
        anim.rowAnim("WALK", 1);
        anim.rowAnim("ATK1", 2);
        anim.rowAnim("ATK2", 3);
        anim.setState("DEFAULT");
        drawable = anim;
    }

    public void update() {
        stopAttack();
        if (state == State.APPROACH) {
            if (inRange())
                state = State.ATTACK;
        }
        if (!inRange()) {
            state = State.APPROACH;
        }
        act(state);
    }

    private void act(State current) {
        switch (current) {
            case APPROACH:
                t = 0;
                atk_t = -1;
                anim.setLoop(true);
                anim.setState("WALK");
                moveToPlayer();
                break;
            case ATTACK:
                anim.setState("DEFAULT");
                t++;
                if (t == ATK1_CHARGE) {
                    t = 0;
                    atk_t = 0;
                    damage = ATK1_DMG;
                    state = State.LIGHT1;
                    anim.setLoop(false);
                    anim.setState("ATK1");
                    attack();
                }
                break;
            case LIGHT1:
                t++;
                if (atk_t >= 0)
                    atk_t++;
                if (atk_t == ATK1_DUR)
                    atk_t = -1;
                if (t == ATK2_CHARGE) {
                    t = 0;
                    atk_t = 0;
                    damage = ATK2_DMG;
                    state = State.LIGHT2;
                    anim.setLoop(false);
                    anim.setState("ATK2");
                    attack();
                }
                break;
            case LIGHT2:
                t++;
                if (atk_t >= 0)
                    atk_t++;
                if (atk_t == ATK2_DUR)
                    atk_t = -1;
                if (t == ATK3_CHARGE) {
                    t = 0;
                    atk_t = 0;
                    damage = ATK3_DMG;
                    state = State.APPROACH; // rip couldnt make the anims - notdredre
                    attack();
                }
                break;
            case HEAVY:
                t++;
                if (atk_t >= 0)
                    atk_t++;
                if (atk_t == ATK3_DUR)
                    atk_t = -1;
                if (t == COOLDOWN) {
                    t = 0;
                    state = State.APPROACH;
                }
                break;
            default:
                break;
        }
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }

    private boolean inRange() {
        return getBoundingBox().intersects(player.getBoundingBox());
    }
}
