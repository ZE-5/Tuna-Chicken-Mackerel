import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Projectile extends GameEntity{
    private int c, speed, timeOut, time, damage;
    private float m;
    private boolean timedOut;
    private boolean active;
    public static final int LEFT = 0, RIGHT = 1;

    public Projectile(int x1, int y1, int x2, int y2, int c, int width, int height, int damage, int speed, int timeOut)
    {
        super(x1, y1, width, height);
        this.m = 1.0f * (y2 - y1) / (x2 - x1);
        this.c = c;
        this.timeOut = timeOut;
        this.speed = Math.abs(speed);
        if (timeOut <= 0)
            timeOut = 1;

        time = 0;
        timedOut = false;
        active = false;
        this.damage = damage;
    }

    public Projectile(int width, int height, int damage, int speed, int timeOut) {
        this(0, 0, 0, 0, 0, width, height, damage, speed, timeOut);
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
        x += speed;
        y = (int) (m * x + c);
    }

    public void fire() {
        if (!active)
            active = true;
    }

    public void fire(int x1, int x2, int y1, int y2, int c, int direction) {
        x = x1;
        y = y1;
        this.m = 1.0f * (y2 - y1) / (x2 - x1);
        this.c = c;
        setDirection(direction);
        fire();
    }

    public void fire(int x, int y, int direction) {
        fire(x, x + 1, y, y, y, direction);
    }

    public void fire(int x, int y, boolean right) {
        int direction = right ? RIGHT : LEFT;
        fire(x, y, direction);
    }

    private void reset() {
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

    public void setDirection(int direction) {
        switch (direction) {
            case LEFT:
                speed = -Math.abs(speed);
                break;
            case RIGHT:
                speed = Math.abs(speed);
                break;
            default:
                break;
        }
    }
}
