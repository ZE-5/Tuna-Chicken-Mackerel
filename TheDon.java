import java.awt.geom.Rectangle2D;
import java.util.Random;

public class TheDon extends Enemy {
    private Random rand;
    private int t, atk_t, big_t;
    private final int CLOSE_RANGE = 20, SWITCH = 500;

    private enum State {
        PUNCH,
        SHOOT,
        TREAD,
        MORTAR
    }

    private State state;

    public TheDon(Player player, int x, int y) {
        super(player, x, y, 80, 80, 600, -1, 4, 4, 250);
        rand = new Random();
        t = 0;
        atk_t = -1;
        big_t = 0;
        state = State.PUNCH;
    }

    public void update() {
        stopAttack();
        if (state != State.TREAD && state != State.MORTAR && big_t == SWITCH) {
            big_t = 0;
            if (state == State.SHOOT)
                state = State.PUNCH;
            else if (state == State.PUNCH)
                state = State.SHOOT;
        }
        big_t++;
        act(state);
    }

    private int distFromPlayer() {
        // TODO: Calculate Euclidean distance from player
        return -1;
    }

    private void act(State current) {
        switch (current) {
            case PUNCH:
                moveToPlayer();
                break;
            case SHOOT:
                if (player.getX() < 500) {
                    if (x < MAX_RIGHT) {
                        move("RIGHT");
                    } else {
                        facePlayer();
                        matchY();
                    }
                } else {
                    if (x > MAX_LEFT) {
                        move("LEFT");
                    } else {
                        facePlayer();
                        matchY();
                    }
                }
                
                break;
            case TREAD:
                break;
            case MORTAR:
                break;
            default:
                break;
        }
    }

    private void facePlayer() {
        if (player.getX() > x) {
            isFacingRight = true;
        } else {
            isFacingRight = false;
        }
    }

    public void draw(Graphics2D g2) {
        if (atk_t >= 0)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.GREEN);
        g2.fillRect(x, y, width, height);
        g2.drawString(state.toString() + " " + big_t, x, y);
    }

    private void matchY() {
        int py = player.getY();
        if (y < py)
            move("DOWN");
        if (y > py)
            move("UP");
    }
    public void damaged(int damage) {
        int before = health;
        super.damaged(damage);
        int after = health;
        if (before > 400 && after < 400)
            state = State.MORTAR;
        if (before > 200 && after < 200)
            state = State.TREAD;
        int diff = before - after;
        if (diff >= 100 && (state == State.MORTAR || state == State.TREAD)) {

        }
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }
}
