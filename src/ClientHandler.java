import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private boolean sent = false;

    private static int players;

    public ClientHandler(Socket socket, int num) {
        try {
            players=num;
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

                if(!sent){
                    while (true){
                        if (clientHandlers.size()==players){
                            break;
                        }
                    }
                    System.out.println("here");


                    for (ClientHandler clientHandler : clientHandlers) {
                        try {
                            clientHandler.dataInputStream.readInt();
                        } catch (IOException e) {
                            closeEverything(socket, dataInputStream, dataOutputStream);
                        }
                    }

                    broadcastMessage(clientHandlers.size());
                    brodcastClientNumber();

                }
                else {

                    int messageFromClient = dataInputStream.readInt();
                    broadcastMessage(messageFromClient);
                }
            } catch (IOException e) {
                closeEverything(socket, dataInputStream, dataOutputStream);
                break;
            }
        }
    }

    public void brodcastClientNumber(){

        for (ClientHandler clientHandler : clientHandlers) {
            try {
                    clientHandler.dataOutputStream.writeInt(clientHandlers.indexOf(clientHandler)+1);
                    clientHandler.dataOutputStream.flush();


            } catch (IOException e) {
                closeEverything(socket, dataInputStream, dataOutputStream);
            }
        }

        sent=true;

    }

    public void broadcastMessage(int messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.equals(this)) {
                    clientHandler.dataOutputStream.writeInt(messageToSend);
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
