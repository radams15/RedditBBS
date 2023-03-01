package uk.co.therhys;

import java.io.*;
import java.net.Socket;

public class ClientConnection extends Thread {
    private final Socket sock;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private boolean running = true;
    public ClientConnection(Socket sock) throws IOException {
        super();

        this.sock = sock;
        reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }

    private void writeln(String val) throws IOException {
        writer.write(val);
        writer.write("\r\n");
        writer.flush();
    }

    @Override
    public void run() {
        try {
            writeln("Welcome to RedditBBS!");

            String test = reader.readLine();
            System.out.println(test);

            while (running) {
                //writer.write("Hello!!");
            }
        }catch (IOException e){
            //e.printStackTrace();

            try {
                close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void close() throws IOException {
        running = false;

        reader.close();
        writer.close();
        sock.close();
    }
}
