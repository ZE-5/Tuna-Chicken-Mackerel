public abstract class Projectile extends GameEntity{
    private int c, speed, timeOut, time, damage;
    private float m;
    private boolean timedOut;

    public Projectile(int x1, int y1, int x2, int y2, int c, int width, int height, int damage, int speed, int timeOut)
    {
        super(x1, y1, width, height);
        this.m = 1.0f * (y2 - y1) / (x2 - x1);
        this.c = c;
        this.timeOut = timeOut;
        this.speed = speed;
        if (timeOut <= 0)
            timeOut = 1;

        time = 0;
        timedOut = false;
        this.damage = damage;
    }

    


    public void update() {
        time++;
        if (time == timeOut)
        {
            timedOut = true;
            return;
        }
        x += speed;
        y = (int) (m * x + c);
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
}
