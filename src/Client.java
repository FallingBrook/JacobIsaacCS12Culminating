import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private Sprite player;

    private Sprite enemy;

    private SnakeGame game;

    private static final int FRAME_RATE = 20;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main(String[] args) throws IOException {

        try {

            final JFrame frame = new JFrame("Snake Game");
            frame.setSize(WIDTH, HEIGHT);
            Client client = new Client(new Socket("10.88.111.8", 2834));
            client.player=new Sprite(100,200);
            client.enemy = new Sprite(100,200);
            client.game=new SnakeGame(WIDTH, HEIGHT, client.player,client.enemy);
            frame.add(client.game);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.pack();
            client.game.startGame();
            client.listenForMessage(client);
            client.sendMessage(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    public Client(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(Client client){
        try{

            while (socket.isConnected()){



                bufferedWriter.write(String.valueOf(client.player.getPosX()));
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage(Client client){
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (socket.isConnected()){
                    try{
                        client.enemy.setPosX(Integer.parseInt(bufferedReader.readLine()));
                    }catch (IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null)
                bufferedReader.close();

            if(bufferedWriter != null)
                bufferedWriter.close();
            if(socket != null)
                socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
