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
    private static final int FRAME_RATE = 300;

    private Sprite player1 = new Sprite(50,50, 100);
    private Sprite enemy1 = new Sprite(50,50, 100);

    Client client;

    public SnakeGame(final int width, final int height, Sprite player, Sprite enemy, Client client) {
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
                    player1.setRight(false);
                    player1.ChangeAnim("walk");


                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    player1.setRight(true);
                    player1.getSpriteMovement().setDirX(1);
                    player1.getSpriteMovement().setRightKey(true);
                    player1.ChangeAnim("walk");
                    System.out.println("right pressed");
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
//        new Timer(1000 / FRAME_RATE, this).start();
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

        graphics.drawImage(player1.getSprite(), (int)player1.getPosX(), (int)player1.getPosY(), null);
        graphics.setColor(Color.RED);
        graphics.drawImage(enemy1.getSprite(), (int)enemy1.getPosX(), (int)enemy1.getPosY(), null);
//        graphics.fillRect((int)enemy1.getPosX(), (int)enemy1.getPosY(),player1.getSize(),player1.getSize());

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
    }



}
