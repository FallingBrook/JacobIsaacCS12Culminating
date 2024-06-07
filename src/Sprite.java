

public class Sprite {

    private double posX;
    private double posY;

    private int size;


    private Movement movement;

    public Sprite(int x, int y, int size){
        posX = x;
        posY = y;
        this.size = size;
        movement = new Movement(this);

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
