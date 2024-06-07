public class Sprite {

    private int posX;
    private int posY;

    private int size;

    private String name;

    private boolean playerIsJump;
    private int jumpHeight = 100;
    private int jumpSpeed = 8;
    private boolean canJump;
    private int jumpStartPos;

    public Sprite(int x, int y, int size){
        posX = x;
        posY = y;
        this.size = size;
    }

    public int getSize(){
        return size;
    }

    public int getPosX(){
        return posX;
    }

    public int getPosY(){
        return posY;
    }

    public void setPosXRunning(int x){
        posX+=x;
    }

    public void setPosYRunning(int y){
        posY+=y;
    }

    public void setPosX (int x){
        posX = x;
    }

    public void setPosY (int y){
        posY = y;
    }

    public void startJump(){
        jumpStartPos = getPosY();
        playerIsJump = true;
        canJump = false;
        jumpStartPos = getPosY();
    }

    public void JumpPhysics(){
        if(playerIsJump){
            if(getPosY() <= jumpStartPos - jumpHeight){
                playerIsJump = false;
                return;
            }
            setPosYRunning(-jumpSpeed);
        }
        else if(getPosY() < 600 - getSize()){
            setPosYRunning(8);
        }

        if(getPosY() >= jumpStartPos && !canJump){
            canJump = true;
        }
    }

    public boolean canPJump(){
        return canJump;
    }

}
