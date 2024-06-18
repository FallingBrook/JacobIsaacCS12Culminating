import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.file.Paths;

public class Movement{
    // region movement variables
    private int moveSpeed = 6;
    private double moveAccel = 0.6;
    private double moveDeccel = 1;
    private int dir = 0;
    // endregion

    // region jump variables
    private boolean canJump;
    private int jumpSpeed = -25;
    private double gravityAccel = 2;
    private double maxFallSpeed = 15;
    private boolean isGrounded = false;

    // endregion

    private double veloX=0;
    private double veloY=2;
    private Sprite player;

    private boolean leftPressed;
    private boolean rightPressed;

    // region attack variables
    private boolean isAttacking = false;
    private boolean attack = false;
    private int attackNum = 1;
    private int maxCombo = 3;
    private double comboCldwn = 0.75;
    private double comboCldwnCounter;

    // endregion

    /** constructor of Movement class
     * @param player the sprite class
     */
    public Movement(Sprite player){
        this.player = player;
    }

    /** set isGrounded variable
     * @param b boolean
     */
    public void setGrounded(boolean b){
        isGrounded=b;
    }

    /** set isAttacking and attack variables
     * @param TorF boolean
     */
    public void setAttacking(boolean TorF){
        attack = TorF;
        isAttacking = TorF;
    }

    /** get the isAttacking variable
     * @return isAttacking variable
     */
    public boolean getAttacking(){
        return isAttacking;
    }

    /** set the leftPressed variable
     * @param bool boolean
     */
    public void setLeftKey(boolean bool){
        leftPressed = bool;
    }

    /** set the rightKey variable
     * @param bool boolean
     */
    public void setRightKey(boolean bool){
        rightPressed = bool;
    }

    /** set the x direction of the player
     * @param dir int
     */
    public void setDirX(int dir){
        this.dir = dir;
    }

    /** get the y velocity of the player
     * @return y velocity of the player
     */
    public double getVeloY(){
        return veloY;
    }

    /** set the y velocity of the player
     * @param y int vaue to set veloY
     */
    public void setVeloY(double y){
        veloY=y;

    }

    /** get if the player is grounded
     * @return if the player is grounded or not
     */
    public boolean getGrounded(){
        return isGrounded;
    }

    /**
     * Update the movement of the player
     */
    public void UpdateMovement(){
        if (player.getPosY() > 530 - player.getSize2()){
            isGrounded = true;
            player.setPosY(530-player.getSize2());
        }
        JumpPhysics();
        HorizontalMovement(dir);
        SetPosWithVel();
    }

    /**
     * start the Jump of the player
     */
    public void startJump(){
        isGrounded = false;
        canJump = false;
        veloY = jumpSpeed;
    }

    /** get if the player can jump or not
     * @return if the player can jump or not
     */
    public boolean canPJump(){
        return canJump;
    }

    /**
     * Handle the jump physics of the player
     */
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

    /**
     * Determine the sprite animation of the player
     */
    public void DetAnim(){
        comboCldwnCounter-=0.02;

        // in air
        if(!isGrounded){

            // doing an air attack
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
        // attacking on ground
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
        else {
            player.ChangeAnim("walk");
        }

    }

    /** Handle the horizontal movement of the player
     * @param dir direction of the player
     */
    public void HorizontalMovement(int dir){
        if(!leftPressed && !rightPressed || isAttacking){
            dir = 0;
        }
        // going right
        if(dir > 0 && veloX < moveSpeed){
            veloX += moveAccel;

            // going left and moving right
            if(veloX < 0){
                veloX += moveDeccel;
            }
        }
        // going left
        else if(dir < 0 && veloX > -moveSpeed){
            veloX -= moveAccel;

            // going right and moving left
            if(veloX > 0){
                veloX -= moveDeccel;
            }
        }
        // idle
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

    /**
     * Set the position of the player with the velocity of the player
     */
    public void SetPosWithVel(){
        player.setPosX((player.getPosX() + veloX));
        player.setPosY((player.getPosY() + veloY));
    }

}
