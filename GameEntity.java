import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class GameEntity {
    protected int x, y, width, height;
    //TODO: add sounds

    public GameEntity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    public abstract void update();


    public void draw(Graphics2D g2){
        //TODO: proper sprite drawing
        g2.setColor(Color.magenta);
        g2.draw(new Rectangle2D.Double(x, y, width, height));
    }


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public Rectangle2D getBoundingBox() {
            return new Rectangle2D.Double(x, y, width, height);
    }
}
