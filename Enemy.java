public abstract class Enemy extends Character {
    protected Player player;
    protected int points;
    protected Pickup pickup;

    public Enemy(Player player, int x, int y, int width, int height, int health, int damage, int dx, int dy, int points) {
        super(x, y, width, height, 1, damage, dx, dy);
        this.player = player;
        this.points = points;
        setVisible(false);

        if (Math.random() < 0.1)
        {
            pickup = new HealthPickup(x, y);
            pickup.setVisible(false);
        }
        else
            pickup = null;
    }


    public void dropPickup() {
        if (pickup != null) {
            pickup.setX(getX());
            pickup.setY(getY());
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
