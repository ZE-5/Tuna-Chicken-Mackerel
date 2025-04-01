import java.awt.Graphics2D;
import java.util.Vector;

public class LevelManager {
    private GamePanel gamePanel;
    private Vector<GameEntity> gameEntities;
    private Character player;
    

    public LevelManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
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
        {
            player.move("UP");
            if (player.getY() < 0)
                player.setY(0);
        }

        if (keys[1])
        {
            player.move("RIGHT");
            if (player.getX() >= gamePanel.getWidth())
                player.setX(gamePanel.getWidth() - player.getWidth());
        }

        if (keys[2])
        {
            player.move("LEFT");
            if (player.getX() < 0)
                player.setX(0);
        }
        
        if (keys[3])
        {
            player.move("DOWN");
            if (player.getY() >= gamePanel.getHeight())
                player.setY(gamePanel.getHeight() - player.getHeight());
        }

        //TODO:Implement attacking
        // if (keys[4])
        //     levelManager.playerAttack();
    }
}
