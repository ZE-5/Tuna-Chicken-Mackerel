import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Projectile extends GameEntity{
    private int x2, y2;
    private int speed, timeOut, time, damage;
    private float trueX, trueY, dx, dy;
    private boolean timedOut;
    private boolean active;
    public static final int LEFT = 0, RIGHT = 1;

    public Projectile(int x1, int y1, int x2, int y2, int width, int height, int damage, int speed, int timeOut)
    {
        super(x1, y1, width, height);
        this.x2 = x2;
        this.y2 = y2;
        trueX = x1;
        trueY = y1;
        calcGrads();
        this.timeOut = timeOut;
        this.speed = Math.abs(speed);
        if (timeOut <= 0)
            timeOut = 1;

        time = 0;
        timedOut = false;
        active = false;
        this.damage = damage;
    }

    public Projectile(int width, int height, int damage, int speed, int timeOut) { // Assume horizontal line
        this(0, 0, 1, 0, width, height, damage, speed, timeOut);
    }

    public void draw(Graphics2D g2) {
        if (!active)
            return;
        g2.setColor(Color.RED);
        g2.fillOval(x, y, width, height);
    }


    public void update() {
        if (!active) {
            return;
        }
        time++;
        if (time == timeOut)
        {
            reset();
            return;
        }
        trueX += dx * speed;
        trueY += dy * speed;
        x = (int) trueX;
        y = (int) trueY;
    }

    public void fire() {
        if (!active)
            active = true;
    }

    public void fire(int x1, int x2, int y1, int y2) {
        trueX = x = x1;
        trueY = y = y1;
        this.x2 = x2;
        this.y2 = y2;
        calcGrads();
        fire();
    }

    public void fire(int x, int y, int direction) {
        if (direction == LEFT)
            fire(x, x - 1, y, y);
        else
            fire(x, x + 1, y, y);
    }

    public void fire(int x, int y, boolean right) {
        int direction = right ? RIGHT : LEFT;
        fire(x, y, direction);
    }

    public void reset() {
        active = false;
        time = 0;
    }

    public boolean isActive() {
        return active;
    }

    public boolean timedOut()
    {
        return timedOut;
    }


    public int getDamage() {
        return damage;
    }    


    public void delete(){
        timedOut = true;
    }

    public void setTrajectory(int x1, int x2, int y1, int y2) {
        setSource(x1, y1);
        setDestination(x2, y2);
    }

    public void setSource(int x, int y) {
        trueX = this.x = x;
        trueY = this.y = y;
        calcGrads();
    }

    public void setDestination(int x, int y) {
        this.x2 = x;
        this.y2 = y;
        calcGrads();
    }

    private void calcGrads() {
        int diffX = x2 - x;
        int diffY = y2 - y;
        float hyp = (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)); // Pythagoras strikes again!!
        dx = diffX / hyp;
        dy = diffY / hyp;
    }
}
