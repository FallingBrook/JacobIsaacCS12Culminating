import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private static final int FRAME_RATE = 20;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main(String[] args) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame("Snake Game");
                frame.setSize(WIDTH, HEIGHT);
                SnakeGame game = new SnakeGame(WIDTH, HEIGHT);
                frame.add(game);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setVisible(true);
                frame.pack();
                game.startGame();
//                System.out.println(Thread.currentThread());
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    socket = new Socket("10.0.0.59", 2834);
                    Client client = new Client(socket);
                    client.listenForMessage();
                    client.sendMessage();
                    System.out.println(Thread.currentThread());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
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
            System.out.println(Thread.currentThread());
            bufferedWriter.write("username");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc= new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = sc.nextLine();

                bufferedWriter.write("username" + ": " + messageToSend);
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
                        System.out.println(Thread.currentThread());
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                        System.out.println("asd");
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
