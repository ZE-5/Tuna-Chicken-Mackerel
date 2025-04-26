import java.awt.image.BufferedImage;

public class DisintegrateFX extends ImageFX {
    private float amount;

    public static final String AMOUNT = "DisintegrateAmount";
    public DisintegrateFX() {
        parameters.put(AMOUNT, amount);
    }

    public BufferedImage doFX(BufferedImage in) {
        int width = in.getWidth();
        int height = in.getHeight();
        int[] pixels = new int[width * height];
        in.getRGB(0, 0, width, height, pixels, 0, width);
        for (int i = 0; i < width * height; i += 2) {
            if (pixels[i] == 0)
                continue;
            int alpha = pixels[i] >> 24 & 255;
            alpha = Math.round(parameters.get(AMOUNT).floatValue() * 255);
            pixels[i] = (alpha << 24) | pixels[i] << 8;
        }
        out.setRGB(0, 0, width, height, pixels, 0, width);
        return out;
    }
}
