import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            System.out.println("Enter number of players");
            Scanner sc = new Scanner(System.in);
            int num= sc.nextInt();
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket,num);
                System.out.println("Client Connected");
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (IOException e){

        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null) {
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(2834);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}