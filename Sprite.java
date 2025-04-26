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
            width = spriteImage.getWidth();
            height = spriteImage.getHeight();
            spriteImage = scaleToOwner(spriteImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected BufferedImage getToDraw() {
        return spriteImage;
    }
}
