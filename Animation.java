import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Animation extends Drawable {
    private HashMap<String, ArrayList<BufferedImage>> animMap;
    private int step, numCols, numRows;
    private long now, diff, target;
    private String currentState, modifier;
    private boolean loop;

    public Animation(GameEntity owner, String path, int numRows, int numCols, long target, boolean loop, boolean defaultDirection) {
        super(owner, path, defaultDirection);
        animMap = new HashMap<>();
        this.target = target;
        // step = -1;
        step = 0;
        now = System.currentTimeMillis();
        this.owner = owner;
        this.path = path;
        this.numCols = numCols;
        this.numRows = numRows;
        modifier = "";
        this.loop = loop;
    }

    public Animation(GameEntity owner, String path, int numRows, int numCols, long target, boolean loop) {
        this(owner, path, numRows, numCols, target, loop, Drawable.RIGHT);
    }

    public Animation(GameEntity owner, String path, int numRows, int numCols, long target) {
        this(owner, path, numRows, numCols, target, false);
    }

    public Animation(GameEntity owner, String path, int numRows, int numCols) {
        this(owner, path, numRows, numCols, 33, false);
    }

    public Animation(GameEntity owner, String path, int numRows, int numCols, boolean loop) {
        this(owner, path, numRows, numCols, 33, loop);
    }

    public void setState(String state) {
        if (!state.equals(currentState))
            step = 0;
        currentState = state + modifier;
    }

    public void setStateIgnoreCurrent(String state) {
        currentState = state + modifier;
    }

    public void resetStep() {
        step = 0;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void rowAnim(String stateName, int row) {
        ArrayList<BufferedImage> string = new ArrayList<>();
        try {
            BufferedImage in = ImageIO.read(getClass().getClassLoader().getResource(path));
            width = in.getWidth() / numCols;
            height = in.getHeight() / numRows;
            for (int i = 0; i < numCols; i++) {
                BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D f2 = (Graphics2D) frame.getGraphics();
                f2.drawImage(in, 0, 0, width, height, i * width, row * height, i * width + width, row * height + height,
                        null);
                frame = scaleToOwner(frame);
                string.add(frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        animMap.put(stateName, string);
    }

    public BufferedImage getCurrentFrame() {
        return animMap.get(currentState).get(step);
    }

    public void draw(Graphics2D g2) {
        ArrayList<BufferedImage> string = animMap.get(currentState);
        diff = System.currentTimeMillis() - now;
        if (diff >= target) {
            if (loop)
                step = (step + 1) % string.size();
            else
                step++;
            now = System.currentTimeMillis();
        }
        x = owner.getX();
        y = owner.getY();
        if (step >= string.size())
            step = string.size() - 1;
        BufferedImage toDraw = string.get(step);
        if (owner.isFacingRight() != defaultDirection) {
            toDraw = flip(toDraw);
        }
        g2.drawImage(toDraw, x, y, null);
    }
}
