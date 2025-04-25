import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class TheDon extends Enemy {
    private Random rand;
    private int t, atk_t, big_t;
    private EnemyProjectile[] projPool;
    private EnemyProjectile[] bigProjPool;
    private EnemyProjectile[] mortarPool;
    private Treadmill[] treadPool;
    private final int SWITCH = 500;
    private final int MAX_LEFT = 200, MAX_RIGHT = 800, CENTER_X = 600, CENTER_Y = 600;
    private final int NUM_PROJECTILES = 50, SHOOT_CHARGE = 10, MORTAR_CHARGE = 100, BIG_SHOOT_CHARGE = 25,
            NUM_LANES = 8;
    private final int NUM_TREADS = 4, TREAD_HEIGHT = 125, TREAD_WIDTH = 650, TREAD_LANE_OFFSET = 10, TREAD_CHARGE = 30;
    private final float BIG_PROJ_FACTOR = 0.8f;
    private int MELEE_CHARGE, meleeCount;
    private float res;

    private enum State {
        PUNCH,
        SHOOT,
        TREAD,
        MORTAR
    }

    private State state;

    public TheDon(Player player, int x, int y) {
        super(player, x, y, 80, 80, 600, 10, 4, 4, 250);
        rand = new Random();
        t = 0;
        atk_t = -1;
        big_t = 0;
        MELEE_CHARGE = 40;
        meleeCount = 0;
        res = 0;
        state = rand.nextInt() % 2 == 0 ? State.PUNCH : State.SHOOT;
        projPool = new EnemyProjectile[NUM_PROJECTILES];
        mortarPool = new EnemyProjectile[NUM_PROJECTILES];
        bigProjPool = new EnemyProjectile[NUM_PROJECTILES];
        for (int i = 0; i < NUM_PROJECTILES; i++) {
            projPool[i] = new EnemyProjectile(x, y, 30, 30, 15, 8, 60);
            Sprite projSprite = new Sprite(projPool[i], "images/bullet.gif");
            projPool[i].setDrawable(projSprite);

            mortarPool[i] = new EnemyProjectile(x, y, 100, 100, 20, 6, 120);
            Sprite mortarSprite = new Sprite(mortarPool[i], "images/mortar.gif", Drawable.LEFT);
            mortarPool[i].setDrawable(mortarSprite);

            bigProjPool[i] = new EnemyProjectile(x, y, Math.round(TREAD_HEIGHT * BIG_PROJ_FACTOR),
                    Math.round(TREAD_HEIGHT * BIG_PROJ_FACTOR), 15, 6, 120);
            Animation bigProjAnim = new Animation(bigProjPool[i], "images/bowling.gif", 1, 8, 60, true);
            bigProjAnim.rowAnim("ROLL", 0);
            bigProjAnim.setState("ROLL");
            bigProjPool[i].setDrawable(bigProjAnim);
        }
        treadPool = new Treadmill[NUM_TREADS];
        for (int i = 0; i < NUM_TREADS; i++) {
            treadPool[i] = new Treadmill(MAX_RIGHT - TREAD_WIDTH - 5,
                    i * TREAD_HEIGHT + CENTER_Y - (NUM_TREADS / 2) * TREAD_HEIGHT + i * TREAD_LANE_OFFSET, TREAD_WIDTH,
                    TREAD_HEIGHT, Math.round(0.9f * player.getDx()), "LEFT");
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
                res = 0;
                if (inRange()) {
                    t++;
                    if (t == MELEE_CHARGE) {
                        t = 0;
                        if (meleeCount == 4) {
                            damage = 10;
                            MELEE_CHARGE = 100;
                        } else {
                            damage *= 1.05f;
                            MELEE_CHARGE = 40;
                            attack();
                        }
                        meleeCount = (meleeCount + 1) % 5;
                    }
                } else {
                    t = 0;
                    moveToPlayer();
                }
                break;
            case SHOOT:
                res = 0.5f;
                if (x > MAX_LEFT) {
                    t = 0;
                    move("LEFT");
                } else {
                    facePlayer();
                    matchY();
                    t++;
                    if (t == SHOOT_CHARGE) {
                        t = 0;
                        shootHim();
                    }
                    if (inRange()) {
                        state = State.PUNCH;
                    }
                }
                break;
            case TREAD:
                res = 1;
                if (x < MAX_RIGHT) {
                    move("RIGHT");
                }
                if (y < CENTER_Y) {
                    move("DOWN");
                }
                if (y > CENTER_Y) {
                    move("UP");
                }
                if (x == MAX_RIGHT && y == CENTER_Y) {
                    t++;
                    facePlayer();
                } else {
                    t = 0;
                }
                if (!treadPool[0].isActive() && t == TREAD_CHARGE) {
                    t = 0;
                    for (Treadmill tread : treadPool) {
                        tread.setActive(true);
                    }
                }
                if (treadPool[0].isActive() && t == BIG_SHOOT_CHARGE) {
                    t = 0;
                    shootHimRandomly();
                }
                break;
            case MORTAR:
                res = 0.15f;
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
        while (projPool[i].isActive()) {
            i++;
        }
        projPool[i].fire(x, player.getX(), y, player.getY());
    }

    private void shootHimRandomly() {
        int i = 0;
        while (bigProjPool[i].isActive()) {
            i++;
        }
        int lane = rand.nextInt(NUM_TREADS);
        int topY = CENTER_Y - (NUM_TREADS / 2) * TREAD_HEIGHT;
        topY += TREAD_HEIGHT / 2 - (BIG_PROJ_FACTOR * TREAD_HEIGHT / 2);
        int yPos = lane;
        yPos *= TREAD_HEIGHT;
        yPos += lane * TREAD_LANE_OFFSET;
        yPos += topY;
        bigProjPool[i].fire(x, yPos, false);
    }

    private void bombHim() {
        int[] xPos = new int[NUM_LANES];
        for (int j = 0; j < 5; j++) {
            int i = 0;
            while (mortarPool[i].isActive()) {
                i++;
            }
            int lane = rand.nextInt(NUM_LANES);
            while (xPos[lane] != 0)
                lane = rand.nextInt(NUM_LANES);
            xPos[lane] = lane + 1;
            xPos[lane] *= 40;
            xPos[lane] += player.getX() - (NUM_LANES / 2) * 40;
            mortarPool[i].setTrajectory(xPos[lane], xPos[lane], 100, 101);
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
        super.damaged(Math.round(damage / (1 + res)));
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
            for (int i = 0; i < NUM_TREADS; i++) {
                treadPool[i].setActive(false);
            }
            big_t = 0;
            state = State.SHOOT;
        }
    }

    private boolean inRange() {
        return getBoundingBox().intersects(player.getBoundingBox());
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }
}
