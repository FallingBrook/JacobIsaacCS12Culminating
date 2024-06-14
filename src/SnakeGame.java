import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.util.ArrayList;

public class SnakeGame extends JPanel implements ActionListener {

    private final int width;
    private final int height;
    private static final int FRAME_RATE = 100;

    Client client;


    public SnakeGame(final int width, final int height,Client client) {
        super();
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        this.client = client;
    }




    public void startGame(){
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    client.getPlayer().getSpriteMovement().setDirX(-1);
                    client.getPlayer().getSpriteMovement().setLeftKey(true);
                    client.getPlayer().setRight(false);
                    client.getPlayer().ChangeAnim("walk");
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    client.getPlayer().setRight(true);
                    client.getPlayer().getSpriteMovement().setDirX(1);
                    client.getPlayer().getSpriteMovement().setRightKey(true);
                    client.getPlayer().ChangeAnim("walk");
                }

                if(e.getKeyCode() == KeyEvent.VK_SPACE && client.getPlayer().getSpriteMovement().canPJump()){
                    client.getPlayer().getSpriteMovement().startJump();

                }

            }
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    client.getPlayer().getSpriteMovement().setLeftKey(false);
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    client.getPlayer().getSpriteMovement().setRightKey(false);
                }
            }
        });

        // calls action performed method
        new Timer(1000 / FRAME_RATE, this).start();

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.setColor(Color.WHITE);
        graphics.setFont(graphics.getFont().deriveFont(30F));
        int currentHeight = height/4;
        final var graphics2D = (Graphics2D) graphics;
        final var frc = graphics2D.getFontRenderContext();
        String message = "Isaac>Jacob game\nPress space to play!";
        for(final var line : message.split("\n")){
            final var layout = new TextLayout(line, graphics.getFont(), frc);
            final var bounds = layout.getBounds();
            final var targetWidth = (float)(width - bounds.getWidth()) / 2;
            layout.draw(graphics2D, targetWidth, currentHeight);
            currentHeight += graphics.getFontMetrics().getHeight();
        }

        for (Sprite s: client.getAllplayers()){
            graphics.drawImage(s.getSprite(),(int)s.getPosX(),(int)s.getPosY(),null);
        }

    }

    /** game loop. called according to frame rate
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        client.sendMessage(client);
        client.listenForMessage(client);
        client.getPlayer().SpritePhysics();
        repaint();

    }



}
