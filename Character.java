import java.awt.geom.Rectangle2D;

public abstract class Character extends GameEntity {
    protected int dx, dy, health, maxHealth, damage;
    protected boolean isAttacking, movedUp, movedDown, movedLeft, movedRight;
    // protected Vector<> attackString //TODO: implement attackString


    Character(int x, int y, int width, int height, int health, int damage, int dx, int dy) { //declare these in subclasses
        super(x, y, width, height);
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.dx = dx;
        this.dy = dy;
        this.isAttacking = false;
        this.movedUp = false;
        this.movedDown = false;
        this.movedLeft = false;
        this.movedRight = false;
    }

    
    public int getHealth() {
        return health;
    }


    public int getMaxHealth() {
        return maxHealth;
    }


    public void attack(){
        isAttacking = true;
    }


    public void stopAttack(){
        isAttacking = false;
    }


    public boolean isAttacking(){
        return isAttacking;
    }


    public void damaged(int damage) { //return true if dead
        health -= damage;
    }

    public boolean isDead() {
        return health <= 0;
    }


    public int getDamage() {
        return damage;
    }


    public int getDx() {
        return dx;
    }


    public int getDy() {
        return dy;
    }


    public void resetMovement() {
        movedUp = false;
        movedDown = false;
        movedLeft = false;
        movedRight = false;
    }


    public boolean movedUp() {
        return movedUp;
    }


    public boolean movedDown() {
        return movedDown;
    }


    public boolean movedLeft() {
        return movedLeft;
    }


    public boolean movedRight() {
        return movedRight;
    }


    public void move(String direction) {
        switch (direction) {
            case "UP":
                movedUp = true;
                y -= dy;
                break;

            case "RIGHT":
                if (isFacingRight){
                    movedRight = true;
                    x += dx;
                }
                else
                    isFacingRight = true;
                break;

            case "LEFT":
                if (!isFacingRight){
                    x -= dx;
                    movedLeft = true;
                }
                else
                    isFacingRight = false;
                break;

            case "DOWN":{
                y += dy;
                movedDown = true;
            }
                break;

            default:
                System.out.println(direction + " is an invalid direction");
        }
    }


    public abstract void update();


    public Rectangle2D getAttackBoundingBox()
    {
        if (!isAttacking)
            return null;
        else
            return generateAttackBoundingBox();
    }


    protected abstract Rectangle2D generateAttackBoundingBox();
}
