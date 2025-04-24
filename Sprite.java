import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Sprite extends Drawable{
    private BufferedImage spriteImage;

    public Sprite(GameEntity owner, String path, boolean defaultDirection) {
        super(owner, path, defaultDirection);
        loadSprite();
    }

    public Sprite(GameEntity owner, String path) {
        this(owner, path, Drawable.RIGHT);
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
