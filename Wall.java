import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;


public class Wall extends GameEntity{    
    private Color colour;

    public Wall(int x, int y, int width, int height, Color color) {
        super((width > 0 ? x : x + width), (height > 0 ? y : y + height), (width > 0 ? width : -width), (height > 0 ? height : -height));
        this.colour = color;
    }


    public Wall(int x, int y, int width, int height) {
        this(x, y, width, height, null);
        if (LevelManager.getInstance().drawDebug())
            this.colour = Color.PINK;
    }


    public void update() {}

    public Line2D getLeftLine() {
        return new Line2D.Double(x, y, x, y + height);
    }


    public Line2D getRightLine() {
        return new Line2D.Double(x + width, y, x + width, y + height);
    }


    public Line2D getTopLine() {
        return new Line2D.Double(x, y, x + width, y);
    }


    public Line2D getBottomLine() {
        return new Line2D.Double(x, y + height, x + width, y + height);
    }



    public void draw(Graphics2D g2) {
        if (colour == null)
            return;
        g2.setColor(colour);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
    }
}
