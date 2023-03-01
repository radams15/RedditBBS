package uk.co.therhys.UI;

import lombok.AllArgsConstructor;
import net.dean.jraw.models.Submission;
import uk.co.therhys.ClientConnection;

import java.io.IOException;

@AllArgsConstructor
public class PostViewer {
    ClientConnection conn;
    Submission submission;

    void run(){
        try {
            conn.writeln("Viewing: %s", submission.getTitle());

            Pager.page(submission.getSelfText(), conn);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
