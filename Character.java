import java.awt.geom.Rectangle2D;

public abstract class Character extends GameEntity {
    protected int dx, dy, health, damage;
    protected boolean isAttacking;
    protected ImageFX damageFX;
    protected ImageFXMod damageFXMod;
    // protected Vector<> attackString //TODO: implement attackString


    Character(int x, int y, int width, int height, int health, int damage, int dx, int dy) { //declare these in subclasses
        super(x, y, width, height);
        this.health = health;
        this.damage = damage;
        this.dx = dx;
        this.dy = dy;
        this.isAttacking = false;
        damageFX = new DisintegrateFX();
        damageFXMod = new FlashFXMod(damageFX, DisintegrateFX.AMOUNT, 5);
        damageFX.addMod(damageFXMod);
    }

    
    public int getHealth() {
        return health;
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
        damageFXMod.reset();
        damageFX.setActive(true);
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


    public void move(String direction) {
        switch (direction) {
            case "UP":
                y -= dy;
                break;

            case "RIGHT":
                if (isFacingRight)
                    x += dx;
                else
                    isFacingRight = true;
                break;

            case "LEFT":
                if (!isFacingRight)
                    x -= dx;
                else
                    isFacingRight = false;
                break;

            case "DOWN":
                y += dy;
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
