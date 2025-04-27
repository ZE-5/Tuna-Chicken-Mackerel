import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public abstract class Drawable {
    protected boolean defaultDirection;
    protected int x, y, width, height, flipOffset;
    protected GameEntity owner;
    protected String path;
    protected ImageFX fx;
    protected GraphicsEnvironment env;
    protected GraphicsDevice device;
    protected GraphicsConfiguration config;
    public final static boolean RIGHT = true, LEFT = false;

    public Drawable(GameEntity owner, String path, boolean defaultDirection, int flipOffset) {
        this.owner = owner;
        this.path = path;
        this.defaultDirection = defaultDirection;
        env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = env.getDefaultScreenDevice();
        config = device.getDefaultConfiguration();
        this.flipOffset = flipOffset;
    }

    public void draw(Graphics2D g2) {
        x = owner.getX();
        y = owner.getY();
        int xOffset = 0;
        BufferedImage toDraw = getToDraw();
        if (owner.isFacingRight() != defaultDirection) {
            xOffset = flipOffset;
            toDraw = flip(toDraw);
        }
        if (fx != null) {
            toDraw = fx.get(toDraw);
        }
        g2.drawImage(toDraw, x + xOffset, y, null);
    }

    protected abstract BufferedImage getToDraw();

    protected BufferedImage flip(BufferedImage toDraw) {
        AffineTransform af = new AffineTransform();
        af.setToScale(-1, 1);
        af.translate(-owner.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        toDraw = op.filter(toDraw, null);
        return toDraw;
    }

    protected BufferedImage scaleToOwner(BufferedImage input) {
        AffineTransform af = new AffineTransform();
        af.setToScale(owner.getWidth() * 1f / width, owner.getHeight() * 1f / height);
        AffineTransformOp op = new AffineTransformOp(af, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(input, null);
    }

    public void setDefaultDirection(boolean defaultDirection) {
        this.defaultDirection = defaultDirection;
    }

    public void setImageFX(ImageFX fx) {
        this.fx = fx;
    }
}
