import java.awt.Graphics2D;
import java.util.Vector;

public class LevelManager {
    Vector<GameEntity> gameEntities;
    Character player;
    

    public LevelManager() {
        gameEntities = new Vector<GameEntity>();
        player = new ExampleCharacter(50, 50);
    }


    public void update(boolean[] keys) {
        handlePlayerInput(keys);
        player.update(); //use this to update the player for things that the user does not directly control, such as increasing time for drawing a cape blowing
        
        for (int i = 0; i < gameEntities.size(); i++) {
            gameEntities.get(i).update();
        }
    }


    public void draw(Graphics2D buffer) {
        player.draw(buffer);
        for (int i = 0; i < gameEntities.size(); i++) {
            gameEntities.get(i).draw(buffer);
        }
    }


    private void handlePlayerInput(boolean[] keys) {
        
        if (keys[0])
            player.move("UP");
        if (keys[1])
            player.move("RIGHT");
        if (keys[2])
            player.move("LEFT");
        if (keys[3])
            player.move("DOWN");

        //TODO:Implement attacking
        // if (keys[4])
        //     levelManager.playerAttack();
    }
}
