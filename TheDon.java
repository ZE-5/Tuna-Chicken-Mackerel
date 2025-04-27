import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class TheDon extends Enemy {
    private Random rand;
    private int t, big_t;
    private EnemyProjectile[] projPool;
    private EnemyProjectile[] bigProjPool;
    private EnemyProjectile[] mortarPool;
    private Treadmill[] treadPool;
    private final int SWITCH = 500;
    private final int MAX_LEFT = 2330, MAX_RIGHT = 4210, CENTER_X = 3440, CENTER_Y = 1200;
    private final int NUM_PROJECTILES = 50, SHOOT_CHARGE = 10, MORTAR_CHARGE = 100, BIG_SHOOT_CHARGE = 25,
            NUM_LANES = 10;
    private final int NUM_TREADS = 8, TREAD_HEIGHT = 295, TREAD_WIDTH = 1534, TREAD_TOP = 265, TREAD_DISPLACE = 90, TREAD_LANE_OFFSET = 10, TREAD_CHARGE = 30;
    private final float BIG_PROJ_FACTOR = 0.8f;
    private int MELEE_CHARGE, meleeCount;
    private final int NUM_ATK = 2;
    private int projX, projY;
    private float res;

    private enum State {
        PUNCH("PUNCH"),
        SHOOT("SHOOT"),
        TREAD("TREAD"),
        MORTAR("MORTAR");

        private String value;
        private State(String value) {
            this.value = value;
        }
    }

    private State state;
    private Animation bossAnim;
    private Sound[] shot, hit; 
    private Sound bomb;
    private Sound mortar;

    public TheDon(Player player, int x, int y) {
        super(player, x, y, 320, 240, 600, 10, 8, 8, 250);
        rand = new Random();
        t = 0;
        big_t = 0;
        MELEE_CHARGE = 40;
        meleeCount = 0;
        res = 0;
        state = rand.nextInt() % 2 == 0 ? State.PUNCH : State.SHOOT;
        shot = new Sound[NUM_PROJECTILES];
        bomb = new Sound("sounds/bomb.wav", false, 0.7f);
        mortar = new Sound("sounds/grenades.wav", false, 0.7f);
        hit = new Sound[3];
        for (int i = 0; i < 3; i++) {
            hit[i] = new Sound("sounds/punch.wav", false, 0.7f);
        }
        for (int i = 0; i < NUM_PROJECTILES; i++) {
            shot[i] = new Sound("sounds/gun.wav", false, 0.5f);
        }
        projPool = new EnemyProjectile[NUM_PROJECTILES];
        mortarPool = new EnemyProjectile[NUM_PROJECTILES];
        bigProjPool = new EnemyProjectile[NUM_PROJECTILES];
        for (int i = 0; i < NUM_PROJECTILES; i++) {
            projPool[i] = new EnemyProjectile(x, y, 30, 30, 15, 8, 160);
            Sprite projSprite = new Sprite(projPool[i], "images/bullet.gif");
            projPool[i].setDrawable(projSprite);

            int mortarSpeed = rand.nextInt(4) + 8;
            int mortarTimeOut = rand.nextInt(20) + 110;
            mortarPool[i] = new EnemyProjectile(x, y, 100, 100, 20, mortarSpeed, mortarTimeOut);
            Sprite mortarSprite = new Sprite(mortarPool[i], "images/mortar.gif", Drawable.LEFT);
            mortarPool[i].setDrawable(mortarSprite);

            bigProjPool[i] = new EnemyProjectile(x, y, Math.round(TREAD_HEIGHT * BIG_PROJ_FACTOR),
                    Math.round(TREAD_HEIGHT * BIG_PROJ_FACTOR), 15, 8, 280);
            Animation bigProjAnim = new Animation(bigProjPool[i], "images/bowling.gif", 1, 8, 60, true);
            bigProjAnim.rowAnim("ROLL", 0);
            bigProjAnim.setState("ROLL");
            bigProjPool[i].setDrawable(bigProjAnim);
        }
        treadPool = new Treadmill[NUM_TREADS];
        for (int i = 0; i < NUM_TREADS; i++) {
            treadPool[i] = new Treadmill(player, MAX_RIGHT - TREAD_WIDTH - TREAD_DISPLACE,
                    TREAD_TOP + i * TREAD_HEIGHT + i * TREAD_LANE_OFFSET, TREAD_WIDTH, TREAD_HEIGHT);
        }
        bossAnim = new Animation(this, "images/TheDonSpriteSheet.gif", 6, 8, 60, true, Drawable.LEFT, 80);
        bossAnim.rowAnim("WALK", 0);
        bossAnim.rowAnim(State.SHOOT.value, 1);
        bossAnim.rowAnim(State.PUNCH + "0", 2);
        bossAnim.rowAnim(State.PUNCH + "1", 3);
        bossAnim.rowAnim(State.MORTAR.value, 4);
        bossAnim.rowAnim(State.TREAD.value, 5);
        drawable = bossAnim;
        drawable.setImageFX(damageFX);
    }

    public void update() {
        if (isFacingRight) {
            projX = (int) getBoundingBox().getMaxX() + 15;
        } else {
            projX = (int) getBoundingBox().getMinX() - 25;
        }
        projY = y + 20;
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

    public void move(String direction) {
        super.move(direction);
    }

    private void act(State current) {
        switch (current) {
            case PUNCH:
                res = 0;
                for (int i = 0; i < NUM_TREADS; i++) {
                    treadPool[i].setActive(false);
                }
                if (inRange()) {
                    t++;
                    if (t == MELEE_CHARGE) {
                        t = 0;
                        bossAnim.resetStep();
                        bossAnim.setLoop(false);
                        bossAnim.setState(State.PUNCH + String.valueOf(meleeCount));
                        if (meleeCount == NUM_ATK - 1) {
                            damage = 10;
                            MELEE_CHARGE = 50;
                        } else {
                            damage *= 1.05f;
                            MELEE_CHARGE = 20;
                            attack();
                        }
                        hit[meleeCount].play();
                        meleeCount = (meleeCount + 1) % NUM_ATK;
                    }
                } else {
                    t = 0;
                    bossAnim.setLoop(true);
                    bossAnim.setState("WALK");
                    moveToPlayer();
                }
                break;
            case SHOOT:
                res = 0.5f;
                for (int i = 0; i < NUM_TREADS; i++) {
                    treadPool[i].setActive(false);
                }
                if (targetX(MAX_LEFT)) {
                    facePlayer();
                    t++;
                    if (t == SHOOT_CHARGE) {
                        t = 0;
                        shootHim();
                        bossAnim.setLoop(true);
                        bossAnim.setState(current.value);
                    }
                    if (inRange()) {
                        state = State.PUNCH;
                    }
                } else {
                    t = 0;
                    bossAnim.setLoop(true);
                    bossAnim.setState("WALK");
                }
                break;
            case TREAD:
                res = 1;
                if (target(MAX_RIGHT, CENTER_Y)) {
                    t++;
                    facePlayer();
                    bossAnim.setLoop(true);
                    bossAnim.setState(current.value);
                } else {
                    bossAnim.setLoop(true);
                    bossAnim.setState("WALK");
                    t = 0;
                }
                if (!treadPool[0].isActive()) {
                    t = 0;
                    for (Treadmill tread : treadPool) {
                        tread.setActive(true);
                    }
                }
                if (treadPool[0].isActive() && t == BIG_SHOOT_CHARGE) {
                    t = 0;
                    shootHimRandomly();
                }
                if (player.getBoundingBox().getX() > MAX_RIGHT - TREAD_DISPLACE)
                    state = State.PUNCH;
                break;
            case MORTAR:
                res = 0.15f;
                if (target(CENTER_X, CENTER_Y)) {
                    t++;
                    if (!mortar.isPlaying())
                        mortar.play();
                    bossAnim.setLoop(true);
                    bossAnim.setState(current.value);
                } else {
                    bossAnim.setLoop(true);
                    bossAnim.setState("WALK");
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
        if (player.getBoundingBox().getX() > getBoundingBox().getX()) {
            isFacingRight = true;
        } else {
            isFacingRight = false;
        }
    }

    private void shootHim() {
        int i = 0;
        while (projPool[i].isActive()) {
            i = (i + 1) % NUM_PROJECTILES;
        }
        projPool[i].fire(projX, player.getX(), projY, player.getY());
        shot[i].play();
    }

    private void shootHimRandomly() {
        int i = 0;
        while (bigProjPool[i].isActive()) {
            i = (i + 1) % NUM_PROJECTILES;
        }
        int lane = rand.nextInt(NUM_TREADS);
        int topY = TREAD_TOP;
        topY += TREAD_HEIGHT / 2 - (BIG_PROJ_FACTOR * TREAD_HEIGHT / 2);
        int yPos = lane;
        yPos *= TREAD_HEIGHT;
        yPos += lane * TREAD_LANE_OFFSET;
        yPos += topY;
        yPos -= 20; // very important
        bigProjPool[i].fire(x, yPos, false);
    }

    private void bombHim() {
        int[] xPos = new int[NUM_LANES];
        for (int j = 0; j < 8; j++) {
            int i = 0;
            while (mortarPool[i].isActive()) {
                i = (i + 1) % NUM_PROJECTILES;
            }
            int lane = rand.nextInt(NUM_LANES);
            while (xPos[lane] != 0)
                lane = rand.nextInt(NUM_LANES);
            xPos[lane] = lane + 1;
            int laneWidth = rand.nextInt(20) + 60;
            xPos[lane] *= laneWidth;
            xPos[lane] += player.getX() - (NUM_LANES / 2) * laneWidth ;
            mortarPool[i].setTrajectory(xPos[lane], xPos[lane], TREAD_TOP, TREAD_TOP + 1);
            mortarPool[i].fire();
        }
        bomb.play();
    }

    public void draw(Graphics2D g2) {
        super.draw(g2);
        g2.setColor(Color.WHITE);
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
        Rectangle2D b = getBoundingBox();
        int xValue = (int) b.getX() + 100;
        if (!isFacingRight)
            xValue -= 150;
        return new Rectangle2D.Double(xValue, y + 30, width - 200, height - 100);
    }

    public Rectangle2D getBoundingBox() {
        return new Rectangle2D.Double(x +  120, y, width - 165, height - 20);
    }
}
