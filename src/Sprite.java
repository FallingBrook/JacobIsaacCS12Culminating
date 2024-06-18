import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite extends Rectangle implements ActionListener {



    private int size;

    private double posX;
    private double posY;

    private double right=1;

    private int health=10;



    // 0 = Idle,
    private ArrayList<BufferedImage> spriteSheets = new ArrayList<>();
    private int currentSpriteSheet;
    private Image currentSprite;
    private String currentAnim = "idle";


    private int spriteStartInd;
    private int spriteInd;
    private int currentSpriteIndLength;
    private final int[][] spriteSheetsCoordinates = {{0, 0, 70, 100}, {70, 0, 70, 100}, {0, 100, 70, 100}, {70, 100, 70, 100},
            {0, 0, 70, 100}, {70, 0, 70, 100}, {140, 0, 70, 100}, {0, 100, 70, 100}, {70, 100, 70, 100},
            {0, 0, 70, 100},
            {0, 0, 100, 100}, {100, 0, 100, 100}, {200, 0, 100, 100}, {300, 0, 100, 100}, {400, 0, 100, 100},
            {0, 0, 100, 100}, {100, 0, 100, 100}, {200, 0, 100, 100},
            {400, 0, 100, 100}, {300, 0, 100, 100}, {200, 0, 100, 100}, {100, 0, 100, 100}, {0, 0, 100, 100}};
    private Movement movement;

    public Sprite(int x, int y, int size){
        this.x = x;
        this.y = y;
        this.height = 100;
        this.width = 70;
        this.size = size;

        movement = new Movement(this);

        try {
            spriteSheets = new ArrayList<BufferedImage>();
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Idle.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Walk.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Jump.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Punch1.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Punch2.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Kick1.png")));
            System.out.println(spriteSheets.get(3).getWidth());
            currentSpriteSheet = 0;
//            ChangeAnim("idle");
            UpdateSprite();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // calls action performed method
        new javax.swing.Timer(70, this).start();
    }

    public void setHealth(int health){
        this.health=health;

    }





    public int getHealth(){
        return health;
    }

    public int getSize2(){
        return size;
    }

    public Image getSprite(){


        return currentSprite;
    }


    public double getPosX(){
        return x;
    }

    public double getPosY(){
        return y;
    }

    public Movement getSpriteMovement(){
        return movement;
    }

    public void setPosX (double x){
        this.posX = x;
        this.x=(int)x;
    }

    public void setPosY (double y){
        this.posY = y;
        this.y=(int)y;
    }

    public void SpritePhysics(){
        movement.UpdateMovement();
        movement.DetAnim();
    }

    public void UpdateSprite(){
        spriteInd++;
        if(spriteInd > currentSpriteIndLength)
            spriteInd = 0;
        if(spriteInd == currentSpriteIndLength)
            movement.setAttacking(false);
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


    public void setRight(double d){
        right = d;

    }

    public double getRight(){
        return right;
    }

    public void getImageFlip(){

        BufferedImage temp = flipImage(spriteSheets.get(currentSpriteSheet));
        currentSprite = temp.getSubimage(spriteSheetsCoordinates[spriteInd + spriteStartInd][0],
                        spriteSheetsCoordinates[spriteInd + spriteStartInd][1],
                        spriteSheetsCoordinates[spriteInd + spriteStartInd][2],
                        spriteSheetsCoordinates[spriteInd + spriteStartInd][3]);
    }

    public void ChangeAnim(String newAnim){
        if(currentAnim.equals(newAnim))
            return;
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
        }
        spriteInd = 0;
    }


    private BufferedImage flipImage(BufferedImage img){
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(width,height,img.getType());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img,0,0,width,height,width,0,0,height,null);
        g.dispose();
        return flippedImage;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        UpdateSprite();
    }
}

