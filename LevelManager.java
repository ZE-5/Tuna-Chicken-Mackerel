import java.awt.Graphics2D;
import java.util.Vector;

public class LevelManager {
    Vector<GameEntity> gameEntities;

    public LevelManager() {
        gameEntities = new Vector<GameEntity>();
    }


    public void update() {
        for (int i = 0; i < gameEntities.size(); i++) {
            gameEntities.get(i).update();
        }
    }


    public void draw(Graphics2D buffer) {
        for (int i = 0; i < gameEntities.size(); i++) {
            gameEntities.get(i).draw(buffer);
        }
    }
}
