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
                    player1.setRight(2);
                    player1.ChangeAnim("walk");



                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    player1.setRight(1);
                    player1.getSpriteMovement().setDirX(1);
                    player1.getSpriteMovement().setRightKey(true);
                    player1.ChangeAnim("walk");
                    System.out.println("right pressed");
                }

                if(e.getKeyCode() == KeyEvent.VK_SPACE && player1.getSpriteMovement().canPJump()){
                    player1.getSpriteMovement().startJump();

                }

                if(e.getKeyCode() == KeyEvent.VK_F){
                    //punch anim
                    if(checkCollision()&&player1.getCurrentAnim()!="punch"&&directionforPunch()){
                        enemy1.setHealth(1);
                    }
                }

            }

            public boolean directionforPunch(){
                if(player1.getRight()==1){
                    if(player1.getPosX()<enemy1.getPosX()){
                        return true;
                    }
                    else{
                        return false;
                    }

                }
                else{
                    if(player1.getPosX()<enemy1.getPosX()){
                        return false;
                    }
                    else{
                        return true;
                    }

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

        graphics.drawImage(player1.getSprite(), (int)player1.getPosX(), (int)player1.getPosY(), null);
        graphics.setColor(Color.GREEN);
        graphics.drawRect((int)player1.getPosX(), (int)player1.getPosY()-20,5*player1.getHealth(),10);
        graphics.drawImage(enemy1.getSprite(), (int)enemy1.getPosX(), (int)enemy1.getPosY(), null);
        graphics.setColor(Color.RED);
        graphics.drawRect((int)enemy1.getPosX(), (int)enemy1.getPosY()-20,5*enemy1.getHealth(),10);
    }

    public boolean checkCollision(){
        if(player1.intersects(enemy1)){
            System.out.println("hits");
        }
        return true;
    }

    /** game loop. called according to frame rate
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        client.sendMessage(client);
        client.listenForMessage();
        player1.SpritePhysics();
        repaint();
    }



}
