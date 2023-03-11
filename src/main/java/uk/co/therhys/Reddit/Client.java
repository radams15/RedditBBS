package uk.co.therhys.Reddit;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import uk.co.therhys.BbsSettings;
import uk.co.therhys.Db.UserDao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client {
    private RedditClient reddit = null;
    public final BbsSettings settings;
    public Client(UserDao.RedditCreds creds, BbsSettings settings){
        this.settings = settings;

        Credentials jrawCreds = Credentials.script(creds.getUsername(), creds.getPassword(), creds.getCid(), creds.getSecret());

        UserAgent userAgent = new UserAgent("BBS", "uk.co.therhys.RedditBBS", "v0.1", creds.getUsername());

        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

        reddit = OAuthHelper.automatic(adapter, jrawCreds);
    }

    public interface SubmissionSelector {
        boolean submissionIsWanted(Submission sub);
    }

    public static class CustomPaginator implements Iterator<List<Submission>> {
        private DefaultPaginator<Submission> paginator;
        private int perPage;
        private SubmissionSelector selector;

        private List<Submission> overflow = new ArrayList<>();

        public CustomPaginator(DefaultPaginator<Submission> paginator, int perPage, SubmissionSelector selector) {
            this.paginator = paginator;
            this.perPage = perPage;
            this.selector = selector;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public List<Submission> next() {
            List<Submission> out = new ArrayList<>(overflow); // Make out with contents of overflow list.
            overflow.clear();

            if(out.size() == perPage){ // If we already have a full list then return it without iteration.
                return out;
            }

            Listing<Submission> subListing;
            while(out.size() < perPage){
                subListing = paginator.next();

                for(Submission sub : subListing){
                    if(out.size() < perPage){
                        if(selector.submissionIsWanted(sub)) {
                            out.add(sub);
                        }
                    }else{
                        if(selector.submissionIsWanted(sub)) {
                            overflow.add(sub);
                        }
                    }
                }
            }

            return out;
        }
    }

    public CustomPaginator getHot(int perPage, SubmissionSelector selector){
        return new CustomPaginator(reddit
            .frontPage()
            .limit(perPage)
            .build(), perPage, selector);
    }
}
