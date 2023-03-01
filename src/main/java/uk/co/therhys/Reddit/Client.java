package uk.co.therhys.Reddit;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Submission;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import uk.co.therhys.BbsSettings;

public class Client {
    private RedditClient reddit = null;
    public final BbsSettings settings;
    public Client(RedditSettings redditSettings, BbsSettings settings){
        this.settings = settings;

        Credentials creds = Credentials.script(redditSettings.getUsername(), redditSettings.getPassword(), redditSettings.getCid(), redditSettings.getSecret());

        UserAgent userAgent = new UserAgent("BBS", "uk.co.therhys.RedditBBS", "v0.1", redditSettings.getUsername());

        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

        reddit = OAuthHelper.automatic(adapter, creds);
    }

    public DefaultPaginator<Submission> getHot(int perPage){
        return reddit
            .frontPage()
            .limit(perPage)
            .build();
    }
}
