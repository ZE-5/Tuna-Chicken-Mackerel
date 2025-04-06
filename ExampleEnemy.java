import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class ExampleEnemy extends Enemy{
    int time, maxTime;

    public ExampleEnemy(Player player, int x, int y, int maxTime) {
        super(player, x, y, 40, 40, 100, 10, 20, 20, 20);
        time = 0;
        if (maxTime <= 0)
            maxTime = 1;
        this.maxTime = maxTime;
        // isVisible = false; //true by default from GameEntity constructor
    }


    private boolean inRange() {
        return getBoundingBox().intersects(player.getBoundingBox());
    }


    public void update() {
        time++;
        if (time == maxTime)
        {
            time = 0;
            if (!inRange())
            {
                if (isAttacking)
                    stopAttack();
                moveToPlayer();
            }
            else
                attack();
        }

        // if (inRange())
        //     color = Color.BLUE;
        // else
        //     color = Color.RED;

    }


    public void draw(Graphics2D g2) {
        if (isAttacking)
            g2.setColor(Color.RED);
        else
            g2.setColor(Color.BLUE);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
    }


    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }

    
}
