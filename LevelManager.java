import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Vector;

public class LevelManager {
    private static LevelManager instance = null;
    private GamePanel gamePanel;
    private Vector<GameEntity> gameEntities;
    private Player player;
    private boolean moveScreenPosition;
    private int screenX, screenY;
    private CollisionManager collisionManager;
    private Sound levelSound;
    private int healthPosX, healthPosY;
    private int respawnPosX, respawnPosY;
    private int level;
    private boolean changeLevel;
    // private int offsetX, offsetY, offsetDx, offsetDy;
    private int[] mapBoundaries; //left corner (x, y) to right corner (x, y) | Imagine a rectangle, inside of which is the playable area
    

    private LevelManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        moveScreenPosition = false;
        changeLevel = false;
        
        mapBoundaries = new int[]{0, 0, (int) (1920 * 1.5f), (int) (1080 * 1.5f)};
        
        gameEntities = new Vector<GameEntity>();
        setPlayerCharacter(gamePanel.getGameWindow().selectCharacter(false));
                
        collisionManager = new CollisionManager(player, gameEntities);
    }


    public void initialize() {
        level = 0;
        setLevel(level);        
    }


    public static LevelManager getInstance(GamePanel gp) {
        if (instance == null) {
            instance = new LevelManager(gp);
        }
        return instance;
    }

    public static LevelManager getInstance() {
        return instance;
    }

    public void register(GameEntity ge) { //each game entity calls this function to add itself to the gameEntity vector
        if (!gameEntities.contains(ge)) {
            gameEntities.add(ge);
        }
    }


    public void registerPlayer(Player player) { //called by player
        this.player = player;
    }


    private void setRespawnPosition(int x, int y) {
        this.respawnPosX = x;
        this.respawnPosY = y;
    }


    private void respawn() {
        player.resetHealth();
        player.setLocation(respawnPosX, respawnPosY);
    }


    public void setPlayerCharacter(int playerCharacter) {
        switch (playerCharacter) {
            case 0:
                player = new KnifePlayer(0, 0);
                break;
            case 1:
                player = new PoolPlayer(0, 0);
                break;
            case 2:
                player = new TheGun(0, 0);
                break;
            default:
                player = new KnifePlayer(0, 0);
        }
    }

    public void exampleLevel() { //level 0
        gamePanel.setScreenPosition(0, 0);
        setRespawnPosition(0, 0);
        player.setLocation(0, 0);
        levelSound = new Sound("sounds/test 2.wav", true, 0.8f);

        new HealthPickup(70, 70);
        new StrengthPickup(100, 100);
        // new ExampleEnemy(player, 5000, 5000, 0);
        new ExampleEnemy(player, 500, 500, 100);
        new Trigger(100, 1000, 50, 50, "LEVEL", 1, true);
        // new Wall(200, 100, 50, 50);
        new Wall(400, 100, 50, 50);
        new ExampleEnemy(player, 200, 100, 10000);

        new Wall(200, 200, 700, 25);


        new Grunt(player, 400, 400);        
        new Assassin(player, 300, 300);
        new TheDon(player, 800, 400);
        new Henchman(player, 600, 600);

        new EnemyProjectile(0, 0, 400, 0, 10, 10, 10, 1, 400);

        new Treadmill(700, 700, 200, 50, player.getDx()/2, "RIGHT");

        for (int i = 0; i < 11; i++)
            new PlayerProjectile(0, 0, 200, 100, 10, 10, 10, 1, 400);

        levelSound.play();
    }


    public void update(boolean[] keys) {
        if (changeLevel) {
            setLevel(level);
            changeLevel = false;
        }


        if (player.isDead()){
            // gamePanel.startGame(); //Uncomment this for a good time ;)
            levelSound.stop();
            respawn();
        }

        handlePlayerMovementInputs(keys);

        collisionManager.checkCollisions(keys);

        player.update(); //use this to update the player for things that the user does not directly control, such as increasing time for drawing a cape blowing
        handlePlayerAttackingInputs(keys);        
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
        for (int i = 0; i < gameEntities.size(); i++) {
            if (!gameEntities.get(i).getDrawBehindPlayer() || !gameEntities.get(i).isVisible())
                continue;
            gameEntities.get(i).draw(buffer);
        }

        player.draw(buffer);

        for (int i = 0; i < gameEntities.size(); i++) {
            if (!gameEntities.get(i).isVisible() ||gameEntities.get(i).getDrawBehindPlayer())
                continue;
            gameEntities.get(i).draw(buffer);
        }

        //Draw player's health above them
        int healthX = player.getX() + (player.getWidth() - 30) / 2;
        int healthY = player.getY() - 10 - 10;
        buffer.setColor(Color.BLACK);
        buffer.fillRect(healthX, healthY, 30, 10);
        buffer.setColor(Color.RED);
        buffer.fillRect(healthX, healthY, (int) (30 * (player.getHealth() / (float) player.getMaxHealth())), 10);
    }


    private void handlePlayerMovementInputs(boolean[] keys) {
        player.setMoving(keys[0] || keys[1] || keys[2] || keys[3]); //Check if player is trying to move
        
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
    }


    public void handlePlayerAttackingInputs(boolean[] keys) {
        //Player is attacking if space is pressed
        if (keys[4]) 
            player.attack();
        else if (player.isAttacking() && !keys[4])
            player.stopAttack();
        
        if (!keys[4])
            player.release();
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
  
  
    public void eventTrigger(String triggerType, int triggerValue) {
        if (triggerType.equals("LEVEL")) {
            level = triggerValue;
            changeLevel = true;
        }
        else if (triggerType.equals("SPAWN")) {

        }
    }


    public void strike() { //reference to strike in theatre
        if (gameEntities == null)
            gameEntities = new Vector<GameEntity>();

        Vector<GameEntity> temp = new Vector<GameEntity>();

        for (GameEntity ge : gameEntities) {
            if (ge instanceof PlayerProjectile) {
                ((PlayerProjectile) ge).reset();
                temp.add(ge);
            }
        }

        gameEntities.clear();
        gameEntities.addAll(temp);
    }


    public void setLevel(int level) {
        strike();
        switch (level) {
            case 0:
                exampleLevel();
                break;

            case 1:
                exampleLevel();
                break;
        
            default:
                System.out.println("Level not found!");
                break;
        }
    }
}
