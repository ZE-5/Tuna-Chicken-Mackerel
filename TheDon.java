import java.awt.geom.Rectangle2D;

public class TheDon extends Enemy {
    private int t, atk_t;

    public TheDon(Player player, int x, int y) {
        super(player, x, y, 80, 80, 600, -1, 4, 4, 250);
    }

    public void update() {
        
    }

    protected Rectangle2D generateAttackBoundingBox() {
        return getBoundingBox();
    }
}
