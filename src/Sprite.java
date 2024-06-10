import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Sprite implements ActionListener {

    private double posX;
    private double posY;

    private int size;

    private boolean right=true;



    // 0 = Idle,
    private ArrayList<BufferedImage> spriteSheets = new ArrayList<>();
    private int currentSpriteSheet;
    private Image currentSprite;
    private String currentAnim = "idle";
    private int spriteStartInd;
    private int spriteInd;
    private int currentSpriteIndLength;
    private final int[][] spriteSheetsCoordinates = {{0, 0, 70, 100}, {70, 0, 70, 100}, {0, 100, 70, 100}, {70, 100, 70, 100},
            {0, 0, 70, 100}, {70, 0, 70, 100}, {140, 0, 70, 100}, {0, 100, 70, 100}, {70, 100, 70, 100}};
    private Movement movement;

    public Sprite(int x, int y, int size){
        posX = x;
        posY = y;
        this.size = size;
        movement = new Movement(this);

        try {
            spriteSheets = new ArrayList<BufferedImage>();
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Idle.png")));
            spriteSheets.add(ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("Walk.png")));
            currentSpriteSheet = 0;
            ChangeAnim("idle");
            UpdateSprite();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // calls action performed method
        new javax.swing.Timer(100, this).start();
    }

    public Image getSprite(){
        return currentSprite;
    }

    public int getSize(){
        return size;

    }

    public double getPosX(){
        return posX;
    }

    public double getPosY(){
        return posY;
    }

    public Movement getSpriteMovement(){
        return movement;
    }

    public void setPosX (double x){
        posX = x;
    }

    public void setPosY (double y){
        posY = y;
    }

    public void SpritePhysics(){
        movement.UpdateMovement();
    }

    public void UpdateSprite(){
        spriteInd++;
        if(spriteInd > currentSpriteIndLength)
            spriteInd = spriteStartInd;

        if(right) {
            currentSprite=spriteSheets.get(currentSpriteSheet).getSubimage(spriteSheetsCoordinates[spriteInd][0],
                    spriteSheetsCoordinates[spriteInd][1],
                    spriteSheetsCoordinates[spriteInd][2],
                    spriteSheetsCoordinates[spriteInd][3]);
        }
        else{
            currentSprite=getImageFlip();

        }
    }

    public void setRight(boolean s){
        right = s;

    }

    public Image getImageFlip(){

        BufferedImage temp = flipImage(spriteSheets.get(currentSpriteSheet));

        return
        temp.getSubimage(spriteSheetsCoordinates[spriteInd][0],
                spriteSheetsCoordinates[spriteInd][1],
                spriteSheetsCoordinates[spriteInd][2],
                spriteSheetsCoordinates[spriteInd][3]);
    }

    public void ChangeAnim(String newAnim){
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
