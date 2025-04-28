import java.util.Vector;

public abstract class Enemy extends Character {
    protected Player player;
    protected int points;
    protected Vector<Pickup> pickups;

    public Enemy(Player player, int x, int y, int width, int height, int health, int damage, int dx, int dy, int points) {
        super(x, y, width, height, health, damage, dx, dy);
        this.player = player;
        this.points = points;
        setVisible(false);

        pickups = new Vector<Pickup>();
        
        if (Math.random() < 0.3)
        {
            Pickup pickup = new HealthPickup(x, y);
            pickup.setVisible(false);
            pickups.add(pickup);
        }
        if (Math.random() < 0.3)
        {
            Pickup pickup = new StrengthPickup(x, y);
            pickup.setVisible(false);
            pickups.add(pickup);
        }
    }


    public void dropPickup() {
        if (pickups == null || pickups.isEmpty())
            return;

        for (Pickup pickup : pickups) {
            pickup.setX((int) (getBoundingBox().getX() + getBoundingBox().getWidth() / 2 - pickup.getWidth() / 2));
            pickup.setY((int) (getBoundingBox().getY() + getBoundingBox().getHeight() - pickup.getHeight() - 10));
            if (pickup instanceof HealthPickup) {
                setX(getX() - 4);
            } else if (pickup instanceof StrengthPickup) {
                setX(getX() + 4);
            }
            pickup.setVisible(true);
        }
    }


    public int getPoints() {
        return points;
    }


    public void moveToPlayer() {
        target(player.getX(), player.getY());
    }
}
