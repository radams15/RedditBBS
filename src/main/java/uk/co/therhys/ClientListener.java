package uk.co.therhys;

import uk.co.therhys.Db.DatabaseManager;
import uk.co.therhys.UI.ClientConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientListener {
    private final int port;
    private final DatabaseManager dbMan;
    private final List<ClientConnection> connections = new ArrayList<>();

    ClientListener(int port, DatabaseManager dbMan){
        this.port = port;
        this.dbMan = dbMan;
    }

    void listen() {
        try(ServerSocket ss = new ServerSocket(port)){
            Socket sock;
            ClientConnection clientConnection;

            System.out.println("Listening!");

            while(true){
                sock = ss.accept();

                clientConnection = new ClientConnection(sock, dbMan);
                clientConnection.start();

                connections.add(clientConnection);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
