import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends JPanel implements ActionListener{

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
                        System.out.println(enemyPosX);
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
        Socket socket = new Socket("172.20.10.2", 2834);
        Client client = new Client(socket);
        client.getKeyStrokes();
        client.listenForMessage();
        client.sendMessage();

    }

    public void keyTyped(KeyEvent e) {

    }

    public void getKeyStrokes() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                int key = e.getKeyCode();

                if (key == KeyEvent.VK_LEFT) {
                    selfPosX -= 1;
                    System.out.println("worked");
                }

                if (key == KeyEvent.VK_RIGHT) {
                    selfPosX += 1;
                }

                if (key == KeyEvent.VK_UP) {
                    selfPosY += 1;
                }
            }
        });

    }

    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
