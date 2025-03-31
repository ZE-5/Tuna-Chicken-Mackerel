import javax.swing.*;            // need this for GUI objects
import java.awt.*;            // need this for Layout Managers
import java.awt.event.*;        // need this to respond to GUI events
    

public class GameWindow extends JFrame implements KeyListener{
    private GraphicsDevice device; 
    private int width, height;
    private GamePanel gamePanel;

    public GameWindow(){
        initFullScreen();
        gamePanel = new GamePanel(this);
        getContentPane().add(gamePanel);
        addKeyListener(this);
        
        this.setFocusable(true);
        this.requestFocus();
        setTitle("Change this to the name of the game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setVisible(true);
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

        width = getBounds().width;
        height = getBounds().height;

        System.out.println("Width of window is " + width);
        System.out.println("Height of window is " + height);
    }

    
    public void keyTyped(KeyEvent e) {
    }

    
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ESCAPE || (keyCode == KeyEvent.VK_F4 && e.isAltDown())) {
            device.setFullScreenWindow(null);
            System.exit(0);
        }
    }

    
    public void keyReleased(KeyEvent e) {

    }
}