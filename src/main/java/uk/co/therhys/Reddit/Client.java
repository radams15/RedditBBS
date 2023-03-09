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
import uk.co.therhys.Db.UserDao;

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

    public DefaultPaginator<Submission> getHot(int perPage){
        return reddit
            .frontPage()
            .limit(perPage)
            .build();
    }
}
