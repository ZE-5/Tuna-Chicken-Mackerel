import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    private boolean isRunning;
    private boolean isPaused;
    private LevelManager levelManager;
    private BufferedImage bufferedImage;
    private boolean[] keys;
    private GameWindow gameWindow;
    private static final long tickrate = 1000/60; 
    int x, y;
    // int[] drawingCoordinates;
    private Image background;

    public GamePanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        bufferedImage = new BufferedImage(gameWindow.getWidth()*3, gameWindow.getHeight()*3, BufferedImage.TYPE_INT_RGB);
        isRunning = false;
        // bufferedImage.createGraphics();
        levelManager = LevelManager.getInstance(this);
        levelManager.initialize();
        keys = new boolean[5];
        x = 0;
        y = 0;
        try {
            background = ImageIO.read(new File("images/level1back.gif"));
            background = background.getScaledInstance(2000, 2000, Image.SCALE_FAST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void startGame() {
        Thread thread = new Thread(this);
        thread.start();
    }


    public void run() {
        isRunning = true;
        while (isRunning) {
            if (!isPaused)
                update();
            render();
            
            try {
                Thread.sleep(tickrate);
            } 
            catch (Exception e) {
                
            }
        }
    }


    public GameWindow getGameWindow() {
        return gameWindow;
    }


    private void update() {
        levelManager.update(keys);
        
    }
    
    
    private void render() {
        if (!levelManager.holdScreenPositionX())
            x = levelManager.getBufferImageX();

        if (!levelManager.holdScreenPositionY())
            y = levelManager.getBufferImageY();
        
        Graphics2D buffer = (Graphics2D) bufferedImage.getGraphics();
        buffer.drawImage(background, 0, 0, null);

        levelManager.draw(buffer);
       
        Graphics2D g2 = (Graphics2D) this.getGraphics();
        g2.drawImage(bufferedImage, x, y, null);
        g2.dispose();
        buffer.dispose();
    }


    public void setKeys(int direction, boolean state) {
        keys[direction] = state;
        //UP, RIGHT, LEFT, DOWN, ATTACK(SPACE)
        //0,  1,     2,    3,    4
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }


    public void setScreenPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void setPlayerCharacter(int playerCharacter) {
        levelManager.setPlayerCharacter(playerCharacter);
    }
}
