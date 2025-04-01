import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class ExampleEnemy extends Enemy{
    int time, maxTime;
    Color color;

    public ExampleEnemy(Player player, int x, int y, int maxTime) {
        super(player, x, y, 40, 40, 100, 0, 20, 20, 20, 50, 50);
        time = 0;
        if (maxTime <= 0)
            maxTime = 1;
        this.maxTime = maxTime;
    }


    public void update() {
        time++;
        if (time == maxTime)
        {
            time = 0;
            super.update();
        }

        if (inRange())
            color = Color.BLUE;
        else
            color = Color.RED;

    }


    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
    }
    
}
