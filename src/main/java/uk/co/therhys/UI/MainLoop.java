package uk.co.therhys.UI;

import net.dean.jraw.models.Submission;
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

    private boolean shouldSelectPost(Submission sub){
        return (!client.settings.isSelftextOnly()) || sub.isSelfPost();
    }

    private void hotPosts(){
        int perPage = 5;

        List<Submission> pageSubs = new ArrayList<>();
        int i;
        int page = 0;
        boolean onSamePage;

        for (Client.CustomPaginator it = client.getHot(perPage, this::shouldSelectPost); it.hasNext(); ) {
            List<Submission> submissions = it.next();
            pageSubs.clear();
            page++;
            onSamePage = true;

            while(onSamePage) {
                i=0;

                try {
                    conn.writeln("****** Page %d ******", page);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Submission submission : submissions) {
                    try {
                        pageSubs.add(submission);
                        conn.writeln("[%d] %s %s by %s", i, (submission.isSelfPost() ? "(st)" : ""), submission.getTitle(), submission.getAuthor());
                        i++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {

                    conn.writeln();
                    conn.write("[id/N/Q]    "); // post id or next page or Quit.
                    String feedback = conn.readln();

                    if (feedback.toLowerCase().equals("q")) {
                        return;
                    }

                    try {
                        int postId = Integer.parseInt(feedback);

                        if (postId >= 0 && postId < perPage) {
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
