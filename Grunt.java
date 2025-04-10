import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Grunt extends Enemy {

    public Grunt(Player player, int x, int y) {
        super(player, x, y, 40, 40, 100, 10, 1, 1, 20);
    }

    public void update() {
        moveToPlayer();
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.GREEN);
        g2.fillRect(x, y, width, height);
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return null;
    }
    
}
