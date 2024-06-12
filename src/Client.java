import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    int clientNum;

    private Sprite player;
    private Sprite enemy;

    private Sprite enemy2;

    private Sprite enemy3;

    private SnakeGame game;




    private static final int FRAME_RATE = 20;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;



    public static void main(String[] args) throws IOException {
        try {
            final JFrame frame = new JFrame("Jacob is super cool Game");
            frame.setSize(WIDTH, HEIGHT);
            Client client = new Client(new Socket("172.20.10.2", 2834));
            client.readyUp();

            client.clientNum = client.streamReadFirst();

            System.out.println(STR."player\{client.clientNum}");

            client.player = new Sprite(100, 200, 100);

            client.enemy = new Sprite(700,200,100);

            if(client.clientNum==3){
                client.enemy2 = new Sprite(400,200,100);
                client.game = new SnakeGame(WIDTH, HEIGHT, client.player, client.enemy,client.enemy2, client);

            }

            else if(client.clientNum==4){
                client.enemy2 = new Sprite(400,200,100);
                client.enemy3 = new Sprite(200,200,100);
                client.game = new SnakeGame(WIDTH, HEIGHT, client.player, client.enemy,client.enemy2,client.enemy3, client);
            }
            else{
                client.game = new SnakeGame(WIDTH, HEIGHT, client.player, client.enemy, client);
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


    public void listenForMessage() {
        if (socket.isConnected()) {
            try {
                enemy.setPosX(dataInputStream.readDouble());
                enemy.setPosY(dataInputStream.readDouble());
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
