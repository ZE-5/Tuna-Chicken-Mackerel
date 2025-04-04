import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Vector;

public class LevelManager {
    private GamePanel gamePanel;
    private Vector<GameEntity> gameEntities;
    private Player player;
    private CollisionManager collisionManager;
    

    public LevelManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gameEntities = new Vector<GameEntity>();
        player = new ExamplePlayer(50, 50);
        collisionManager = new CollisionManager(gameEntities, player);

        //TODO: move into level-maker class or method
        gameEntities.add(new HealthPickup(70, 70));
        gameEntities.add(new StrengthPickup(100, 100));
        // gameEntities.add(new ExampleEnemy(player, 5000, 5000, 0));
        gameEntities.add(new ExampleEnemy(player, 500, 500, 100));
        gameEntities.add(new Wall(200, 100, 50, 50));
    }


    public void update(boolean[] keys) {
        if (player.isDead()){
            // gamePanel.startGame(); //Uncomment this for a good time ;)
            return;
        }
        handlePlayerInput(keys);
        player.update(); //use this to update the player for things that the user does not directly control, such as increasing time for drawing a cape blowing
        
        //TODO collisoin manager
        collisionManager.checkCollisions();

        // for (int i = 0; i < gameEntities.size(); i++) {
        // for (GameEntity entity : gameEntities) {
        Iterator<GameEntity> iterator = gameEntities.iterator();
        while (iterator.hasNext()) {
            GameEntity entity = iterator.next();
            if (entity instanceof Enemy && ((Enemy) entity).isDead())
                iterator.remove();
            else
                entity.update();
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

        //Player is attacking if space is pressed
        if (keys[4]) 
            player.attack();
        else if (player.isAttacking() && !keys[4])
            player.stopAttack();
    }
}
