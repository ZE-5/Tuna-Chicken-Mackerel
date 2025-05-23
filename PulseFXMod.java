public class PulseFXMod extends ImageFXMod {
    private int maxSteps, descendTarget;
    private float min, max, step;

    public PulseFXMod(ImageFX fx, String parameterName, float min, float max, int maxSteps) {
        super(fx, parameterName);
        this.min = Math.clamp(min, 0, 1);
        this.max = Math.clamp(max, 0, 1);
        this.maxSteps = maxSteps;
        step = (this.max - this.min) / maxSteps;
        descendTarget = maxSteps * 2;
        current = this.min;
    }

    public PulseFXMod(ImageFX fx, String parameterName, int maxSteps) {
        this(fx, parameterName, 0, 1, maxSteps);
    }

    public PulseFXMod(ImageFX fx, String parameterName) {
        this(fx, parameterName, 0, 1, 10);
    }

    public void process() {
        float currFloat = current.floatValue();
        if (t < maxSteps)
            currFloat += step;
        else if (t < descendTarget) {
            currFloat -= step;
        }
        if (t >= descendTarget) {
            current = min;
            t = -1;
        }
        current = currFloat;
    }
}
