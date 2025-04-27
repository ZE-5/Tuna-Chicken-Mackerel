import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Assassin extends Enemy {
    private int t, atk_t;
    private int projectileY, projectileX;
    private EnemyProjectile[] projectile;
    private Sprite projectileSprite;
    private final int THROW_CHARGE = 40, CLOSE_CHARGE = 50, ATTACK_DUR = 20, RETREAT_DUR = 200, NUM_PROJECTILES = 12;
    private Animation assassinAnim;
    private Sound sound;

    private enum State {
        STALK("STALK"),
        SHANK("SHANK"),
        SCARED("SCARED"),
        THROW("THROW"),
        SLIDE("SLIDE");

        private String value;
        State(String string) {
            value = string;
        }
    }

    private State state;

    public Assassin(Player player, int x, int y) {
        super(player, x, y, 150, 150, 100, 20, 4, 4, 40);
        t = 0;
        atk_t = -1;
        state = State.STALK;
        projectile = new EnemyProjectile[NUM_PROJECTILES];
        sound = new Sound("sounds/knife.wav", false, 0.65f);
        for (int i = 0; i < NUM_PROJECTILES; i++) {
            projectile[i] = new EnemyProjectile(x, y, 50, 50, 1, 5, 120);
            projectileSprite = new Sprite(projectile[i], "images/knife.gif");
            projectile[i].setDrawable(projectileSprite);
        }
        assassinAnim = new Animation(this, "images/AssassinSpriteSheet.gif", 4, 5, 60, true, Drawable.LEFT);
        assassinAnim.rowAnim(State.STALK.value, 0);
        assassinAnim.rowAnim(State.SHANK.value, 1);
        assassinAnim.rowAnim(State.SCARED.value, 0);
        assassinAnim.rowAnim(State.THROW.value, 2);
        assassinAnim.rowAnim("DEFAULT", 3);
        assassinAnim.setState("DEFAULT");
        drawable = assassinAnim;
        drawable.setImageFX(damageFX);
    }

    public void update() {
        projectileY = (int) getBoundingBox().getY() + 30;
        if (isFacingRight)
            projectileX = (int) getBoundingBox().getMaxX();
        else
            projectileX = (int) getBoundingBox().getX() - 20;
        stopAttack();
        if (!playerFacingMe()) {
            state = State.STALK;
            if (inCloseRange())
                state = State.SHANK;
        } else {
            if (inCloseRange()) {
                state = State.SCARED;
            } else {
                if (inLongRange() && state != State.SLIDE) {
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
                assassinAnim.setLoop(true);
                assassinAnim.setState(current.value);
                break;
            case SHANK:
                t++;
                if (t == CLOSE_CHARGE) {
                    t = 0;
                    atk_t = 0;
                    attack();
                    assassinAnim.setLoop(false);
                    assassinAnim.setState(current.value);
                }
                if (atk_t >= 0) {
                    if (atk_t == ATTACK_DUR) {
                        atk_t = -2;
                        assassinAnim.setState("DEFAULT");
                        assassinAnim.setLoop(true);
                    }
                    atk_t++;
                }
                break;
            case SCARED:
                assassinAnim.setLoop(true);
                assassinAnim.setState(current.value);
                retreat();
                t++;
                if (t == RETREAT_DUR) {
                    t = 0;
                    state = State.STALK;
                }
                break;
            case THROW:
                t++;
                if (!sound.isPlaying())
                    sound.play();
                assassinAnim.setLoop(false);
                assassinAnim.setState(current.value);
                if (t == THROW_CHARGE) {
                    t = 0;
                    assassinAnim.setLoop(true);
                    assassinAnim.setState("DEFAULT");
                    throwKnife();
                    state = State.SLIDE;
                }
                break;
            case SLIDE:
                assassinAnim.setLoop(true);
                assassinAnim.setState(State.STALK.value);
                if (targetY(player.getY())) {
                    state = State.THROW;
                }
            default:
                break;
        }
    }

    private void throwKnife() {
        int i = 0;
        while (projectile[i].isActive()) {
            i = (i + 1) % NUM_PROJECTILES;
        }
        projectile[i].fire(projectileX, projectileY, isFacingRight);
    }

    private boolean inCloseRange() {
        return generateAttackBoundingBox().intersects(player.getBoundingBox());
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

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D.Double(x + 30, y, width - 60, height);
    }

    protected Rectangle2D generateAttackBoundingBox() {
        Rectangle2D b = getBoundingBox();
        int xValue = (int) b.getX() + 50;
        if (!isFacingRight)
            xValue -= 90;
        return new Rectangle2D.Double(xValue, y + 30, 60, height - 100);
    }
}