public abstract class Character extends GameEntity {
    protected int dx, dy, health, damage, time;
    // protected Vector<> attackString //TODO: implement attackString


    Character(int x, int y, int width, int height, int health, int damage, int dx, int dy) { //declare these in subclasses
        super(x, y, width, height);
        this.health = health;
        this.damage = damage;
        this.dx = dx;
        this.dy = dy;
    }


    //TODO:Changes from UML:
    // NO SET HEALTH
    //moved inRange to Enemy and it will be abstract in Enemy


    public int getHealth() {
        return health;
    }


    public boolean gotHit(int damage) { //return true if dead
        health -= damage;
        return health <= 0;
    }


    public boolean isDead() {
        return health <= 0;
    }


    public int getDamage() {
        return damage;
    }


    public void move(String direction) {
        switch (direction) {
            case "UP":
                dy = -1;
                break;

            case "RIGHT":
                dx = 1;
                break;

            case "LEFT":
                dy = 1;
                break;

            case "DOWN":
                dx = -1;
                break;

            default:
                System.out.println(direction + " is an invalid direction");
        }
    }


    public abstract void update();
}
