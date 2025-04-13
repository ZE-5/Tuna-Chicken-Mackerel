import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class GameEntity {
    protected int x, y, width, height;
    protected Drawable drawable;
    protected boolean isVisible;
    private LevelManager levelManager;
    //TODO: add sounds

    public GameEntity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        drawable = null;
        isVisible = true;
        levelManager = LevelManager.getInstance();
        levelManager.register(this);
    }


    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }


    public boolean isVisible() {
        return isVisible;
    }


    public abstract void update();


    public void draw(Graphics2D g2){
        //TODO: proper sprite drawing
        g2.setColor(Color.magenta);
        g2.draw(new Rectangle2D.Double(x, y, width, height));
    }


    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public int setX(int x) {
        return this.x = x;
    }


    public int setY(int y) {
        return this.y = y;
    }
    
    
    public int[] getLocation() {
        int[] loc = {x, y};
        return loc;
    }
    
    
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public Rectangle2D getBoundingBox() {
            return new Rectangle2D.Double(x, y, width, height);
    }
}
