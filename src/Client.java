import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private Sprite player;
    private Sprite enemy;

    private SnakeGame game;

    private static final int FRAME_RATE = 20;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private boolean gameOver =false;

    private JFrame Frame;






    public static void main(String[] args) throws IOException {
        try {

            final JFrame frame = new JFrame("Jacob is super cool Game");
            frame.setSize(WIDTH, HEIGHT);
            Client client = new Client(new Socket("10.88.111.5", 2831));
            client.Frame=frame;
            StartMenu menu = new StartMenu(WIDTH,HEIGHT,client);
            StartMenu menu1 = new StartMenu(menu);


            frame.add(menu);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.pack();
            menu.run();
            frame.remove(menu);


            client.player = new Sprite(100, 200, 100);
            client.enemy = new Sprite(100, 200, 100);
            client.game = new SnakeGame(WIDTH, HEIGHT, client.player, client.enemy, client);
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



    public JFrame getFrame(){
        return Frame;
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

    public void sendMessage(Client client) {

        try {
            if (socket.isConnected()) {
                dataOutputStream.writeDouble(player.getPosX());
                dataOutputStream.writeDouble(player.getPosY());
                if(client.player.getRight()==1) {
                    dataOutputStream.writeDouble(1);
                }
                else{
                    dataOutputStream.writeDouble(2);
                }
                dataOutputStream.writeDouble(enemy.getHealth());
                dataOutputStream.flush();
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
                enemy.setRight(dataInputStream.readDouble());
                player.setHealth((int)dataInputStream.readDouble());
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
