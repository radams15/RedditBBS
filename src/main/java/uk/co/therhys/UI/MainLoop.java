package uk.co.therhys.UI;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import uk.co.therhys.ClientConnection;
import uk.co.therhys.Reddit.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainLoop {
    private ClientConnection conn;
    private Client client;

    public MainLoop(ClientConnection conn, Client client) {
        this.conn = conn;
        this.client = client;
    }

    public void run() {
        hotPosts();
    }

    private void hotPosts(){
        int perPage = 5;

        List<Submission> pageSubs = new ArrayList<>();
        int i;
        int page = 0;
        boolean onSamePage;

        for(Listing<Submission> submissions : client.getHot(perPage)){
            pageSubs.clear();
            i=0;
            page++;
            onSamePage = true;

            while(onSamePage) {
                try {
                    conn.writeln("****** Page %d ******", page);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Submission submission : submissions) {
                    try {
                        pageSubs.add(submission);
                        conn.writeln("[%d] %s %s by %s", i, (submission.isSelfPost() ? "(st)" : ""), submission.getTitle(), submission.getAuthor());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    i++;
                }

                try {

                    conn.writeln();
                    conn.write("[id/N/R]    "); // post id or next page or return.
                    String feedback = conn.readln();
                    if (feedback.toLowerCase().equals("r")) {
                        return;
                    }

                    try {
                        int postId = Integer.parseInt(feedback);

                        if (postId > 0 && postId < perPage) {
                            Submission sub = submissions.get(postId);

                            new PostViewer(conn, sub).run(); // View the post
                        }
                    } catch (NumberFormatException ex) {
                        // Not an integer and not 'r' so must be 'n'.
                        // Go to next post.
                        onSamePage = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
