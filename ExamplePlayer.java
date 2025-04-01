import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class ExamplePlayer extends Player {
    Color color;

    public ExamplePlayer(int x, int y) {
        super(x, y, 10, 10, 100, 10, 10, 10);
    }


    public void update() {
        super.update();
        if (isAttacking())
            color = Color.WHITE;
        else
            color = Color.MAGENTA;
    }


    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.draw(new Rectangle2D.Double(x, y, width, height));
    }
}
