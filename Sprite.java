import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Sprite implements Drawable{
    private GameEntity owner;
    private String path;
    private BufferedImage spriteImage;
    private int x, y;

    public Sprite(GameEntity owner, String path) {
        this.owner = owner;
        this.path = path;
        loadSprite();
    }

    private void loadSprite() {
        try {
            spriteImage = ImageIO.read(getClass().getClassLoader().getResource(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        x = owner.getX();
        y = owner.getY();
        BufferedImage toDraw = spriteImage;
        g2.drawImage(toDraw, x, y, null);
    }
}
