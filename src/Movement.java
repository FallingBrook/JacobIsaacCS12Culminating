import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Paths;

public class Movement{
    // region movement var
    private double moveSpeed = 6;
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
    public Movement(Sprite player){
        this.player = player;
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

    public void UpdateMovement(){
        if (player.getPosY() > 600 - player.getSize()){
            isGrounded = true;
            player.setPosY(600-player.getSize());
        }
        JumpPhysics();
        HorizontalMovement(dir);
        SetPosWithVel();
    }
    public void startJump(){
        isGrounded = false;
        canJump = false;
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
           player.ChangeAnim("idle");
       }
        if(!isGrounded){
            player.ChangeAnim("jump");
        }
        else if(isGrounded){
            if(!leftPressed && !rightPressed){
                dir = 0;
                player.ChangeAnim("idle");
            }
            else{
                player.ChangeAnim("walk");
            }
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
        player.setPosX((player.getPosX() + veloX));
        player.setPosY((player.getPosY() + veloY));
    }



}
