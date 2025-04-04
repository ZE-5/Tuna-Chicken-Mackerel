public abstract class Enemy extends Character {
    protected Player player;
    protected int points;

    public Enemy(Player player, int x, int y, int width, int height, int health, int damage, int dx, int dy, int points) {
        super(x, y, width, height, health, damage, dx, dy);
        this.player = player;
        this.points = points;
    }


    // public void update() {
    //     //TODO: implement update method
    //     // if (!inRange())
    //     // {
    //     //     moveToPlayer();
    //     // }
    // }


    public int getPoints() {
        return points;
    }

    // public boolean inRange() {
    //     //TODO: implement inRange method
    //     return (player.getBoundingBox().intersects(getRangeBoundingBox()));
    // }


    public void moveToPlayer() {
        //TODO: implement moveToPlayer method

        if (getX() < player.getX())
            move("RIGHT");
        else if (getX() > player.getX())
            move("LEFT");

        if (getY() < player.getY())
            move("DOWN");
        else if (getY() > player.getY())
            move("UP");
    }
}
