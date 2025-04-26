public abstract class ImageFXMod {
    protected ImageFX fx;
    protected int t;

    public ImageFXMod(ImageFX fx) {
        this.fx = fx;
        t = -1;
    }

    public void update() {
        t++;
        process();
    }

    protected abstract void process();
}
