public abstract class Player extends Character{
    private boolean isBonusDamageActive;
    private int damageBonusTimer;

    private static final int bonusDamageMultiplier = 1+1/3, bonusDamageTime = 15, healAmount = 20;

    Player(int x, int y, int width, int height, int health, int damage, int dx, int dy) {
        super(x, y, width, height, health, damage, dx, dy);
        isBonusDamageActive = false;
    }


    public void heal() {
        health += healAmount;
    }


    public void applyBonusDamage() {
        if(isBonusDamageActive)
            damageBonusTimer = 0;
        else
        {
            isBonusDamageActive = true;
            damageBonusTimer = 0;
            damage *= bonusDamageMultiplier;
        }
    }


    public void removeBonusDamage() {
        isBonusDamageActive = false;
        damage /= bonusDamageMultiplier;
        damageBonusTimer = 0;
    }


    public void update() {
        if(damageBonusTimer == bonusDamageTime)
            removeBonusDamage();
        else
            damageBonusTimer++;
    }
}
