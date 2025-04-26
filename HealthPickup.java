public class HealthPickup extends Pickup{
    private Animation healthAnim;
    HealthPickup(int x, int y) {
        super(x, y, 35, 35);
        healthAnim = new Animation(this, "images/health.gif", 1, 10, 60, true);
        drawable = healthAnim;
        healthAnim.rowAnim("DEFAULT", 0);
        healthAnim.setState("DEFAULT");
    }


    public void update() {}
}
