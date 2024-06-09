import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Sprite {


    //Physics
    private double posX;
    private double posY;

    private int size;
    private Movement movement;

    //Animation stuff

    private BufferedImage spriteSheet;
    final private int frameWidth = 47;
    final private int frameHeight = 106;

    final private int startX = 0;




    public Sprite(int x, int y, int size) throws IOException {
        posX = x;
        posY = y;
        this.size = size;
        movement = new Movement(this);

        //change to path

        spriteSheet = ImageIO.read(new File("C:/Users/isaac/OneDrive/Documents/GitHub/JacobIsaacCS12Culminating/src/try2.png"));

    }

    public Image getFrame(int col, int row) {
        return spriteSheet.getSubimage(startX+(frameWidth*row),0,frameWidth,frameHeight);



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
}
