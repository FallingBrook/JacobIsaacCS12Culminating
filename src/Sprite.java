import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Sprite extends Rectangle implements ActionListener {

    private int size;
    private double right=1;
    private int health=10;

    private ArrayList<BufferedImage> spriteSheets = new ArrayList<>();
    private int currentSpriteSheet;
    private Image currentSprite;
    private String currentAnim = "idle";
    private int spriteStartInd;
    private int spriteInd;
    private int currentSpriteIndLength;

    private boolean isBlocking = false;
    private boolean isHit = false;
    private final int[][] spriteSheetsCoordinates = {{0, 0, 70, 100}, {70, 0, 70, 100}, {0, 100, 70, 100}, {70, 100, 70, 100},
            {0, 0, 70, 100}, {70, 0, 70, 100}, {140, 0, 70, 100}, {210, 0, 70, 100}, {280, 0, 70, 100},
            {0, 0, 70, 100},
            {0, 0, 100, 100}, {100, 0, 100, 100}, {200, 0, 100, 100}, {300, 0, 100, 100}, {400, 0, 100, 100},
            {0, 0, 100, 100}, {100, 0, 100, 100}, {200, 0, 100, 100},
            {400, 0, 100, 100}, {300, 0, 100, 100}, {200, 0, 100, 100}, {100, 0, 100, 100}, {0, 0, 100, 100},
            {0, 0, 70, 100}, {70, 0, 70, 100}, {140, 0, 70, 100},
            {0, 0, 70, 100}, {70, 0, 70, 100}, {140, 0, 70, 100}, {210, 0, 70, 100},
            {0, 0, 70, 100}, {70, 0, 70, 100}, {140, 0, 70, 100},
            {0, 0, 70, 100}, {70, 0, 70, 100}, {140, 0, 70, 100}, {210, 0, 70, 100},
            {0, 0, 70, 100}, {70, 0, 70, 100}, {140, 0, 70, 100}, {210, 0, 70, 100},
            {0, 0, 100, 100}, {100, 0, 100, 100}, {200, 0, 100, 100}, {300, 0, 100, 100},
            {0, 0, 70, 180}, {70, 0, 70, 180}, {140, 0, 70, 180},
            {0, 0, 70, 100}};
    private Movement movement;


    /**
     * Sprite constructor
     * @param x position of sprite
     * @param y position of sprite
     * @param size size of sprite
     */

    public Sprite(int x, int y, int size){
        this.x = x;
        this.y = y;
        this.height = 100;
        this.width = 70;
        this.size = size;
        movement = new Movement(this);

        try {
            //add all animations to spriteSheet array list
            spriteSheets = new ArrayList<BufferedImage>();
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Idle.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Walk.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Jump.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Punch1.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Punch2.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Kick1.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("AirPunch1.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("AirKick1.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("AirKick2.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Hit1.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Hit2.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Death.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Victory.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Block.png")));
            currentSpriteSheet = 0;
            UpdateSprite();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // calls action performed method
        new javax.swing.Timer(100, this).start();
    }



    public void setHealth(int health){
        Random rd = new Random();
        if(this.health != health && rd.nextInt(0, 10) > 5 && health > 0){
            int rand = rd.nextInt(0, 2);
            if(rand == 0)
                ChangeAnim("hit1");
            else
                ChangeAnim("hit2");
            isHit = true;
        }


        if(this.health!=health)
        if(!isBlocking)
            this.health=health;

    }

    /**
     * gets the health of the sprite
     * @return the health
     */
    public int getHealth(){
        return health;
    }

    /**
     * gets the size of the sprite
     * @return the size
     */

    public int getSize2(){
        return size;
    }

    /**
     * gets the current sprite image
     * @return the image
     */

    public Image getSprite(){
        return currentSprite;
    }


    /**
     * gets the x position of the sprite
     * @return the x position
     */

    public double getPosX(){
        return x;
    }

    /**
     * gets the Y position of the sprite
     * @return the Y position
     */

    public double getPosY(){
        return y;
    }

    /**
     * gets the sprites movement object
     * @return the movement object
     */
    public Movement getSpriteMovement(){
        return movement;
    }

    /**
     * Sets the x position of the sprite
     * @param x  x position to be set
     */

    public void setPosX (double x){
        this.x=(int)x;
    }

    /**
     * Sets the Y position of the sprite
     * @param y y position to be set
     */

    public void setPosY (double y){
        this.y=(int)y;
    }

    /**
     * Sets if the sprite is blocking or not
     * @param x boolean for the block
     */
    public void setBlock(boolean x){
        isBlocking = x;
    }

    public void SpritePhysics(){
        if(isHit || (movement.getGrounded() && isBlocking))
            return;
        movement.UpdateMovement();
        movement.DetAnim();
    }

    public void UpdateSprite(){
        spriteInd++;
        if(spriteInd > currentSpriteIndLength){
            if(currentAnim.equals("death"))
                return;
            spriteInd = 0;
        }
        if(spriteInd == currentSpriteIndLength){
            movement.setAttacking(false);
            isHit = false;
        }
        if(right==1) {
            currentSprite=spriteSheets.get(currentSpriteSheet).getSubimage(spriteSheetsCoordinates[spriteInd + spriteStartInd][0],
                    spriteSheetsCoordinates[spriteInd + spriteStartInd][1],
                    spriteSheetsCoordinates[spriteInd + spriteStartInd][2],
                    spriteSheetsCoordinates[spriteInd + spriteStartInd][3]);
        }
        else{
            getImageFlip();

        }
    }

    /**
     * Sets the direction the sprite is facing
     * @param d direction
     */
    public void setRight(double d){
        right = d;

    }

    /**
     * gets the direction the sprite is facing
     * @return teh direction
     */

    public double getRight(){
        return right;
    }

    /**
     * Sets the current sprite image equal to the same image flipped across the y axis
     */

    public void getImageFlip(){
        BufferedImage temp = flipImage(spriteSheets.get(currentSpriteSheet));
        currentSprite = temp.getSubimage(spriteSheetsCoordinates[spriteInd + spriteStartInd][0],
                        spriteSheetsCoordinates[spriteInd + spriteStartInd][1],
                        spriteSheetsCoordinates[spriteInd + spriteStartInd][2],
                        spriteSheetsCoordinates[spriteInd + spriteStartInd][3]);
    }

    /**
     * gets current sprite sheet
     * @return index current sprite sheet
     */
    public int getAnimNum(){
        return currentSpriteSheet;
    }
    public void ChangeAnimFromServer(double anim){
        switch ((int) anim){
            case 0:
                ChangeAnim("idle");
                break;
            case 1:
                ChangeAnim("walk");
                break;
            case 2:
                ChangeAnim("jump");
                break;
            case 3:
                ChangeAnim("punch2");
                break;
            case 4:
                ChangeAnim("punch1");
                break;
            case 5:
                ChangeAnim("kick1");
                break;
            case 6:
                ChangeAnim("airpunch1");
                break;
            case 7:
                ChangeAnim("airkick1");
                break;
            case 8:
                ChangeAnim("airkick2");
                break;
            case 9:
                ChangeAnim("hit1");
                break;
            case 10:
                ChangeAnim("hit2");
                break;
            case 13:
                ChangeAnim("block");
                isBlocking = true;
                break;
        }
    }
    public void ChangeAnim(String newAnim){
        if(currentAnim.equals(newAnim))
            return;
        if(currentAnim.equals("block"))
            isBlocking = false;
        currentAnim = newAnim;
        switch (currentAnim){
            case "idle":
                currentSpriteSheet = 0;
                currentSpriteIndLength = 3;
                spriteStartInd = 0;
                break;
            case "walk":
                currentSpriteSheet = 1;
                currentSpriteIndLength = 4;
                spriteStartInd = 4;
                break;
            case "jump":
                currentSpriteSheet = 2;
                currentSpriteIndLength = 0;
                spriteStartInd = 9;
                break;
            case "punch2":
                currentSpriteSheet = 4;
                currentSpriteIndLength = 4;
                spriteStartInd = 10;
                break;
            case "punch1":
                currentSpriteSheet = 3;
                currentSpriteIndLength = 2;
                spriteStartInd = 15;
                break;
            case "kick1":
                currentSpriteSheet = 5;
                currentSpriteIndLength = 4;
                spriteStartInd = 18;
                break;
            case "airpunch1":
                currentSpriteSheet = 6;
                currentSpriteIndLength = 2;
                spriteStartInd = 23;
                break;
            case "airkick1":
                currentSpriteSheet = 7;
                currentSpriteIndLength = 3;
                spriteStartInd = 26;
                break;
            case "airkick2":
                currentSpriteSheet = 8;
                currentSpriteIndLength = 2;
                spriteStartInd = 30;
                break;
            case "hit1":
                currentSpriteSheet = 9;
                currentSpriteIndLength = 3;
                spriteStartInd = 33;
                break;
            case "hit2":
                currentSpriteSheet = 10;
                currentSpriteIndLength = 3;
                spriteStartInd = 37;
                break;
            case "death":
                currentSpriteSheet = 11;
                currentSpriteIndLength = 3;
                spriteStartInd = 41;
                break;
            case "victory":
                currentSpriteSheet = 12;
                currentSpriteIndLength = 2;
                spriteStartInd = 45;
                break;
            case "block":
                currentSpriteSheet = 13;
                currentSpriteIndLength = 0;
                spriteStartInd = 48;
                break;
        }
        spriteInd = 0;
    }

    /**
     * flips the image
     * @param img image to be flipped
     * @return teh flipped image
     */
    private BufferedImage flipImage(BufferedImage img){
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(width,height,img.getType());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img,0,0,width,height,width,0,0,height,null);
        g.dispose();
        return flippedImage;
    }


    /**
     * updates the sprite
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        UpdateSprite();
    }
}

