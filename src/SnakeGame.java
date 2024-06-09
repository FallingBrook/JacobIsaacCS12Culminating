import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.util.ArrayList;

public class SnakeGame extends JPanel implements ActionListener {

    private final int width;
    private final int height;
    private static final int FRAME_RATE = 300;

    private Sprite player1 = new Sprite(50,50, 100);
    private Sprite enemy1 = new Sprite(50,50, 100);

    Client client;

    private int test=0;





    public SnakeGame(final int width, final int height, Sprite player, Sprite enemy, Client client) throws IOException {
        super();
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        player1=player;
        enemy1=enemy;
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
                    player1.getSpriteMovement().setDirX(-1);
                    player1.getSpriteMovement().setLeftKey(true);
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    player1.getSpriteMovement().setDirX(1);
                    player1.getSpriteMovement().setRightKey(true);
                }
                if(e.getKeyCode() == KeyEvent.VK_SPACE && player1.getSpriteMovement().canPJump()){
                    player1.getSpriteMovement().startJump();

                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    player1.getSpriteMovement().setLeftKey(false);
                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    player1.getSpriteMovement().setRightKey(false);
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
        graphics.setColor(Color.MAGENTA);
        //graphics.fillRect((int)player1.getPosX(), (int)player1.getPosY(), player1.getSize(),player1.getSize());
        graphics.setColor(Color.RED);
        graphics.fillRect((int)enemy1.getPosX(), (int)enemy1.getPosY(),player1.getSize(),player1.getSize());



        graphics.drawImage(player1.getFrame(0,test),(int)player1.getPosX(),(int)player1.getPosY(),this);













    }

    /** game loop. called according to frame rate
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        client.sendMessage();
        client.listenForMessage();
        player1.SpritePhysics();
        repaint();
        test++;
        if (test==5){
            test=0;
        }
    }
}
