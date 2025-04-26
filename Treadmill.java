import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Treadmill extends GameEntity {
    private int moveRate;
    private String direction;
    private boolean isActive;

    public Treadmill(int x, int y, int width, int height, int moveRate, String direction) {
        super(x, y, width, height);
        this.moveRate = moveRate;
        this.direction = direction.toUpperCase();
        isActive = false;
        drawBehindPlayer = true;
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
        if (!isActive)
            return;
        drawable.draw(g2);
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }
}
