import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public abstract class ImageFX {
    protected BufferedImage out;
    protected ImageFXMod mod;
    protected GraphicsEnvironment env;
    protected GraphicsDevice device;
    protected GraphicsConfiguration config;
    protected boolean isActive;
    protected HashMap<String, Number> parameters;

    public ImageFX() {
        env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = env.getDefaultScreenDevice();
        config = device.getDefaultConfiguration();
        parameters = new HashMap<>();
    }

    public BufferedImage get(BufferedImage in) {
        BufferedImage copy = config.createCompatibleImage(in.getWidth(), in.getHeight(), Transparency.TRANSLUCENT);
        Graphics2D c2 = (Graphics2D) copy.getGraphics();
        c2.drawImage(in, 0, 0, null);
        c2.dispose();
        if (mod != null)
            mod.update();
        out = config.createCompatibleImage(in.getWidth(), in.getHeight(), Transparency.TRANSLUCENT);
        return isActive ? doFX(copy) : in;
    }

    public void setActive(boolean value) {
        isActive = value;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setParameter(String name, Number value) {
        parameters.put(name, value);
    }

    public Number getParameter(String name) {
        return parameters.get(name);
    }

    protected abstract BufferedImage doFX(BufferedImage in);

    public void setMod(ImageFXMod mod) {
        this.mod = mod;
    }
}
