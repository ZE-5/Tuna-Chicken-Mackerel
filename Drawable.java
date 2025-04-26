import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public abstract class Drawable {
    protected boolean defaultDirection;
    protected int x, y, width, height;
    protected GameEntity owner;
    protected String path;
    public final static boolean RIGHT = true, LEFT = false;

    public Drawable(GameEntity owner, String path, boolean defaultDirection) {
        this.owner = owner;
        this.path = path;
        this.defaultDirection = defaultDirection;
    }

    public abstract void draw(Graphics2D g2);

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
}
