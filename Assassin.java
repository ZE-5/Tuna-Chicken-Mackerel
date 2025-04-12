import java.awt.geom.Rectangle2D;

public class Assassin extends Enemy {
    public Assassin(Player player, int x, int y) {
        super(player, x, y, 40, 60, 100, 20, 5, 5, 40);
    }
    @Override
    public void update() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    protected Rectangle2D generateAttackBoundingBox() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateAttackBoundingBox'");
    }
    
}
