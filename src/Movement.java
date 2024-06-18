import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Paths;

public class Movement{
    // region movement var
    private int moveSpeed = 6;
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

    private boolean isAttacking = false;
    private boolean attack = false;
    private int attackNum = 1;
    private int maxCombo = 3;
    private double comboCldwn = 0.75;
    private double comboCldwnCounter;

    public Movement(Sprite player){
        this.player = player;
    }

    public void setGrounded(boolean b){
        isGrounded=b;
    }

    public void setAttacking(boolean TorF){
        attack = TorF;
        isAttacking = TorF;
    }
    public boolean getAttacking(){
        return isAttacking;
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

    public double getVeloY(){
        return veloY;
    }

    public void setVeloY(double y){
        veloY=y;

    }
    public boolean getGrounded(){
        return isGrounded;
    }

    public void UpdateMovement(){
        if (player.getPosY() > 530 - player.getSize2()){
            isGrounded = true;
            player.setPosY(530-player.getSize2());
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
    public void DetAnim(){
        comboCldwnCounter-=0.02;
        if(!isGrounded){
            if(isAttacking){
                if(!attack){
                    return;
                }
                if(attackNum > maxCombo || comboCldwnCounter <= 0)
                    attackNum = 1;
                if(attackNum == 1)
                    player.ChangeAnim("airpunch1");
                else if(attackNum == 2 && comboCldwnCounter > 0)
                    player.ChangeAnim("airkick1");
                else if(attackNum == 3 && comboCldwnCounter > 0)
                    player.ChangeAnim("airkick2");
                comboCldwnCounter = comboCldwn;
                attackNum++;
                attack = false;
            }
            else{
                player.ChangeAnim("jump");
            }

        }
        else if(isAttacking) {
            if(!attack)
                return;
            if(attackNum > maxCombo || comboCldwnCounter <= 0)
                attackNum = 1;
            if(attackNum == 1)
                player.ChangeAnim("punch1");
            else if(attackNum == 2 && comboCldwnCounter > 0)
                player.ChangeAnim("punch2");
            else if(attackNum == 3 && comboCldwnCounter > 0)
                player.ChangeAnim("kick1");
            comboCldwnCounter = comboCldwn;
            attackNum++;
            attack = false;
        }
        else if(!leftPressed && !rightPressed){
            player.ChangeAnim("idle");
        }
        else if(leftPressed || rightPressed){
            player.ChangeAnim("walk");
        }

    }

    public void HorizontalMovement(int dir){
        if(!leftPressed && !rightPressed || isAttacking){
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
        player.setPosX((player.getPosX() + veloX));
        player.setPosY((player.getPosY() + veloY));
    }

}
