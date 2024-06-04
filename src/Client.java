import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements KeyListener {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private int enemyPosX=200;
    private int enemyPosY=300;

    private int selfPosX=100;

    private int selfPosY = 300;

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


            while (socket.isConnected()){
                bufferedWriter.write(selfPosX);
                bufferedWriter.newLine();
                bufferedWriter.write(selfPosY);
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

                while (socket.isConnected()){
                    try{
                        enemyPosX = Integer.parseInt( bufferedReader.readLine());
                        enemyPosY = Integer.parseInt( bufferedReader.readLine());
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
        Socket socket = new Socket("192.168.208.172", 2834);
        Client client = new Client(socket);
        client.listenForMessage();
        client.sendMessage();
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            selfPosX -= 1;
        }

        if (key == KeyEvent.VK_RIGHT) {
            selfPosX += 1;
        }

        if (key == KeyEvent.VK_UP) {
            selfPosY += 1;
        }

    }

    public void keyReleased(KeyEvent e) {

    }
}
