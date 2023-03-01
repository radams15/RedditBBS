package uk.co.therhys;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientListener {
    private final int port;
    private final List<ClientConnection> connections = new ArrayList<ClientConnection>();
    ClientListener(int port){
        this.port = port;
    }

    void listen() {
        try(ServerSocket ss = new ServerSocket(port)){
            Socket sock;
            ClientConnection clientConnection;
            while(true){
                sock = ss.accept();

                clientConnection = new ClientConnection(sock);
                clientConnection.start();

                connections.add(clientConnection);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
