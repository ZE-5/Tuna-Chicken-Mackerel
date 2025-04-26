public abstract class ImageFXMod {
    protected ImageFX fx;
    protected String parameterName;
    protected int t;
    protected Number current;

    public ImageFXMod(ImageFX fx, String parameterName) {
        this.fx = fx;
        this.parameterName = parameterName;
        t = -1;
    }

    public void update() {
        t++;
        process();
        fx.setParameter(parameterName, current);
    }

    public void reset() {
        t = -1;
    }
    
    protected abstract void process();
}
