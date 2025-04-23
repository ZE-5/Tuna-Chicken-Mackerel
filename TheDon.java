import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class TheDon extends Enemy {
    private Random rand;
    private int t, atk_t, big_t;
    private EnemyProjectile[] pool;
    private EnemyProjectile[] mortarPool;
    private final int SWITCH = 500;
    private final int MAX_LEFT = 200, MAX_RIGHT = 800, CENTER_X = 600, CENTER_Y = 600;
    private final int NUM_PROJECTILES = 15, SHOOT_CHARGE = 50, MORTAR_CHARGE = 100;
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
        state = rand.nextInt() % 2 == 0 ? State.PUNCH : State.SHOOT;
        pool = new EnemyProjectile[NUM_PROJECTILES];
        mortarPool = new EnemyProjectile[NUM_PROJECTILES];
        for (int i = 0; i < NUM_PROJECTILES; i++) {
            pool[i] = new EnemyProjectile(x, y, 15, 15, 15, 8, 60);
            mortarPool[i] = new EnemyProjectile(x, y, 30, 30, 20, 6, 200);
        }
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

    private void act(State current) {
        switch (current) {
            case PUNCH:
                t = 0;
                moveToPlayer();
                break;
            case SHOOT:
                if (player.getX() < 500) {
                    if (x < MAX_RIGHT) {
                        move("RIGHT");
                    } else {
                        facePlayer();
                        matchY();
                        t++;
                        if (t == SHOOT_CHARGE) {
                            t = 0;
                            shootHim();
                        }
                    }
                } else {
                    if (x > MAX_LEFT) {
                        move("LEFT");
                    } else {
                        facePlayer();
                        matchY();
                        t++;
                        if (t == SHOOT_CHARGE) {
                            t = 0;
                            shootHim();
                        }
                    }
                }
                
                break;
            case TREAD:
                if (x < MAX_RIGHT) {
                    move("RIGHT");
                }
                if (y < CENTER_Y) {
                    move("DOWN");
                }
                if (y > CENTER_Y) {
                    move("UP");
                }
                break;
            case MORTAR:
                if (x < CENTER_X) {
                    move("RIGHT");
                }
                if (x > CENTER_X) {
                    move("LEFT");
                }
                if (y < CENTER_Y) {
                    move("DOWN");
                }
                if (y > CENTER_Y) {
                    move("UP");
                }
                if (x == CENTER_X && y == CENTER_Y) {
                    t++;
                } else {
                    t = 0;
                }
                if (t == MORTAR_CHARGE) {
                    t = 0;
                    bombHim();
                }
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

    private void shootHim() {
        int i = 0;
        while (pool[i].isActive()) {
            i++;
        }
        pool[i].fire(x, y, isFacingRight);
    }

    private void bombHim() {
        int[] xPos = {-1, -1, -1, -1, -1, -1, -1, -1};
        for (int j = 0; j < 5; j++) {
            int i = 0;
            while (mortarPool[i].isActive()) {
                i++;
            }
            int lane = rand.nextInt(8);
            while (xPos[lane] != -1)
                lane = rand.nextInt(8);
            xPos[lane] = lane + 1;
            xPos[lane] *= 40;
            xPos[lane] += player.getX() - 100;
            mortarPool[i].setTrajectory(xPos[lane], xPos[lane ], 100, 101);
            mortarPool[i].fire();
        }
    }

    public void draw(Graphics2D g2) {
        if (atk_t >= 0)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.GREEN);
        g2.fillRect(x, y, width, height);
        g2.drawString(state.toString() + " " + health, x, y);
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
        if (before > 400 && after <= 400)
            state = State.MORTAR;
        if (before > 200 && after <= 200)
            state = State.TREAD;
        
        if (state == State.MORTAR && after <= 300) {
            big_t = 0;
            state = State.SHOOT;
        }

        if (state == State.TREAD && after <= 100) {
            big_t = 0;
            state = State.SHOOT;
        }
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }
}
