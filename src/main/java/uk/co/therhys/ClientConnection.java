package uk.co.therhys;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import uk.co.therhys.Reddit.Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ClientConnection extends Thread {
    private final Socket sock;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    private Client client;

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

    private void writeln(String val, Object ... args) throws IOException {
        writer.write(String.format(val, args));
        writer.write("\r\n");
        writer.flush();
    }

    private void writeln() throws IOException {
        writeln("");
    }

    private void hotPosts(){
        int perPage = 5;

        List<Submission> pageSubs = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();

        for(Listing<Submission> submissions : client.getHot(perPage)){
            pageSubs.clear();
            i.set(1);

            for(Submission submission : submissions){
                try {
                    pageSubs.add(submission);
                    writeln("[%d] %s by %s", i.get(), submission.getTitle(), submission.getAuthor());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                i.addAndGet(1);
            }

            try {
                writeln("[id/N/R]"); // post id or next page or return.
                String feedback = reader.readLine();
                if(feedback.toLowerCase().equals("r")){
                    return;
                }

                try{
                    int postId = Integer.parseInt(feedback);

                    if(postId > 0 && postId < perPage){
                        writeln("Chosen: post %d", postId);
                    }
                }catch (NumberFormatException ex){
                    // Go to next post
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        client = new Client(SECRETS.settings);

        try {
            writeln("******* Welcome to RedditBBS! *******");
            writeln();

            String test = reader.readLine();
            System.out.println(test);

            hotPosts();

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
