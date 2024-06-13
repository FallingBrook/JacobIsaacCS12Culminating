import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    int clientNum;

    int playersNum;

    private Sprite player;
    private Sprite enemy1;

    private Sprite enemy2;

    private Sprite enemy3;

    private Sprite enemy4;

    private SnakeGame game;




    private static final int FRAME_RATE = 20;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;



    public static void main(String[] args) throws IOException {
        try {
            final JFrame frame = new JFrame("Jacob is super cool Game");
            frame.setSize(WIDTH, HEIGHT);
            Client client = new Client(new Socket("10.88.111.5", 2834));
            client.readyUp();

            client.clientNum = client.streamReadFirst();
            client.playersNum=client.streamReadFirst();


            System.out.println(STR."player\{client.clientNum}");



            switch(client.playersNum){
                case 2:

                    if(client.clientNum==1){
                        client.player = new Sprite(100,200,100);
                        client.enemy2 = new Sprite(700,200,100);
                        client.game = new SnakeGame(WIDTH, HEIGHT, client.player,client.enemy2, client);
                    }
                    else{
                        client.enemy1= new Sprite(100,200,100);
                        client.player = new Sprite(700,200,100);
                        client.game = new SnakeGame(WIDTH, HEIGHT, client.enemy1, client.player, client);
                    }
                    break;

                case 3:

                    if(client.clientNum==1){
                        client.player = new Sprite(100,200,100);
                        client.enemy2 = new Sprite(700,200,100);
                        client.enemy3 = new Sprite(200,200,100);
                        client.game = new SnakeGame(WIDTH, HEIGHT, client.player, client.enemy2,client.enemy3, client);
                    }
                    else if (client.clientNum==2){
                        client.enemy1= new Sprite(100,200,100);
                        client.player = new Sprite(700,200,100);
                        client.enemy3 = new Sprite(200,200,100);
                        client.game = new SnakeGame(WIDTH, HEIGHT, client.enemy1, client.player,client.enemy3, client);
                    }
                    else{
                        client.enemy1= new Sprite(100,200,100);
                        client.enemy2 = new Sprite(700,200,100);
                        client.player = new Sprite(200,200,100);
                        client.game = new SnakeGame(WIDTH, HEIGHT, client.enemy1, client.enemy2,client.player, client);
                    }
                    break;

                case 4:
                    if(client.clientNum==1){
                        client.player = new Sprite(100,200,100);
                        client.enemy2 = new Sprite(700,200,100);
                        client.enemy3 = new Sprite(200,200,100);
                        client.enemy4 = new Sprite(600,200,100);
                        client.game = new SnakeGame(WIDTH, HEIGHT, client.player, client.enemy2,client.enemy3,client.enemy4,client);

                    }
                    else if (client.clientNum==2){
                        client.enemy1= new Sprite(100,200,100);
                        client.player = new Sprite(700,200,100);
                        client.enemy3 = new Sprite(200,200,100);
                        client.enemy4 = new Sprite(600,200,100);
                        client.game = new SnakeGame(WIDTH, HEIGHT, client.enemy1, client.player,client.enemy3,client.enemy4,client);
                    }
                    else if (client.clientNum==3){
                        client.enemy1= new Sprite(100,200,100);
                        client.enemy2 = new Sprite(700,200,100);
                        client.player = new Sprite(200,200,100);
                        client.enemy4 = new Sprite(600,200,100);
                        client.game = new SnakeGame(WIDTH, HEIGHT, client.enemy1, client.enemy2,client.player,client.enemy4,client);
                    }
                    else{
                        client.enemy1= new Sprite(100,200,100);
                        client.enemy2 = new Sprite(700,200,100);
                        client.enemy3 = new Sprite(200,200,100);
                        client.player = new Sprite(600,200,100);
                        client.game = new SnakeGame(WIDTH, HEIGHT, client.enemy1, client.enemy2,client.enemy3,client.player,client);
                    }
                    break;
            }








            frame.add(client.game);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.pack();
            client.game.startGame();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.socket.setPerformancePreferences(1, 0, 2);
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            closeEverything(socket, dataInputStream, dataOutputStream);
        }

    }

    public void sendMessage() {

        try {
            if (socket.isConnected()) {
                dataOutputStream.writeInt(clientNum);
                dataOutputStream.writeDouble(player.getPosX());
                dataOutputStream.writeDouble(player.getPosY());
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, dataInputStream, dataOutputStream);
        }
    }

    public int streamReadFirst(){
        if (socket.isConnected()) {
            try {
                return dataInputStream.readInt();
            } catch (IOException e) {
                closeEverything(socket, dataInputStream, dataOutputStream);
            }
        }

        return -1;
    }

    public void readyUp(){

        try {
            if (socket.isConnected()) {
                Scanner sc = new Scanner(System.in);
                int ready = sc.nextInt();
                dataOutputStream.writeInt(ready);
            }
        } catch (IOException e) {
            closeEverything(socket, dataInputStream, dataOutputStream);
        }


    }


    public void listenForMessage(SnakeGame game) {
        if (socket.isConnected()) {
            try {
                int clientNum = dataInputStream.readInt();

                game.playerList.get(clientNum+1).setPosX(dataInputStream.readDouble());
                game.playerList.get(clientNum+1).setPosY(dataInputStream.readDouble());

            } catch (IOException e) {
                closeEverything(socket, dataInputStream, dataOutputStream);
            }
        }
    }

    public void closeEverything(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        try {
            if (dataInputStream != null) dataInputStream.close();
            if (dataOutputStream != null) dataOutputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
