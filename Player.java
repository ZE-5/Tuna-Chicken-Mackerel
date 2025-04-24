public abstract class Player extends Character{
    private boolean isBonusDamageActive;
    private int damageBonusTimer;
    private boolean released;
    private int t;
    protected int COOLDOWN, atkCount, numAtk;
    private static final int bonusDamageMultiplier = 1+1/3, bonusDamageTime = 15, healAmount = 20;

    Player(int x, int y, int width, int height, int health, int damage, int dx, int dy) {
        super(x, y, width, height, health, damage, dx, dy);
        isBonusDamageActive = false;
        damageBonusTimer = -1;
        t = -1;
        released = true;
        atkCount = 0;
        numAtk = 1;
    }


    protected void register() {
        LevelManager.getInstance().registerPlayer(this);
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
        stopAttack();
        if (t >= 0) {
            t++;
            if (t == COOLDOWN)
                t = -1;
        }
    }

    public void release() {
        released = true;
    }

    public void attack() {
        if (t < 0 && released) {
            t = 0;
            isAttacking = true;
        }
        released = false;
        atkCount = ++atkCount % numAtk;
    }
}
