package uk.co.therhys.UI;

import lombok.AllArgsConstructor;
import net.dean.jraw.models.Submission;

import java.io.IOException;

@AllArgsConstructor
public class PostViewer {
    ClientConnection conn;
    Submission submission;

    void run(){
        try {
            conn.writeln("Viewing: %s", submission.getTitle());

            String selftext = submission.getSelfText();

            if(selftext != null) {
                Pager.page(submission.getSelfText(), conn);
            } else {
                conn.writeln("Post has no selftext.");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
