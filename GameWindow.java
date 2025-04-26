import javax.swing.*;            // need this for GUI objects
import java.awt.*;            // need this for Layout Managers
import java.awt.event.*;        // need this to respond to GUI events
    

public class GameWindow extends JFrame implements KeyListener{
    private GraphicsDevice device; 
    // private int width, height;
    private GamePanel gamePanel;
    private boolean selectingPlayer;
    private CharacterSelectionMenu characterSelectionMenu;

    public GameWindow(){
        selectingPlayer = false;
        initFullScreen();

        
        addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();
      
        setTitle("Tuna Chicken Mackerel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //TODO: hide TheGun
        gamePanel = new GamePanel(this);
        getContentPane().add(gamePanel);

        setVisible(true);
        gamePanel.startGame();
    }

    private void initFullScreen() {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = ge.getDefaultScreenDevice();

        setUndecorated(true);	// no menu bar, borders, etc.
        setIgnoreRepaint(true);	// turn off all paint events since doing active rendering
        setResizable(false);	// screen cannot be resized

        if (!device.isFullScreenSupported()) {
            System.out.println("Full-screen exclusive mode not supported");
            System.exit(0);
        }

        device.setFullScreenWindow(this); // switch on full-screen exclusive mode

        // we can now adjust the display modes, if we wish

        // width = getBounds().width;
        // height = getBounds().height;

        // System.out.println("Width of window is " + width);
        // System.out.println("Height of window is " + height);
    }

    
    public void keyTyped(KeyEvent e) {
    }

    
    public void keyPressed(KeyEvent e) {
        if (selectingPlayer || gamePanel == null) {
            characterSelectionMenu.keyPressed(e);
            return; // ignore key events while selecting character
        }


        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ESCAPE || (keyCode == KeyEvent.VK_F4 && e.isAltDown())) {
            device.setFullScreenWindow(null);
            System.exit(0);
        }

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) 
            gamePanel.setKeys(0, true);
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) 
            gamePanel.setKeys(1, true);
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) 
            gamePanel.setKeys(2, true);
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) 
            gamePanel.setKeys(3, true);
        if (keyCode == KeyEvent.VK_SPACE) 
            gamePanel.setKeys(4, true);
    }

    
    public void keyReleased(KeyEvent e) {
        if (selectingPlayer || gamePanel == null) {
            return; // ignore key events while selecting character
        }

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) 
            gamePanel.setKeys(0, false);
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) 
            gamePanel.setKeys(1, false);
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) 
            gamePanel.setKeys(2, false);
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) 
            gamePanel.setKeys(3, false);
        if (keyCode == KeyEvent.VK_SPACE) 
            gamePanel.setKeys(4, false);
        // if (keyCode == KeyEvent.VK_G)
        //     gamePanel.test();
    }


    public int selectCharacter(boolean showHidden) { // 0 = Knife, 1 = Pool, 2 = Hidden
        selectingPlayer = true;
        characterSelectionMenu = new CharacterSelectionMenu(this, showHidden);
        getContentPane().add(characterSelectionMenu);
        setVisible(true);
        characterSelectionMenu.displayOptions();
        int playerCharacter = characterSelectionMenu.getSelectedCharacter();
        selectingPlayer = false;
        getContentPane().remove(characterSelectionMenu);
        return playerCharacter;
    }
}