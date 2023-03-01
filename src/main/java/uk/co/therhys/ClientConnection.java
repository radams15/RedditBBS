package uk.co.therhys;

import uk.co.therhys.Reddit.Client;
import uk.co.therhys.UI.MainLoop;

import java.io.*;
import java.net.Socket;

public class ClientConnection extends Thread {
    private final Socket sock;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public Client client;

    private boolean running = true;
    public ClientConnection(Socket sock) throws IOException {
        super();

        this.sock = sock;
        reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }

    public void write(String val) throws IOException {
        writer.write(val.replaceAll("\n", "\r\n"));
        writer.flush();
    }

    public void writeln(String val) throws IOException {
        write(val);
        write("\r\n");
    }

    public void writeln(String val, Object ... args) throws IOException {
        writeln(String.format(val, args));
    }

    public void writeln() throws IOException {
        writeln("");
    }

    public String readln() throws IOException {
        return reader.readLine();
    }

    @Override
    public void run() {
        client = new Client(SECRETS.settings, new BbsSettings("user", "pass", new int[]{80, 24}, true));

        try {
            writeln("******* Welcome to RedditBBS! *******");
            writeln();

            new MainLoop(this, client)
                    .run();
        }catch (IOException e){
            //e.printStackTrace();
        }

        try {
            close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void close() throws IOException {
        running = false;

        reader.close();
        writer.close();
        sock.close();
    }
}
