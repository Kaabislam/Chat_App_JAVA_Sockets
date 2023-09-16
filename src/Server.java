import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        // for listening incoming network connections
        // TCP
        //
        final ServerSocket serverSocket;
        // for creating client side network connections
        // TCP / UDP
        final Socket clientSocket;

        // Efficient reading of text from character input streams
        // file,socket
        final BufferedReader in;
        // writing text based data to
        // character output streams
        // i.e. file,socket
        final PrintWriter out;

        // read input
        final Scanner sc = new Scanner(System.in);


        try {
            serverSocket = new ServerSocket(5000);
            clientSocket = serverSocket.accept();
            // allows to send text-based data to the
            // connected client.
            out = new PrintWriter(clientSocket.getOutputStream());
            // allows to read text-based
            // data from the connected client
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            Thread sender = new Thread(new Runnable() {
                // contains data write from user
                String msg;
                @Override
                public void run() {
                    while (true){
                        msg = sc.nextLine();    // read data from client keyboard
                        out.println(msg);       // write data on clientsocket
                        // ensure that any data that has been buffered
                        // is sent or written immediately
                        // forces the sending of data
                        out.flush();
                    }
                }
            });
            sender.start();

            Thread receive = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg!=null){
                            System.out.println("Client : "+msg);
                            msg = in.readLine();
                        }
                        System.out.println("Client Disconnected");
                        out.close();
                        clientSocket.close();
                        serverSocket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            receive.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}