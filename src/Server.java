import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    //socket to be used on server side of connection
    private final ServerSocket serverSocket;


    /**
     * Server constructor
     * @param serverSocket socket to be used
     */
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    /**
     * Starts the server
     */

    public void startServer(){
        try{
            while(!serverSocket.isClosed()){
                //creates the connection between sockets
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                //runs the client handler on a separate thread
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (IOException e){

        }
    }


    public static void main(String[] args) throws IOException{
        //create and start the server
        ServerSocket serverSocket = new ServerSocket(2831);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}