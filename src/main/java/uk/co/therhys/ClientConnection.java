package uk.co.therhys;

import uk.co.therhys.Db.DatabaseManager;
import uk.co.therhys.Db.UserDao;
import uk.co.therhys.Reddit.Client;
import uk.co.therhys.UI.MainLoop;

import java.io.*;
import java.net.Socket;

public class ClientConnection extends Thread {
    private final Socket sock;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private final DatabaseManager dbMan;

    public Client client;

    private boolean running = true;
    public ClientConnection(Socket sock, DatabaseManager dbMan) throws IOException {
        super();

        this.sock = sock;
        reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

        this.dbMan = dbMan;
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

    private UserDao.RedditCreds login() throws IOException {
        write("Username: ");
        String username = readln();

        write("Password: ");
        String password = readln();

        return dbMan.userDao().getCreds(new UserDao.User(username, password));
    }

    @Override
    public void run() {

        try {
            writeln("******* Welcome to RedditBBS! *******");
            writeln();

            UserDao.RedditCreds creds = login();

            if(creds == null) {
                writeln("Invalid username or password, terminating.");
                close();
                return;
            }

            writeln("Logged in as: %s", creds.getUsername());

            client = new Client(creds, new BbsSettings(new int[]{80, 24}, true));

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
