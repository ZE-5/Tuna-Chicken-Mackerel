import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CharacterSelectionMenu extends JPanel {
    private boolean showHidden;
    private BufferedImage bufferedImage;
    private BufferedImage knifePlayer;
    private BufferedImage poolPlayer;
    private BufferedImage hiddenPlayer;
    private int currentSelection;
    private int selectedCharacter;
    
    private int characterFrameLength, characterFrameHeight, xOffset, yOffset, distanceBetween;

    public CharacterSelectionMenu(JFrame gameWindow, boolean showHidden) {
        characterFrameLength = 100;
        characterFrameHeight = 100;

        distanceBetween = characterFrameLength*2;
        
        xOffset = showHidden ? gameWindow.getWidth() / 2 - characterFrameLength * 3 / 2 - distanceBetween/2: gameWindow.getWidth() / 2 - characterFrameLength - distanceBetween / 4;
        yOffset = gameWindow.getHeight() / 2 - characterFrameHeight / 2;

        this.showHidden = showHidden;
        this.currentSelection = 0;
        this.selectedCharacter = -1;

        bufferedImage = new BufferedImage(gameWindow.getWidth(), gameWindow.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        Player player = new KnifePlayer(0, 0);
        knifePlayer = new BufferedImage(player.getWidth() + 1, player.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
        player.draw(knifePlayer.createGraphics());
        
        player = new PoolPlayer(0, 0);
        poolPlayer = new BufferedImage(player.getWidth() + 1, player.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
        player.draw(poolPlayer.createGraphics());


        if (showHidden) {
            player = new TheGun(0, 0);
            hiddenPlayer = new BufferedImage(player.getWidth() + 1, player.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
            player.draw(hiddenPlayer.createGraphics());
        }
    }


    public void displayOptions() {
        Graphics2D buffer = (Graphics2D) bufferedImage.getGraphics();
        buffer.setColor(Color.black);
        buffer.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight()); //erase screen | replace with background


        drawCharacter(buffer, knifePlayer, xOffset, yOffset, characterFrameLength, characterFrameHeight);        

        drawCharacter(buffer, poolPlayer, xOffset + distanceBetween, yOffset, characterFrameLength, characterFrameHeight);
        
        if (showHidden) {
            drawCharacter(buffer, hiddenPlayer, xOffset + 2 * distanceBetween, yOffset, characterFrameLength, characterFrameHeight);
        }


        buffer.setColor(Color.white); //selection rectangle
        if (currentSelection == 0) {
            buffer.drawRect(xOffset - 1, yOffset - 1, characterFrameLength + 1, characterFrameHeight + 1);
        } else if (currentSelection == 1) {
            buffer.drawRect(xOffset + distanceBetween - 1, yOffset - 1, characterFrameLength + 1, characterFrameHeight + 1);
        } else if (showHidden && currentSelection == 2) {
            buffer.drawRect(xOffset + 2 * distanceBetween - 1, yOffset - 1, characterFrameLength + 1, characterFrameHeight + 1);
        }

        Font font = new Font("Arial", Font.BOLD, 20);
        buffer.setFont(font);
        FontMetrics metrics = buffer.getFontMetrics();
        String text = "Knife";
        int x = xOffset + (characterFrameLength - metrics.stringWidth(text)) / 2;
        int y = yOffset + characterFrameHeight + 20 + 10;
        buffer.setColor(Color.white);
        buffer.drawString(text, x, y);

        text = "Pool";
        x = xOffset + distanceBetween + (characterFrameLength - metrics.stringWidth(text)) / 2;
        buffer.drawString(text, x, y);

        if (showHidden) {
            text = "Hidden";
            x = xOffset + 2 * distanceBetween + (characterFrameLength - metrics.stringWidth(text)) / 2;
            buffer.drawString(text, x, y);
        }
        
        text = "Press Enter to select character";
        x = (bufferedImage.getWidth() - metrics.stringWidth(text)) / 2;
        y = bufferedImage.getHeight() - 20;
        buffer.setColor(Color.white);
        buffer.drawString(text, x, y);

        Graphics2D g2 = (Graphics2D) this.getGraphics();
        g2.drawImage(bufferedImage, 0, 0, null);
        g2.dispose();
        buffer.dispose();
    }


    public void setShowHidden(boolean showHidden) {
        this.showHidden = showHidden;
    }


    public int getSelectedCharacter() {
        while (selectedCharacter == -1) {
            displayOptions();
        }
        return selectedCharacter;
    }


    private void drawCharacter(Graphics2D buffer, BufferedImage image, int xOffset, int yOffset, int characterFrameLength, int characterFrameHeight) {
        double scaleFactor = Math.min((double) characterFrameLength / image.getWidth(), (double) characterFrameHeight / image.getHeight());
        buffer.drawImage(image, (int) (xOffset + characterFrameLength/2 - image.getWidth()/2 * scaleFactor), yOffset, (int) (image.getWidth() * scaleFactor), (int) (image.getHeight() * scaleFactor), null);
    }



    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE || (keyCode == KeyEvent.VK_F4 && e.isAltDown())) {
            System.exit(0);
        }
        
        else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_S) {
            currentSelection = showHidden ? (currentSelection + 1) % 3 : (currentSelection + 1) % 2;
        }

        else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_W) {
            currentSelection = showHidden ? (currentSelection - 1 + 3) % 3 : (currentSelection - 1 + 2) % 2;
        }

        else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
            selectedCharacter = currentSelection;
        }
    }
}
