public class StrengthPickup extends Pickup{
    private Animation strengthAnim;

    StrengthPickup(int x, int y) {
        super(x, y, 35, 35);
        strengthAnim = new Animation(this, "images/strength.gif", 1, 10, 60, true);
        drawable = strengthAnim;
        strengthAnim.rowAnim("DEFAULT", 0);
        strengthAnim.setState("DEFAULT");
    }


    public void update() {}
}
