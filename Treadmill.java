import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Treadmill extends GameEntity {
    private int moveRate;
    private String direction;
    private boolean isActive;
    private Animation anim;

    public Treadmill(Player player, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.moveRate = Math.round(0.6f * player.getDx());
        this.direction = "LEFT";
        isActive = false;
        drawBehindPlayer = true;
        anim = new Animation(this, "images/tread.gif", 1, 8, true);
        anim.rowAnim("TREAD", 0);
        anim.setState("TREAD");
        drawable = anim;
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
