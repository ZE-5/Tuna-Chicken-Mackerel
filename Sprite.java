import java.awt.Graphics2D;
import java.awt.Transparency;
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
            BufferedImage in = ImageIO.read(getClass().getClassLoader().getResource(path));
            width = in.getWidth();
            height = in.getHeight();
            spriteImage = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            Graphics2D s2 = (Graphics2D) spriteImage.getGraphics();
            s2.drawImage(in, 0, 0, null);
            s2.dispose();
            spriteImage = scaleToOwner(spriteImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected BufferedImage getToDraw() {
        return spriteImage;
    }
}
