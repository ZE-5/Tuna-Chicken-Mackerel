import java.awt.Color;
import java.awt.Graphics2D;

public class Trigger extends GameEntity {
    private String triggerType;
    private int triggerValue;
    private boolean isActive;
    private boolean show;

    public Trigger(int x, int y, int width, int height, String triggerType, int triggerValue) {
        super(x, y, width, height);
        this.triggerType = triggerType.toUpperCase();
        this.triggerValue = triggerValue;
        isActive = true;
        drawBehindPlayer = true;
        isVisible = true;
        show = false;
    }


    public Trigger(int x, int y, int width, int height, String triggerType, int triggerValue, boolean show)  {
        this(x, y, width, height, triggerType, triggerValue);
        this.show = show;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType.toUpperCase();
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerValue(int triggerValue) {
        this.triggerValue = triggerValue;
    }

    public int getTriggerValue() {
        return triggerValue;
    }

    public void update() {}

    public void draw(Graphics2D g2) {
        if (!show)
            return;
        g2.setColor(Color.RED);
        g2.drawRect(x, y, width, height);
    }


    public void setVisible(boolean isVisible) {
        this.isVisible = true;
    }


    public void setActive(boolean active) {
        isActive = active;
    }


    public boolean isActive() {
        return isActive;
    }
}
