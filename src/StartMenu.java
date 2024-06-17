import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class StartMenu extends JPanel implements ActionListener {

    private int width;
    private int height;

    private StartMenu thing;

    private Client client;

    final private Image screen= ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Screeny.png"));


    public StartMenu(final int width, final int height,Client client) throws IOException {
        this.width = width;
        this.height = height;
        this.client=client;
        setPreferredSize(new Dimension(width, height));
    }

    public StartMenu(StartMenu s) throws IOException {
        thing=s;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(screen,0,0,null);
    }

    public void run(){
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    client.getFrame().remove(thing);

                }
            }

        });
        new Timer(1000 / 20, this).start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();

    }
}
