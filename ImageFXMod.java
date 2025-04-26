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

    protected abstract void process();
}
