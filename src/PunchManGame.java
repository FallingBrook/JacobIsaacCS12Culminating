// Punch Man Game
// A multiplayer brawler game
// Jacob Brown and Isaac Simmie
// June 18, 2024

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PunchManGame extends JPanel implements ActionListener {

    private final int width;
    private final int height;
    private static final int FRAME_RATE = 300;

    private Image platform = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("platform.png"));

    private Image healthBar = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("HealthBarUI.png"));

    private Image background = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("background2.png"));

    private Image KO =  ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("KO.png"));

    private BufferedImage playerIcons  =  ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("PlayerIcons.png"));

    private Sprite player1;
    private Sprite enemy1;

    private int otherReadyScreen;

    private int readyScreen=0;

    Client client;

    private int screenType = 0;

    private boolean isGameOver = false;

    /**
     * Snake game constructor
     * @param width width of panel
     * @param height height of panel
     * @param player player sprite
     * @param enemy enemy sprite
     * @param client client running the game
     * @throws IOException
     */

    public PunchManGame(final int width, final int height, Sprite player, Sprite enemy, Client client) throws IOException {
        super();
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        player1=player;
        enemy1=enemy;
        this.client = client;
    }


    /**
     * Starts the game
     */
    public void startGame(){

        //window setup
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {

            //key events
            @Override
            public void keyPressed(KeyEvent e) {

                if(e.getKeyCode()==KeyEvent.VK_R){
                    readyScreen = 1;
                    if(otherReadyScreen==0){
                        enemy1.setPosX(700);
                        enemy1.setRight(2);
                    }
                    else {
                        player1.setPosX(700);
                        player1.setRight(2);
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    player1.getSpriteMovement().setDirX(-1);
                    player1.getSpriteMovement().setLeftKey(true);
                    player1.setRight(2);

                }
                else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    player1.setRight(1);
                    player1.getSpriteMovement().setDirX(1);
                    System.out.println("IN");
                    player1.getSpriteMovement().setRightKey(true);
                }
                if(e.getKeyCode() == KeyEvent.VK_B && player1.getSpriteMovement().getGrounded()){
                    player1.setBlock(true);

                    player1.ChangeAnim("block");
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
                if(e.getKeyCode() == KeyEvent.VK_B){
                    player1.setBlock(false);
                }
            }
        });

        //timer that calls action performed

        new Timer(1000 / FRAME_RATE, this).start();

    }

    /**
     * gets the players ready status
     * @return the status
     */
    public int getReadyScreen(){
        return readyScreen;
    }


    /**
     * gets the other players ready status
     * @return the status
     */
    public void setOtherReadyScreen(int r){
        otherReadyScreen=r;
    }

    /**
     * Checks if the player is facing the right direction to execute a punch
     * @return true or false if they can punch
     */
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


    /**
     * paints the screen
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        //starting screen
        if (screenType == 0) {
            try {
                graphics.drawImage(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("start.png")), 0, 0, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //main game
        } else {
            graphics.drawImage(background, 0, 0, null);
            graphics.drawImage(player1.getSprite(), (int) player1.getPosX(), (int) player1.getPosY(), null);
            graphics.drawImage(enemy1.getSprite(), (int) enemy1.getPosX(), (int) enemy1.getPosY(), null);

            // draw health bar
            graphics.drawImage(healthBar, 50, 20, null);
            graphics.setColor(Color.RED);
            graphics.fillRect(52, 33, 31 * player1.getHealth() - 1, 19);
            graphics.fillRect(439, 33, 31 * enemy1.getHealth() - 1, 19);

            // draw player icons
            graphics.drawImage(playerIcons.getSubimage(100, 0, 100, 100), 0, 0, null);
            graphics.drawImage(playerIcons.getSubimage(0, 0, 100, 100), 700, 0, null);



            graphics.drawImage(platform, 180, 400, null);
            graphics.drawImage(platform, 500, 400, null);

            //win loss state

            if (player1.getHealth() <= 0) {
                isGameOver = true;
                player1.ChangeAnim("death");
                enemy1.ChangeAnim("victory");
            }
            else if (enemy1.getHealth() <= 0) {
                isGameOver = true;
                enemy1.ChangeAnim("death");
                player1.ChangeAnim("victory");
            }
            if (isGameOver) {
                graphics.drawImage(KO, 50, 180, null);
            }
        }
    }


    /**
     * Checks if the player is on the platform
     */
    public void onPlatform(){
        //if on platform set height
        if(((player1.getPosX()+player1.width/2)>180&&(player1.getPosX()+player1.width/2)<300)||((player1.getPosX()+player1.width/2)>500&&(player1.getPosX()+player1.width/2)<620)){
            if((player1.getPosY()+player1.height>390&&player1.getPosY()+player1.height<410)&&player1.getSpriteMovement().getVeloY()>=0){

                player1.setPosY(340);

                player1.getSpriteMovement().setVeloY(0);

                player1.getSpriteMovement().setGrounded(true);

            }
        }
        else if(player1.getPosY()+ player1.height<530){
            player1.getSpriteMovement().setGrounded(false);
        }
    }

    /**
     * checks if players are touching each other
     * @return boolean for if they are touching
     */

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

        if(!isGameOver){

            if(readyScreen==1&&otherReadyScreen==1){
                screenType=1;
            }
            player1.SpritePhysics();
            onPlatform();
        }
        client.sendMessage(client,this);
        client.listenForMessage(this);

        repaint();

    }



}