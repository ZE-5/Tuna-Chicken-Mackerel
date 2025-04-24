import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class Assassin extends Enemy {
    private int t, atk_t;
    private int projectileY, projectileX;
    private EnemyProjectile projectile;
    private final int THROW_CHARGE = 100, CLOSE_CHARGE = 50, ATTACK_DUR = 20, RETREAT_DUR = 200;

    private enum State {
        STALK("STALK"),
        SHANK("SHANK"),
        SCARED("SCARED"),
        THROW("THROW");

        private String value;
        State(String string) {
            value = string;
        }
    }

    private State state;

    public Assassin(Player player, int x, int y) {
        super(player, x, y, 40, 60, 100, 20, 2, 2, 40);
        t = 0;
        atk_t = -1;
        state = State.STALK;
        projectile = new EnemyProjectile(x, y, 10, 10, 1, 5, 50);
    }

    public void update() {
        projectileY = y;
        if (isFacingRight)
            projectileX = x + width;
        else
            projectileX = x;
        stopAttack();
        if (!playerFacingMe()) {
            state = State.STALK;
            if (inCloseRange())
                state = State.SHANK;
        } else {
            if (inCloseRange()) {
                state = State.SCARED;
            } else {
                if (inLongRange()) {
                    state = State.THROW;
                }
            }
        }
        act(state);
    }

    private boolean playerFacingMe() {
        return (player.isFacingRight() && x > player.getX()) || (!player.isFacingRight() && x <= player.getX());
    }

    private void retreat() {
        if (player.isFacingRight()) {
            move("RIGHT");
        } else {
            move("LEFT");
        }
    }

    private void act(State current) {
        switch (current) {
            case STALK:
                t = 0;
                atk_t = -1;
                moveToPlayer();
                break;
            case SHANK:
                t++;
                if (t == CLOSE_CHARGE) {
                    t = 0;
                    atk_t = 0;
                    attack();
                }
                if (atk_t >= 0) {
                    if (atk_t == ATTACK_DUR) {
                        atk_t = -2;
                    }
                    atk_t++;
                }
                break;
            case SCARED:
                retreat();
                t++;
                if (t == RETREAT_DUR) {
                    t = 0;
                    state = State.STALK;
                    System.out.println("HERE");
                }
                break;
            case THROW:
                t++;
                if (t == THROW_CHARGE) {
                    t = 0;
                    if (!projectile.isActive()) {
                        projectile.fire(projectileX, projectileY, isFacingRight);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void draw(Graphics2D g2) {
        if (atk_t >= 0)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.GREEN);
        g2.fillRect(x, y, width, height);
    }

    private boolean inCloseRange() {
        return getBoundingBox().intersects(player.getBoundingBox());
    }

    private boolean inLongRange() {
        Rectangle2D range = new Rectangle2D.Double(0, 0, 0, 0);
        if (isFacingRight) {
            range = new Rectangle2D.Double(getX() + width, getY(), width * 10, height);
        } else {
            range = new Rectangle2D.Double(getX() - width * 10, getY(), width * 10, height);
        }
        return range.intersects(player.getBoundingBox());
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }
}