import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    private int EnemyPosX=200;
    private int EnemyPosY=300;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc= new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = sc.nextLine();

                bufferedWriter.write(username + ": " + messageToSend);
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
                int msgFromGroupChat;

                while (socket.isConnected()){
                    try{
                        msgFromGroupChat = Integer.parseInt( bufferedReader.readLine());
                        System.out.println(msgFromGroupChat+3);
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

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = sc.nextLine();
        Socket socket = new Socket("192.168.208.172", 2834);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}
