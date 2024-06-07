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
    private static final int FRAME_RATE = 50;

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
                    player1.setPosXRunning(-8);
                }
                if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                    player1.setPosXRunning(8);
                if(e.getKeyCode() == KeyEvent.VK_SPACE && player1.canPJump()){
                    player1.setVeloY(-200);

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
        graphics.fillRect(player1.getPosX(), player1.getPosY(), player1.getSize(),player1.getSize());
        graphics.setColor(Color.RED);
        graphics.fillRect(enemy1.getPosX(), enemy1.getPosY(),player1.getSize(),player1.getSize());

    }

    /** game loop. called according to frame rate
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        client.sendMessage();
        client.listenForMessage();

        player1.setPosXRunning(player1.getVeloX());
        player1.setPosYRunning(player1.getVeloY());

        //add terminal velocity
        if(player1.getVeloY()<4){
            player1.setPosYRunning(1);
        }


        if (player1.getPosY() > 600 - player1.getSize()){
            player1.setPosY(600-player1.getSize());
        }



        repaint();



        //player1.JumpPhysics();
    }



}
