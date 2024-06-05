public class Sprite {

    private int posX;
    private int posY;

    private String name;

    public Sprite(int x, int y){
        posX = x;
        posY = y;
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



}
