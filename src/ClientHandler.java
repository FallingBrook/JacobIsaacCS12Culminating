import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    /**
     * ClientHandler constructor
     * @param socket socket to be used
     */

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            clientHandlers.add(this);
        } catch (IOException e) {
            closeEverything(socket, dataInputStream, dataOutputStream);
        }
    }

    /**
     * Loop that is continually sending messages across the server
     */

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                double messageFromClient = dataInputStream.readDouble();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, dataInputStream, dataOutputStream);
                break;
            }
        }
    }

    /**
     * Brodcasts the mesaage to other clients
     * @param messageToSend the number to be sent
     */

    public void broadcastMessage(double messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.equals(this)) {
                    clientHandler.dataOutputStream.writeDouble(messageToSend);
                    clientHandler.dataOutputStream.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, dataInputStream, dataOutputStream);
            }
        }
    }

    /**
     * removes the instance from the static arraylist
     */
    public void removeClientHandler() {
        clientHandlers.remove(this);
    }

    /**
     * Closes all server components
     * @param socket socket to be closed
     * @param dataInputStream data input stream to be closed
     * @param dataOutputStream data output stream to be closed
     */

    public void closeEverything(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        removeClientHandler();
        try {
            if (dataInputStream != null) dataInputStream.close();
            if (dataOutputStream != null) dataOutputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
