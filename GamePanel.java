import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

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


    public GamePanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        bufferedImage = new BufferedImage(gameWindow.getWidth()*3, gameWindow.getHeight()*3, BufferedImage.TYPE_INT_RGB);
        isRunning = false;
        // bufferedImage.createGraphics();
        levelManager = new LevelManager(this);
        keys = new boolean[5];
        x = 0;
        y = 0;
        // drawingCoordinates = new int[]{0, 0};
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
        Graphics2D buffer = (Graphics2D) bufferedImage.getGraphics();
        //TODO:replace with background image
        buffer.setColor(Color.black);
        buffer.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight()); //erase screen | replace with background

        levelManager.draw(buffer);

        Graphics2D g2 = (Graphics2D) this.getGraphics();
       
    //    updateDrawingCoordinates();
        if (!levelManager.holdScreenPositionX())
            x = levelManager.getBufferImageX();

        if (!levelManager.holdScreenPositionY())
            y = levelManager.getBufferImageY();

        g2.drawImage(bufferedImage, x, y, null);
        g2.dispose();
        buffer.dispose();
    }


    public void setKeys(int direction, boolean state) {
        keys[direction] = state;
        //UP, RIGHT, LEFT, DOWN, ATTACK(SPACE)
        //0,  1,     2,    3,    4
    }


    // public void updateDrawingCoordinates(){
    //     if (levelManager.holdScreenPosition())
    //         return;
        
    //     int[]  playerPosition = levelManager.getPlayerPosition();

    //     if (playerPosition[0] > getWidth()/2)
    //         drawingCoordinates[0] = -1 * (playerPosition[0] - getWidth()/2);
        
    //     if (playerPosition[1] > getHeight()/2)
    //         drawingCoordinates[1] = -1 * (playerPosition[1] - getHeight()/2);

    // }
}
