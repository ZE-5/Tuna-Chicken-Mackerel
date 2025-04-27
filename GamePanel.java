import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
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
    private static final long tickrate = 1000/36; 
    int x, y;
    // int[] drawingCoordinates;
    private Image background;
    private long pre;
    private GraphicsConfiguration config;
    public GamePanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        // bufferedImage = new BufferedImage(gameWindow.getWidth()*4, gameWindow.getHeight()*4, BufferedImage.TYPE_INT_RGB);
        isRunning = false;
        // bufferedImage.createGraphics();
        levelManager = LevelManager.getInstance(this);
        levelManager.initialize();
        keys = new boolean[5];
        x = 0;
        y = 0;
        pre = System.currentTimeMillis();
    }

    public void startGame() {
        Thread thread = new Thread(this);
        thread.start();
    }


    public void run() {
        isRunning = true;
        while (isRunning) {
            long diff = System.currentTimeMillis() - pre;
            if (diff > tickrate) {
                pre = System.currentTimeMillis();
                if (!isPaused)
                    update();
                render();
            }
        }
    }


    public GameWindow getGameWindow() {
        return gameWindow;
    }


    private void update() {
        levelManager.update(keys);
        
    }

    public void setBackground(String path, int width, int height) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        config = device.getDefaultConfiguration();
        bufferedImage = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        try {
            BufferedImage in = ImageIO.read(new File(path));
            background = config.createCompatibleImage(width, height);
            Graphics2D b2 = (Graphics2D) background.getGraphics();
            b2.drawImage(in, 0, 0, width, height, null);
            b2.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
