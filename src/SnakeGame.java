import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.IOException;
import java.util.ArrayList;

public class SnakeGame extends JPanel implements ActionListener {

    private final int width;
    private final int height;
    private static final int FRAME_RATE = 300;

    private Image platform = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("platform.png"));

    private BufferedImage healthBar = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("healthBar.png"));


    private Sprite player1 = new Sprite(50,50, 100);
    private Sprite enemy1 = new Sprite(50,50, 100);

    private int otherReadyScreen;

    private int readyScreen=0;

    Client client;

    private int screenType = 0;

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

                if(e.getKeyCode()==KeyEvent.VK_R){
                        readyScreen = 1;
                }

                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    player1.getSpriteMovement().setDirX(-1);
                    player1.getSpriteMovement().setLeftKey(true);
                    player1.setRight(2);

                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    player1.setRight(1);
                    player1.getSpriteMovement().setDirX(1);
                    player1.getSpriteMovement().setRightKey(true);
                }

                if(e.getKeyCode() == KeyEvent.VK_SPACE && player1.getSpriteMovement().canPJump()){
                    player1.getSpriteMovement().startJump();

                }

                if(e.getKeyCode() == KeyEvent.VK_F){
                    if(player1.getSpriteMovement().getAttacking())
                        return;
                    player1.getSpriteMovement().setAttacking(true);
                    if(checkCollision()&&directionforPunch()){
                        enemy1.setHealth(enemy1.getHealth()-1);
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

        new Timer(1000 / FRAME_RATE, this).start();

    }


    public int getReadyScreen(){
        return readyScreen;
    }

    public void setOtherReadyScreen(int r){
        otherReadyScreen=r;
    }

    public void setScreenType(int screenType){
        this.screenType = screenType;
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
    protected void paintComponent(Graphics graphics) {
        try {
            super.paintComponent(graphics);
            if(screenType == 0){
                try {
                    graphics.drawImage(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("StartScreen.png")), 0, 0, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                graphics.setColor(Color.WHITE);
                graphics.setFont(graphics.getFont().deriveFont(30F));
                int currentHeight = height / 4;
                final var graphics2D = (Graphics2D) graphics;
                final var frc = graphics2D.getFontRenderContext();
                String message = "Isaac>Jacob game\nPress space to play!";
                for (final var line : message.split("\n")) {
                    final var layout = new TextLayout(line, graphics.getFont(), frc);
                    final var bounds = layout.getBounds();
                    final var targetWidth = (float) (width - bounds.getWidth()) / 2;
                    layout.draw(graphics2D, targetWidth, currentHeight);
                    currentHeight += graphics.getFontMetrics().getHeight();
                }

                graphics.drawImage(player1.getSprite(), (int) player1.getPosX(), (int) player1.getPosY(), null);
                graphics.setColor(Color.GREEN);
                graphics.drawImage(healthBar.getSubimage(0, 0, (int) (4 * player1.getHealth()), 9), (int) player1.getPosX(), (int) player1.getPosY() - 20, null);
                graphics.drawImage(healthBar.getSubimage(0, 0, (int) (4 * enemy1.getHealth()), 9), (int) enemy1.getPosX() + 15, (int) enemy1.getPosY() - 20, null);
                graphics.drawImage(enemy1.getSprite(), (int) enemy1.getPosX(), (int) enemy1.getPosY(), null);
                graphics.drawImage(platform, 180, 430, null);
                graphics.drawImage(platform, 500, 430, null);
            }
        }
        catch(RasterFormatException r){
            System.out.println("game over close all the shit and say who won");

        }
    }

    public void onPlatform(){
        if(((player1.getPosX()+player1.width/2)>180&&(player1.getPosX()+player1.width/2)<300)||((player1.getPosX()+player1.width/2)>500&&(player1.getPosX()+player1.width/2)<620)){
            if((player1.getPosY()+player1.height>425&&player1.getPosY()+player1.height<435)&&player1.getSpriteMovement().getVeloY()>=0){
                player1.setPosY(370);
                player1.getSpriteMovement().setVeloY(0);
                player1.getSpriteMovement().setGrounded(true);
            }
        }
        else if(player1.getPosY()+ player1.height<600){
            player1.getSpriteMovement().setGrounded(false);
        }
    }

    public boolean checkCollision(){
        if(player1.intersects(enemy1)){
            return true;
        }
        return false;
    }

    /** game loop. called according to frame rate
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(final ActionEvent e) {

        if(readyScreen==1&&otherReadyScreen==1){
            screenType=1;
        }
        client.sendMessage(client,this);
        client.listenForMessage(this);
        player1.SpritePhysics();
        onPlatform();



        repaint();

    }



}
