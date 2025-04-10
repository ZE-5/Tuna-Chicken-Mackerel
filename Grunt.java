import java.awt.geom.Rectangle2D;

public class Grunt extends Enemy {

    public Grunt(Player player, int x, int y, int width, int height, int health, int damage, int dx, int dy, int points) {
        super(player, x, y, width, height, health, damage, dx, dy, points);
    }

    @Override
    public void update() {
        
    }

    @Override
    protected Rectangle2D generateAttackBoundingBox() {
        return null;
    }
    
}
