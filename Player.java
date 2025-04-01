public abstract class Player extends Character{
    private boolean isBonusDamageActive;
    private int damageBonusTimer;
    private boolean isAttacking;

    private static final int bonusDamageMultiplier = 1+1/3, bonusDamageTime = 15, healAmount = 20;

    Player(int x, int y, int width, int height, int health, int damage, int dx, int dy) {
        super(x, y, width, height, health, damage, dx, dy);
        isBonusDamageActive = false;
        damageBonusTimer = -1;
    }


    public void heal() {
        health += healAmount;
        // System.out.println("Healed");
    }


    public void applyBonusDamage() {
        if(isBonusDamageActive)
            damageBonusTimer = 0;
        else
        {
            isBonusDamageActive = true;
            damageBonusTimer = 0;
            damage *= bonusDamageMultiplier;
            // System.out.println("Bonus damage applied");
        }
    }


    public void removeBonusDamage() {
        isBonusDamageActive = false;
        damage /= bonusDamageMultiplier;
        damageBonusTimer = -1;
        // System.out.println("Bonus damage removed");
    }


    public void update() {
        if(damageBonusTimer == bonusDamageTime)
            removeBonusDamage();
        else if (damageBonusTimer != -1)
            damageBonusTimer++;
    }


    public boolean isAttacking() {
        return isAttacking;
    }


    public void setAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }
}
