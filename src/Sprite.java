

public class Sprite {
    // region movement var
    private int moveSpeed = 15;
    private double moveAccel = 0.6;
    private double moveDeccel = 1;
    private int dir = 0;
    // endregion

    // region jump var
    private boolean canJump;
    private int jumpSpeed = -20;
    private double gravityAccel = 2;
    private double maxFallSpeed = 15;
    private boolean isGrounded = false;

    // endregion

    private double veloX=0;
    private double veloY=2;
    private Sprite player;

    private boolean leftPressed;
    private boolean rightPressed;


    private double posX;
    private double posY;

    private int size;


//    private Movement movement;

    public Sprite(int x, int y, int size){
        posX = x;
        posY = y;
        this.size = size;
//        movement = new Movement(this);

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

//    public Movement getSpriteMovement(){
//        return movement;
//    }

    public void setPosX (double x){
        posX = x;


    }

    public void setPosY (double y){
        posY = y;
    }



    public void SpritePhysics(){

        UpdateMovement();
    }
    public void UpdateMovement(){
        if (getPosY() > 600 - getSize()){
            isGrounded = true;
            setPosY(600-getSize());
        }
        JumpPhysics();
        HorizontalMovement(dir);
        SetPosWithVel();
    }
    public void startJump(){
        isGrounded = false;
        veloY = jumpSpeed;
    }
    public boolean canPJump(){
        return canJump;
    }
    public void JumpPhysics(){
        if(!isGrounded){
            if(veloY < maxFallSpeed)
                veloY += gravityAccel;
            if(veloY > maxFallSpeed)
                veloY = maxFallSpeed;
        }
        // on ground
        else{
            veloY = 0;
            isGrounded = true;
        }

        if(isGrounded && !canJump){
            canJump = true;
        }
    }

    public void HorizontalMovement(int dir){
        if(!leftPressed && !rightPressed){
            dir = 0;
        }

        if(dir > 0 && veloX < moveSpeed){
            veloX += moveAccel;

//          going left and moving right
            if(veloX < 0){
                veloX += moveDeccel;
            }
        }
        else if(dir < 0 && veloX > -moveSpeed){
            veloX -= moveAccel;

//          going right and moving left
            if(veloX > 0){
                veloX -= moveDeccel;
            }
        }
        else if(dir == 0){
            if(veloX > 0 + moveDeccel)
                veloX -= moveDeccel;
            else if(veloX < 0 - moveDeccel)
                veloX += moveDeccel;
            else{
                veloX = 0;
            }

        }
    }

    public void SetPosWithVel(){
        setPosX((getPosX() + veloX));
        setPosY((getPosY() + veloY));
    }

    public void setLeftKey(boolean bool){
        leftPressed = bool;
    }
    public void setRightKey(boolean bool){
        rightPressed = bool;
    }
    public void setDirX(int dir){
        this.dir = dir;
    }
}
