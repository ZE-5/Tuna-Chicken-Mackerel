import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public abstract class Character extends GameEntity {
    protected boolean isAttacking;
    protected ImageFX damageFX;
    protected ImageFXMod damageFXMod;
    protected int dx, dy, health, maxHealth, damage;
    protected boolean movedUp, movedDown, movedLeft, movedRight;
    // protected Vector<> attackString //TODO: implement attackString


    Character(int x, int y, int width, int height, int health, int damage, int dx, int dy) { //declare these in subclasses
        super(x, y, width, height);
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.dx = dx;
        this.dy = dy;
        this.isAttacking = false;
        damageFX = new DisintegrateFX();
        damageFXMod = new FlashFXMod(damageFX, DisintegrateFX.AMOUNT, 5);
        damageFX.addMod(damageFXMod);
        this.movedUp = false;
        this.movedDown = false;
        this.movedLeft = false;
        this.movedRight = false;
    }

    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (levelManager != null && levelManager.drawDebug()) {
            g2.setColor(Color.WHITE);
            g2.draw(getBoundingBox());
    
            g2.setColor(Color.RED);
            g2.draw(generateAttackBoundingBox());
        } else {
            levelManager = LevelManager.getInstance();
        }
    }

    public int getHealth() {
        return health;
    }


    public int getMaxHealth() {
        return maxHealth;
    }


    public void resetHealth() {
        health = maxHealth;
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

    protected boolean target(int x, int y) {
        if (this.x + dx >= x && this.x - dx <= x && this.y + dy >= y && this.y - dy <= y)
            return true;
        if (this.x + dx < x) {
            move("RIGHT");
        }
        if (this.x - dx > x) {
            move("LEFT");
        }
        if (this.y + dy < y) {
            move("DOWN");
        }
        if (this.y - dy > y) {
            move("UP");
        }
        return false;
    }

    protected boolean targetX(int x) {
        return target(x, y);
    }

    protected boolean targetY(int y) {
        return target(x, y);
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


    private void drawHoveringHealthBar(Graphics2D buffer, int xOffset, int yOffset, int width, int height) {
        // Draw character's health above them
        Rectangle2D boundingBox = getBoundingBox();

        int healthX = (int) (boundingBox.getX() + (boundingBox.getWidth() - 30) / 2 + xOffset);
        int healthY = (int) (boundingBox.getY() - 10 - 10 + yOffset);
        buffer.setColor(Color.BLACK);
        buffer.fillRect(healthX, healthY, width, height);
        buffer.setColor(Color.RED);
        buffer.fillRect(healthX, healthY, (int) (width * (getHealth() / (float) getMaxHealth())), height);
    }


    //Override in subclasses to adjust the health bar position and size
    public void drawHoveringHealthBar(Graphics2D buffer) {
        drawHoveringHealthBar(buffer, 0, 0, 30, 10); // default width and height
    }
}
