import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    //server components
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    //player and enemy
    private Sprite player;
    private Sprite enemy;

    //main game loop obkect
    private SnakeGame game;


    //size of window
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main(String[] args) throws IOException {
        try {

            //creating all necessary components for the game
            final JFrame frame = new JFrame("Jacob is super cool Game");
            frame.setSize(WIDTH, HEIGHT);
            Client client = new Client(new Socket("10.88.111.5", 2831));
            client.player = new Sprite(100, 200, 100);
            client.enemy = new Sprite(100, 200, 100);
            client.game = new SnakeGame(WIDTH, HEIGHT, client.player, client.enemy, client);

            //more window setup + start game
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

    /**
     * Constructor for client class
     * @param socket the socket to be used on the client side of the communication with the network
     */



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

    /**
     * Sends the message to the server in order to be distributed to other clients
     * @param client the client that is sending the message
     * @param game the client's game object
     */


    public void sendMessage(Client client,SnakeGame game) {

        try {
            if (socket.isConnected()) {
                //output player cords
                dataOutputStream.writeDouble(player.getPosX());
                dataOutputStream.writeDouble(player.getPosY());

                //output direction of player
                if(client.player.getRight()==1) {
                    dataOutputStream.writeDouble(1);
                }
                else{
                    dataOutputStream.writeDouble(2);
                }

                //ouput health and if they are ready to start the game
                dataOutputStream.writeDouble(enemy.getHealth());
                dataOutputStream.writeDouble(game.getReadyScreen());
                dataOutputStream.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, dataInputStream, dataOutputStream);
        }
    }

    /**
     * Recieves messages form the sever and updates the game accordingly
     * @param game the players game object
     */

    public void listenForMessage(SnakeGame game) {
        if (socket.isConnected()) {
            try {
                //setting position of enemy
                enemy.setPosX(dataInputStream.readDouble());
                enemy.setPosY(dataInputStream.readDouble());

                //setting enemy direction and health
                enemy.setRight(dataInputStream.readDouble());
                player.setHealth((int)dataInputStream.readDouble());

                //letting them know if other client is ready to begin the game
                game.setOtherReadyScreen((int)dataInputStream.readDouble());
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
