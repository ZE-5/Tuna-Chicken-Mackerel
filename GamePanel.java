import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    private boolean isRunning;
    private boolean isPaused;
    private LevelManager levelManager;
    private BufferedImage bufferedImage;


    public GamePanel(GameWindow gameWindow) {
        bufferedImage = new BufferedImage(gameWindow.getWidth(), gameWindow.getHeight(), BufferedImage.TYPE_INT_RGB);
        levelManager = new LevelManager();
    }


    public void run() {
        isRunning = true;
        while (isRunning) {
            if (!isPaused)
                update();
            render();
        }
    }


    private void update() {
        levelManager.update();
    }
    
    
    private void render() {
        Graphics2D buffer = (Graphics2D) bufferedImage.getGraphics();
        levelManager.draw(buffer);

        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(bufferedImage, 0, 0, null);

        g2.dispose();
        buffer.dispose();
    }
    
}
