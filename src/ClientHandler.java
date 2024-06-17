import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

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

    public void removeClientHandler() {
        clientHandlers.remove(this);
    }

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
