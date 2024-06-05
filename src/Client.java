import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private Sprite player;

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
            client.game=new SnakeGame(WIDTH, HEIGHT, client.player);
            frame.add(client.game);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.pack();
            client.game.startGame();
            client.listenForMessage();
            client.sendMessage();
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

    public void sendMessage(){
        try{

            Scanner sc= new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = sc.nextLine();

                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while (socket.isConnected()){
                    try{
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println("recieved: " + msgFromGroupChat);
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
