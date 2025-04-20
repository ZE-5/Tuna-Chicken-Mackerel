import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Treadmill extends GameEntity {
    private int moveRate;
    private String direction;

    public Treadmill(int x, int y, int width, int height, int moveRate, String direction) {
        super(x, y, width, height);
        this.moveRate = moveRate;
        this.direction = direction.toUpperCase();
    }


    public void setMoveRate(int moveRate) {
        this.moveRate = moveRate;
    }


    public int getMoveRate() {
        return moveRate;
    }


    public String getDirection() {
        return direction;
    }

    public void update() {}


    public void draw(Graphics2D g2) {
        //TODO: proper sprite drawing
        g2.setColor(Color.LIGHT_GRAY);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
    }
}
