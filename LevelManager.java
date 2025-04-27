import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class LevelManager {
    private static LevelManager instance = null;
    private GamePanel gamePanel;
    private Vector<GameEntity> gameEntities;
    private Map<String, Map<Integer, Vector<GameEntity>>> triggerEntitiesMap;
    private Player player;
    private boolean moveScreenPosition;
    private int screenX, screenY;
    private CollisionManager collisionManager;
    private Sound levelSound;
    private int respawnPosX, respawnPosY;
    private int level;
    private boolean changeLevel;
    private int[] mapBoundaries; //left corner (x, y) to right corner (x, y) | Imagine a rectangle, inside of which is the playable area
    private boolean showBossHealthBar;
    
    private boolean drawDebug;
    private boolean defeatedTheDon, gameCompleted;
    private boolean showHidden;
    private int winScreenTimer;

    private LevelManager(GamePanel gamePanel) {
        winScreenTimer = 0;
        defeatedTheDon = false;
        gameCompleted = false;
        this.gamePanel = gamePanel;
        moveScreenPosition = false;
        changeLevel = false;
        showBossHealthBar = false;
        mapBoundaries = new int[4];

        triggerEntitiesMap = new HashMap<>();
        gameEntities = new Vector<GameEntity>();


        drawDebug = false;
    }


    public void initialize() {
        level = 1;

        String fileContents = "null";
        try{
            File file = new File("SavedData.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                fileContents = scanner.nextLine();
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {}
        showHidden = fileContents.equals("HIDDEN");

        setPlayerCharacter(gamePanel.getGameWindow().selectCharacter(showHidden));
        collisionManager = new CollisionManager(player, gameEntities);
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


    public int[] getMapBoundaries() {
        return mapBoundaries;
    }


    public void setMapBoundaries(int x1, int y1, int x2, int y2) {
        mapBoundaries[0] = x1;
        mapBoundaries[1] = y1;
        mapBoundaries[2] = x2;
        mapBoundaries[3] = y2;
    }


    private void setRespawnPosition(int x, int y) {
        this.respawnPosX = x;
        this.respawnPosY = y;
    }


    private void respawn() {
        player.resetHealth();
        player.setLocation(respawnPosX, respawnPosY);
        screenFix();
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


    public void setPlayerStartingPosition(int x, int y) {
        if (x < mapBoundaries[0])
            x = mapBoundaries[0];
        else if (x > mapBoundaries[2] - player.getBoundingBox().getWidth())
            x = (int) (mapBoundaries[2] - player.getBoundingBox().getWidth());

        if (y < mapBoundaries[1])
            y = mapBoundaries[1];
        else if (y > mapBoundaries[3] - player.getBoundingBox().getHeight())
            y = (int) (mapBoundaries[3] - player.getBoundingBox().getHeight());

        setRespawnPosition(x, y);
        player.setLocation(x, y);
        screenFix();
    }


    public void update(boolean[] keys) {
        if (changeLevel) {
            setLevel(level);
            changeLevel = false;
        }


        if (player.isDead()){
            playerDied();
        }

        handlePlayerMovementInputs(keys);

        collisionManager.checkCollisions(keys);

        player.update(); //use this to update the player for things that the user does not directly control, such as increasing time for drawing a cape blowing
        handlePlayerAttackingInputs(keys);
        
        boolean enemiesPresent = false;
        Iterator<GameEntity> iterator = gameEntities.iterator();
        while (iterator.hasNext()) {
            GameEntity entity = iterator.next();

            if (!entity.isVisible())
                continue;

            if (entity instanceof Enemy && ((Enemy) entity).isDead()) {
                ((Enemy) entity).dropPickup();

                if (entity instanceof TheDon) {
                    defeatedTheDon = true;
                }

                iterator.remove();
            }
            
            else if (entity instanceof Projectile && ((Projectile) entity).timedOut())
                iterator.remove();
            
            else{
                entity.update();
                
                if (entity instanceof Enemy){
                    restrictToMapBoundaries((entity));
                    enemiesPresent = true;
                }
            }
        }

        if (defeatedTheDon && !enemiesPresent){
            gameWon();
            defeatedTheDon = false;
            gameCompleted = true;
        }
    }


    public void gameWon() {
        try {
            File file = new File("SavedData.txt");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write("HIDDEN");
            writer.close();
        }
        catch (Exception e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    private void restrictToMapBoundaries(GameEntity entity) {
        if (entity.getX() < mapBoundaries[0])
            entity.setX(mapBoundaries[0]);
        else if (entity.getX() + entity.getBoundingBox().getWidth() > mapBoundaries[2])
            entity.setX((int) (mapBoundaries[2] - entity.getBoundingBox().getWidth()));

        if (entity.getY() < mapBoundaries[1])
            entity.setY(mapBoundaries[1]);
        else if (entity.getY() + entity.getBoundingBox().getHeight() > mapBoundaries[3])
            entity.setY((int) (mapBoundaries[3] - entity.getBoundingBox().getHeight()));
    }


    public boolean drawDebug() {
        return drawDebug;
    }

    public void setDrawDebug(boolean value) {
        drawDebug = value;
    }

    public void draw(Graphics2D buffer) {
        TheDon theDon = null;

        for (int i = 0; i < gameEntities.size(); i++) {
            if (!gameEntities.get(i).getDrawBehindPlayer() || !gameEntities.get(i).isVisible())
                continue;
            gameEntities.get(i).draw(buffer);

            if (gameEntities.get(i) instanceof TheDon)
                theDon = (TheDon) gameEntities.get(i);
            
                else if (gameEntities.get(i) instanceof Enemy)
                ((Enemy) gameEntities.get(i)).drawHoveringHealthBar(buffer);
        }

        player.draw(buffer);

        for (int i = 0; i < gameEntities.size(); i++) {
            if (!gameEntities.get(i).isVisible() ||gameEntities.get(i).getDrawBehindPlayer())
                continue;
            gameEntities.get(i).draw(buffer);

            if (gameEntities.get(i) instanceof TheDon)
            theDon = (TheDon) gameEntities.get(i);

            else if (gameEntities.get(i) instanceof Enemy)
                ((Enemy) gameEntities.get(i)).drawHoveringHealthBar(buffer);
        }
        
        drawPlayerHealthBar(buffer);

        if (theDon != null) 
            drawXX_Big_Man_Gang_Leader_Don_Honcho_Kingpin_the_OG_XxHealthBar(buffer, theDon);

        if (gameCompleted)
            drawWinScreen(buffer);

        //TODO: Remove
        if (drawDebug()) {
            buffer.setColor(Color.WHITE);
            buffer.drawString("X: " + (int) (player.getBoundingBox().getX()) + " Y: " + (int) (player.getBoundingBox().getY()), (int) player.getBoundingBox().getX(), (int) player.getBoundingBox().getY());
        }
    }


    private void drawPlayerHealthBar(Graphics2D buffer) {
        int healthX = -1 * gamePanel.getX() + 4; //+ offset
        int healthY = -1 * gamePanel.getY() + 2;
        int height = 20 ;
        int width = 300;
        buffer.setColor(Color.BLACK);
        buffer.fillRect(healthX, healthY, width, height);
        buffer.setColor(Color.RED);
        buffer.fillRect(healthX, healthY, (int) (width * (player.getHealth() / (float) player.getMaxHealth())), height);
    }


    public void drawWinScreen(Graphics2D buffer) {
        int height = 100;
        int width = 700;
        int healthX = -1 * gamePanel.getX() + gamePanel.getWidth()/2 - width/2; //+ offset
        int healthY = -1 * gamePanel.getY() + gamePanel.getHeight()/2 - height/2;
        buffer.setColor(Color.BLACK);
        buffer.fillRect(healthX, healthY, width, height);
        Font font = new Font("Times New Roman", Font.BOLD, 24);
        FontMetrics metrics = buffer.getFontMetrics(font);
        String string = "You won the game!";

        winScreenTimer++;
        int num = 60;
        if (winScreenTimer > num)
            string = "The Cougar Cats have been defeated!";

        if (!showHidden && winScreenTimer > num * 2)
            string = "Hmm... That's odd";

        if (!showHidden && winScreenTimer > num * 3)
            string = "Try restarting the game";

        int stringWidth = metrics.stringWidth(string);
        buffer.setFont(font);
        buffer.setColor(Color.GREEN);
        buffer.drawString(string, healthX + width/2 - stringWidth/2, healthY + height/2 + metrics.getAscent()/2);
    }

    private void drawXX_Big_Man_Gang_Leader_Don_Honcho_Kingpin_the_OG_XxHealthBar(Graphics2D buffer, TheDon theDon) {
        if (!showBossHealthBar)
            return;

        int height = 30;
        int width = 700;
        int healthX = -1 * gamePanel.getX() + gamePanel.getWidth()/2 - width/2; //+ offset
        int healthY = -1 * gamePanel.getY() + gamePanel.getHeight() - height - 30;
        buffer.setColor(Color.BLACK);
        buffer.fillRect(healthX, healthY, width, height);
        buffer.setColor(Color.GREEN);
        buffer.fillRect(healthX, healthY, (int) (width * (theDon.getHealth() / (float) theDon.getMaxHealth())), height);
        Font font = new Font("Times New Roman", Font.BOLD, 24);
        FontMetrics metrics = buffer.getFontMetrics(font);
        String donString = "xX_Big_Man_Gang_Leader_Don_Honcho_Kingpin_the_OG_Xx";
        int stringWidth = metrics.stringWidth(donString);
        buffer.setFont(font);
        buffer.setColor(Color.black);
        buffer.drawString(donString, healthX + (width - stringWidth)/2, healthY + metrics.getAscent());
        
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
                player.setX((int) (mapBoundaries[2] - player.getBoundingBox().getWidth()));
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
                player.setY((int) (mapBoundaries[3] - player.getBoundingBox().getHeight()));
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

        if (player.getX() + player.getBoundingBox().getWidth()/2 < mapBoundaries[0] + gamePanel.getWidth()/2 || player.getX() > mapBoundaries[2] - gamePanel.getWidth()/2)
            return true;
        
        return false;        
    }


    public boolean holdScreenPositionY() {

        if (moveScreenPosition)
            return false;
        
        if (player.getY() + player.getBoundingBox().getHeight()/2 < mapBoundaries[1] + gamePanel.getHeight()/2 || player.getY() > mapBoundaries[3] - gamePanel.getHeight()/2)
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
        return (int) (-1 * (player.getX() + player.getBoundingBox().getWidth()/2 - gamePanel.getWidth()/2));
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
        return (int) (-1 * (player.getY() + player.getBoundingBox().getHeight()/2 - gamePanel.getHeight()/2));
    }


    public void moveScreen(int x, int y) {
        moveScreenPosition = true;
        screenX = -1*(x - gamePanel.getWidth()/2);
        screenY = -1*(y - gamePanel.getHeight()/2);
    }


    public void releaseScreen()
    {
        moveScreenPosition = false;

        int x, y;
        if (player.getX() + player.getBoundingBox().getWidth()/2 < mapBoundaries[0] + gamePanel.getWidth()/2)
            x = mapBoundaries[0] + gamePanel.getWidth()/2;
        else if (player.getX() > mapBoundaries[2] - gamePanel.getWidth()/2)
            x = mapBoundaries[2] - gamePanel.getWidth()/2;
        else
            x = (int) (player.getX() + player.getBoundingBox().getWidth()/2);
        
        if (player.getY() + player.getBoundingBox().getHeight()/2 < mapBoundaries[1] + gamePanel.getHeight()/2)
            y = mapBoundaries[1] + gamePanel.getHeight()/2;
        else if (player.getY() > mapBoundaries[3] - gamePanel.getHeight()/2)
            y = mapBoundaries[3] - gamePanel.getHeight()/2;
        else
            y = (int) (player.getY() + player.getBoundingBox().getHeight()/2);


        gamePanel.setX(-1*(x - gamePanel.getWidth()/2));
        gamePanel.setY(-1*(y - gamePanel.getHeight()/2));
    }


    private void screenFix() {
        if (gamePanel.getX() < mapBoundaries[0])
            gamePanel.setX(mapBoundaries[0]);
        else if (gamePanel.getX() > mapBoundaries[2] - gamePanel.getWidth())
            gamePanel.setX(mapBoundaries[2] - gamePanel.getWidth());

        if (gamePanel.getY() < mapBoundaries[1])
            gamePanel.setY(mapBoundaries[1]);
        else if (gamePanel.getY() > mapBoundaries[3] - gamePanel.getHeight())
            gamePanel.setY(mapBoundaries[3] - gamePanel.getHeight());
    }


    public void strike() { //reference to strike in theatre
        if (gameEntities == null)
            gameEntities = new Vector<GameEntity>();

        if (levelSound != null) {
            levelSound.stop();
        }

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
                level1();
                break;
        
            case 2:
                level2();
                break;

            default:
                System.err.println("Level not found!");
                break;
        }
    }


    public void exampleLevel() { //level 0
        gamePanel.setBackground("images/level1back.gif", 5000, 5000);
        setMapBoundaries(0, 0, 5000, 5000);
        setPlayerStartingPosition(1000, 0);
        levelSound = new Sound("sounds/test 2.wav", true, 0.8f);

        new HealthPickup(70, 70);
        new StrengthPickup(100, 100);
        // new ExampleEnemy(player, 5000, 5000, 0);
        new ExampleEnemy(player, 500, 500, 100);
        new Trigger(100, 1000, 50, 50, "LEVEL", 2);
        // new Wall(200, 100, 50, 50);
        new Wall(400, 100, 50, 50);
        new ExampleEnemy(player, 200, 100, 10000);

        new Wall(200, 200, 700, 25);


        new Grunt(player, 400, 400);        
        new Assassin(player, 300, 300);
        new TheDon(player, 800, 400);
        new Henchman(player, 600, 600);

        new EnemyProjectile(0, 0, 400, 0, 10, 10, 10, 1, 400);

        new Treadmill(player, 700, 700, 200, 50);

        levelSound.play();
    }

    public void level1() {
        gamePanel.setBackground("images/Level1.png", 4500, 4500);
        setMapBoundaries(0, 0, 4500 - 50, 4500 - 100);
        setPlayerStartingPosition(30, 630);

        // setPlayerStartingPosition(2570, 3659);

        int defaultSize = 50;

        new Wall(0, 0, 3020, (int) (100 - player.getBoundingBox().getHeight())); //top wall
        new Wall(0, 1220, 1459, defaultSize); //top street bottom wall

        new Wall(3025, 0, defaultSize, (int) (3290 - player.getBoundingBox().getHeight())); //vertical street RIGHT wall
        new Wall(1465, 1220, -1 * defaultSize, 4040); //vertical street LEFT wall

        new Wall(1450, 4370, 3590 - 1450, defaultSize); //bottom street BOTTOM wall
        new Wall(3025, (int) (3290 - player.getBoundingBox().getHeight()), 3590 - 3020, -1 * defaultSize); //bottom street TOP wall


        //setting up TRIGGERS
        Trigger trigger;
        //Spawn triggers
        //Top street
        trigger = new Trigger(800, 0, defaultSize, 1220, "SPAWN", 1);
        addTriggerEntity(trigger, new Grunt(player, 970, 300));
        addTriggerEntity(trigger, new Grunt(player, 610, 310));
        addTriggerEntity(trigger, new Grunt(player, 700, 1000));
        addTriggerEntity(trigger, new Grunt(player, 1000, 900));

        //Vertical street
        trigger = new Trigger(1465, 2400, 3025 - 1465, defaultSize, "SPAWN", 2);
        addTriggerEntity(trigger, new Grunt(player, 1630, 1925));
        addTriggerEntity(trigger, new Grunt(player, 2734, 2093));
        addTriggerEntity(trigger, new Henchman(player, 2400, 1973));
        addTriggerEntity(trigger, new Assassin(player, 1598, 2909));
        addTriggerEntity(trigger, new Grunt(player, 2000, 2909)); 
        addTriggerEntity(trigger, new Grunt(player, 1830, 2669));
        addTriggerEntity(trigger, new Grunt(player, 2558, 2629));

        addTriggerEntity(trigger, new Assassin(player, 1670, 22));
        addTriggerEntity(trigger, new Assassin(player, 2334, 22));
        addTriggerEntity(trigger, new Assassin(player, 2946, 22));

        addTriggerEntity(trigger, new Henchman(player, 1561, 4019));
        addTriggerEntity(trigger, new Henchman(player, 2200, 4019));
        addTriggerEntity(trigger, new Henchman(player, 2857, 4019));
        
        //Block level switch
        trigger = new Trigger(3000, (int) (3290 - player.getBoundingBox().getHeight() - defaultSize), defaultSize, 4370 - (int) (3290 - player.getBoundingBox().getHeight() - defaultSize), "LEVEL BARRICADE", 1);
        addTriggerEntity(trigger, new Wall(3000 + defaultSize, 3263, defaultSize, 4370 - 3265 + 17, Color.black));
        //Level switch
        new Trigger(3595, (int) (3290 - player.getBoundingBox().getHeight() - defaultSize), defaultSize, 4370 - (int) (3290 - player.getBoundingBox().getHeight() - defaultSize), "LEVEL", 2);
    }


    private void playerDied() {
        // gamePanel.startGame(); //Uncomment this for a good time ;)
        if (levelSound != null)
        {
            levelSound.stop();
            levelSound.play();

        }
        respawn();
        
        resetTriggerEvents();
    }


    private void addTriggerEntity(Trigger trigger, GameEntity entity) {
        String triggerType = trigger.getTriggerType();
        int triggerValue = trigger.getTriggerValue();

        if (!triggerEntitiesMap.containsKey(triggerType)) {
            triggerEntitiesMap.put(triggerType, new HashMap<>());
        }

        Map<Integer, Vector<GameEntity>> entityMap = triggerEntitiesMap.get(triggerType);
        if (!entityMap.containsKey(triggerValue)) {
            entityMap.put(triggerValue, new Vector<>());
        }

        Vector<GameEntity> entities = entityMap.get(triggerValue);
        entities.add(entity);
    }


    private void removeTriggerEntity(Trigger trigger, GameEntity entity) {
        String triggerType = trigger.getTriggerType();
        int triggerValue = trigger.getTriggerValue();

        if (triggerEntitiesMap.containsKey(triggerType)) {
            Map<Integer, Vector<GameEntity>> entityMap = triggerEntitiesMap.get(triggerType);
            if (entityMap.containsKey(triggerValue)) {
                Vector<GameEntity> entities = entityMap.get(triggerValue);
                entities.remove(entity);
            }
        }
    }


    public Vector<GameEntity> getTriggerEntities(Trigger trigger) {
        String triggerType = trigger.getTriggerType();
        int triggerValue = trigger.getTriggerValue();

        if (triggerEntitiesMap.containsKey(triggerType)) {
            Map<Integer, Vector<GameEntity>> entityMap = triggerEntitiesMap.get(triggerType);
            if (entityMap.containsKey(triggerValue)) {
                return entityMap.get(triggerValue);
            }
        }

        return null;
    }


    public Vector<GameEntity> getTriggerEntities(String triggerType, int triggerValue) {
        if (triggerEntitiesMap.containsKey(triggerType)) {
            Map<Integer, Vector<GameEntity>> entityMap = triggerEntitiesMap.get(triggerType);
            if (entityMap.containsKey(triggerValue)) {
                return entityMap.get(triggerValue);
            }
        }

        return null;
    }
    
 
  
    public void triggerEvent(Trigger trigger) {
        String triggerType = trigger.getTriggerType();
        int triggerValue = trigger.getTriggerValue();

        if (triggerType.equals("LEVEL")) {
            level = triggerValue;
            changeLevel = true;
        }
        else if (triggerType.equals("SPAWN")) { 
            Vector<GameEntity> triggerEntities = getTriggerEntities(trigger);
            if (triggerEntities == null) 
                return;
            for (GameEntity entity : triggerEntities) {
                entity.setVisible(true);
            }
        }
        else if (triggerType.equals("LEVEL BARRICADE")) {
            //check if enemies are present
            if (gameEntities != null)
            {
                for (GameEntity entity : gameEntities) {
                    if (entity instanceof Enemy) {
                        return;
                    }
                }
            }

            //if no enemies are present
            Vector<GameEntity> triggerEntities = getTriggerEntities(trigger);
            if (triggerEntities == null) 
                return;
            for (GameEntity entity : triggerEntities) {
                if (entity instanceof Wall) {
                    entity.setVisible(false);
                }
            }
        }
        else if (triggerType.equals("BOSSBATTLE")) {
            // showBossBattleText("The Don has entered the building!");
            this.showBossHealthBar = true;
            Vector<GameEntity> triggerEntities = getTriggerEntities(trigger);

            if (triggerEntities != null) {
                for (GameEntity entity : triggerEntities) {
                    if (entity instanceof Wall)
                        entity.setVisible(true);
                    else if(entity.getX() > 2090)
                        continue;
                    else if (entity instanceof TheDon)
                        entity.setLocation(4064, 1264);
                    else if (entity instanceof Enemy)
                        entity.setX((int) (Math.random() * 1000 + 2090));
                }
            }
        }
        else
            throw new IllegalArgumentException("Invalid trigger type: " + triggerType);
    }


    public void resetTriggerEvents() {
        if (level == 2) {
            this.showBossHealthBar = false;
            Vector<GameEntity> triggerEntities = getTriggerEntities("BOSSBATTLE", 1);
            
            if (triggerEntities != null) {
                for (GameEntity entity : triggerEntities){
                    if (entity instanceof Wall)
                        entity.setVisible(false);
                }
            }
        }
    }


    public void level2() {
        levelSound = new Sound("sounds/test 2.wav", true, 0.8f);
        levelSound.play();
        gamePanel.setBackground("images/Level2Extended.gif", 4680, 2600);
        setMapBoundaries(0, 0, 4680 - 50, 2160 - 100);

        setPlayerStartingPosition(10, 1220);
        player.setFacingRight(true);

        new Wall(0, 800, 2080, (int) (1190 - 800 - player.getBoundingBox().getHeight())); //starting roof
        new Wall(0, 1410 + 15, 2080, 25);

        new Wall(1980, 0, 4680 - 1980, (int) (280 - player.getBoundingBox().getHeight())); // dojo roof
        new Wall(1980, 0, 2080 - 1980, (int) (800 + (1190 - 800 - player.getBoundingBox().getHeight()))); // dojo top left wall
        new Wall(1980, 1410 + 15, 2080 - 1980, 2160 - 1410 - 5); // dojo bottom left wall
        new Wall(4545, 0, 160, mapBoundaries[3] + 55); //dojo right wall       
        new Wall(1980, 2000 + 90, 4680 - 1980, 25) ; //dojo floor

        Trigger bossWallTrigger = new Trigger((int) (player.getWidth()/2 + 2060), 1110, 2140 - 2060, 1404 - 1110 + 15, "BOSSBATTLE", 1);
        
        addTriggerEntity(bossWallTrigger, new TheDon(player, 4064, 1264));
        addTriggerEntity(bossWallTrigger, new Assassin(player, 4064, 1200));
        addTriggerEntity(bossWallTrigger, new Henchman(player, 4064, 1300));
        addTriggerEntity(bossWallTrigger, new Grunt(player, 4064, 1400));
        addTriggerEntity(bossWallTrigger, new Grunt(player, 4000, 1500));
        addTriggerEntity(bossWallTrigger, new Grunt(player, 4000, 1600));

        Vector<GameEntity> enemies = getTriggerEntities(bossWallTrigger); //making the enemies visible when level is generated. THis is intentional
        for (GameEntity entity : enemies) 
            ((Enemy) entity).setVisible(true);

        // GameEntity bossWall = new Wall(2080, (int) (1190 - player.getBoundingBox().getHeight()), -21, (int) (1425 - 1190 + player.getBoundingBox().getHeight()), new Color(147, 70, 49)); //rgb(147, 70, 49)
        GameEntity bossWall = new Wall(2080, 700, -21, 1430 - 700, new Color(147, 70, 49)); //rgb(147, 70, 49)
        bossWall.setVisible(false);
        addTriggerEntity(bossWallTrigger, bossWall);
    }
}
