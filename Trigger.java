import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Trigger extends GameEntity {
    private String triggerType;
    private int triggerValue;
    private boolean isActive;
    private boolean show;
    private static Map<String, Vector<Integer>> triggers = new HashMap<>();

    public Trigger(int x, int y, int width, int height, String triggerType, int triggerValue) {
        super(x, y, width, height);
        
        if (!triggers.containsKey(triggerType)) {
            triggers.put(triggerType, new Vector<>());
            triggers.get(triggerType).add(triggerValue);
        }
        else if (triggers.get(triggerType).contains(triggerValue)) {
                String message = "Trigger value already exists for this trigger type. Trigger type: " + triggerType + ", Trigger value: " + triggerValue;
                throw new IllegalArgumentException(message);
            }
        else
            triggers.get(triggerType).add(triggerValue);
        

        
        this.triggerType = triggerType.toUpperCase();
        this.triggerValue = triggerValue;
        isActive = true;
        drawBehindPlayer = true;
        isVisible = true;
        show = LevelManager.getInstance().drawDebug();
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
