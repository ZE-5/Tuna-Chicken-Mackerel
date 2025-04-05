import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Vector;

public class LevelManager {
    private GamePanel gamePanel;
    private Vector<GameEntity> gameEntities;
    private Player player;
    private boolean moveScreenPosition;
    private int screenX, screenY;
    private CollisionManager collisionManager;
    // private int offsetX, offsetY, offsetDx, offsetDy;
    private int[] mapBoundaries; //left corner (x, y) to right corner (x, y) | Imagine a rectangle, inside of which is the playable area
    

    public LevelManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        //TODO: Implement choices for character selection
        player = new ExamplePlayer(500, 500);

        gameEntities = new Vector<GameEntity>();

        collisionManager = new CollisionManager(player, gameEntities);
        // this.offsetDx = player.getDx();
        // this.offsetDy = player.getDy();

        
        // moveScreenPosition = false;
        moveScreenPosition = false;


        //TODO: move into level-maker class or method
        mapBoundaries = new int[]{0, 0, (int) (1920 * 1.5f), (int) (1080 * 1.5f)};
        gameEntities.add(new HealthPickup(70, 70));
        gameEntities.add(new StrengthPickup(100, 100));
        // gameEntities.add(new ExampleEnemy(player, 5000, 5000, 0));
        gameEntities.add(new ExampleEnemy(player, 500, 500, 100));
        // gameEntities.add(new Wall(200, 100, 50, 50));
        gameEntities.add(new Wall(400, 100, 50, 50));
        gameEntities.add(new ExampleEnemy(player, 200, 100, 10000));

        gameEntities.add(new PlayerProjectile(0, 0, 400, 100, 0, 10, 10, 10, 1, 400));

        for (int i = 0; i < 11; i++)
            gameEntities.add(new PlayerProjectile(0, 0, 200, 100, 6, 10, 10, 10, 1, 400));
    }


    public void update(boolean[] keys) {
        if (player.isDead()){
            // gamePanel.startGame(); //Uncomment this for a good time ;)
            return;
        }
        handlePlayerInput(keys);
        player.update(); //use this to update the player for things that the user does not directly control, such as increasing time for drawing a cape blowing
        
        collisionManager.checkCollisions(keys);

        // for (int i = 0; i < gameEntities.size(); i++) {
        // for (GameEntity entity : gameEntities) {
        Iterator<GameEntity> iterator = gameEntities.iterator();
        while (iterator.hasNext()) {
            GameEntity entity = iterator.next();

            if (!entity.isVisible())
                continue;

            if (entity instanceof Enemy && ((Enemy) entity).isDead())
                iterator.remove();
            
            else if (entity instanceof Projectile && ((Projectile) entity).timedOut())
                iterator.remove();
            
            else
                entity.update();
        }
    }


    public void draw(Graphics2D buffer) {
        player.draw(buffer);
        for (int i = 0; i < gameEntities.size(); i++) {
            if (!gameEntities.get(i).isVisible())
                continue;
            gameEntities.get(i).draw(buffer);
        }
    }


    private void handlePlayerInput(boolean[] keys) {
        
        if (keys[0])
        {
            player.move("UP");
            if (player.getY() < mapBoundaries[1])
                player.setY(mapBoundaries[1]);
        }

        if (keys[1])
        {
            player.move("RIGHT");
            if (player.getX() >= mapBoundaries[2])
                player.setX(mapBoundaries[2] - player.getWidth());
        }

        if (keys[2])
        {
            player.move("LEFT");
            if (player.getX() < mapBoundaries[0])
                player.setX(mapBoundaries[0]);
        }
        
        if (keys[3])
        {
            player.move("DOWN");
            if (player.getY() >= mapBoundaries[3])
                player.setY(mapBoundaries[3] - player.getHeight());
        }

        //Player is attacking if space is pressed
        if (keys[4]) 
            player.attack();
        else if (player.isAttacking() && !keys[4])
            player.stopAttack();
    }


    public int[] getPlayerPosition() {
        int x = player.getX();
        int y = player.getY();
        return new int[]{x, y};
    }


    public boolean holdScreenPositionX() {

        if (moveScreenPosition)
            return false;

        if (player.getX() + player.getWidth()/2 < mapBoundaries[0] + gamePanel.getWidth()/2 || player.getX() > mapBoundaries[2] - gamePanel.getWidth()/2)
            return true;
        
        return false;        
    }


    public boolean holdScreenPositionY() {

        if (moveScreenPosition)
            return false;
        
        if (player.getY() + player.getHeight()/2 < mapBoundaries[1] + gamePanel.getHeight()/2 || player.getY() > mapBoundaries[3] - gamePanel.getHeight()/2)
            return true;
        
        return false;        
    }


    public int getBufferImageX()
    {
        if (moveScreenPosition){
            if (screenX != gamePanel.getX()){
                int dx = 10;
                if (Math.abs(gamePanel.getX() - screenX) < dx)
                    return screenX;
                return screenX > gamePanel.getX() ? gamePanel.getX() + dx : gamePanel.getX() - dx;
            }
            return gamePanel.getX();
        }
        return -1 * (player.getX() + player.getWidth()/2 - gamePanel.getWidth()/2);
    }


    public int getBufferImageY()
    {
        if (moveScreenPosition){
            if (screenY != gamePanel.getY()){
                int dy = 10;
                if (Math.abs(gamePanel.getY() - screenY) < dy)
                    return screenY;
                return screenY > gamePanel.getY() ? gamePanel.getY() + dy : gamePanel.getY() - dy;
            }
            return gamePanel.getY();
        }
        return -1 * (player.getY() + player.getHeight()/2 - gamePanel.getHeight()/2);
    }


    public void moveScreen(int x, int y) {
        moveScreenPosition = true;

        //Constraints the right corner associated edges have not been tested.
        // if (x < mapBoundaries[0] + gamePanel.getWidth()/2)
        //     x = mapBoundaries[0] + gamePanel.getWidth()/2;
        // else if (x > mapBoundaries[2] - gamePanel.getWidth()/2)
        //     x = mapBoundaries[2] - gamePanel.getWidth() * 3/2;
        
        // if (y < mapBoundaries[1] + gamePanel.getHeight()/2)
        //     y = mapBoundaries[1] + gamePanel.getHeight()/2;
        // else if (y > mapBoundaries[3] - gamePanel.getHeight()/2)
        //     y = mapBoundaries[3] - gamePanel.getHeight()* 3/2;

        screenX = -1*(x - gamePanel.getWidth()/2);
        screenY = -1*(y - gamePanel.getHeight()/2);
    }


    public void releaseScreen()
    {
        moveScreenPosition = false;

        int x, y;
        if (player.getX() + player.getWidth()/2 < mapBoundaries[0] + gamePanel.getWidth()/2)
            x = mapBoundaries[0] + gamePanel.getWidth()/2;
        else if (player.getX() > mapBoundaries[2] - gamePanel.getWidth()/2)
            x = mapBoundaries[2] - gamePanel.getWidth()/2;
        else
            x = player.getX() + player.getWidth()/2;
        
        if (player.getY() + player.getHeight()/2 < mapBoundaries[1] + gamePanel.getHeight()/2)
            y = mapBoundaries[1] + gamePanel.getHeight()/2;
        else if (player.getY() > mapBoundaries[3] - gamePanel.getHeight()/2)
            y = mapBoundaries[3] - gamePanel.getHeight()/2;
        else
            y = player.getY() + player.getHeight()/2;


        gamePanel.setX(-1*(x - gamePanel.getWidth()/2));
        gamePanel.setY(-1*(y - gamePanel.getHeight()/2));
    }
}
